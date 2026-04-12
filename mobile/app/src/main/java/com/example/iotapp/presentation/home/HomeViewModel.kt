package com.example.iotapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapp.core.socket.SocketManager
import com.example.iotapp.core.utils.Constants
import com.example.iotapp.data.remote.api.RetrofitClient
import com.example.iotapp.data.remote.dto.DeviceDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.json.JSONObject

class HomeViewModel : ViewModel (){
    private val _state = MutableStateFlow(HomeState())
    val state : StateFlow<HomeState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        connectSocket()
        observeSensor()
        observeDeviceStatus()
        fetchLatestDeviceStatus()
    }

    private fun observeSensor() {
        viewModelScope.launch {
            SocketManager.sensorData.collect { a ->
                a?.let {
                    updateState(it)
                }
            } }
        }

    private fun observeDeviceStatus() {
        viewModelScope.launch {
            SocketManager.deviceStatus.collect { pair ->
                pair?.let { (deviceIdStr, status) ->
                    val deviceId = deviceIdStr.toIntOrNull() ?: return@let
                    val isChecked = status == "ON"
                    
                    _state.value = when (deviceId) {
                        Constants.DEVICE_FAN -> _state.value.copy(fanState = _state.value.fanState.copy(isChecked = isChecked, isLoading = false))
                        Constants.DEVICE_LIGHT -> _state.value.copy(lightState = _state.value.lightState.copy(isChecked = isChecked, isLoading = false))
                        Constants.DEVICE_AC -> _state.value.copy(acState = _state.value.acState.copy(isChecked = isChecked, isLoading = false))
                        else -> _state.value
                    }
                }
            }
        }
    }

    private fun fetchLatestDeviceStatus() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getLatestDeviceStatus()
                if (response.isSuccessful) {
                    val statuses = response.body()?.data ?: emptyList()
                    var currentState = _state.value
                    statuses.forEach { statusInfo ->
                        val isChecked = statusInfo.status == "ON"
                        currentState = when (statusInfo.deviceId) {
                            Constants.DEVICE_FAN -> currentState.copy(fanState = currentState.fanState.copy(isChecked = isChecked))
                            Constants.DEVICE_LIGHT -> currentState.copy(lightState = currentState.lightState.copy(isChecked = isChecked))
                            Constants.DEVICE_AC -> currentState.copy(acState = currentState.acState.copy(isChecked = isChecked))
                            else -> currentState
                        }
                    }
                    _state.value = currentState
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleDevice(deviceId: Int, command: String) {
        // Set loading state
        _state.value = when (deviceId) {
            Constants.DEVICE_FAN -> _state.value.copy(fanState = _state.value.fanState.copy(isLoading = true))
            Constants.DEVICE_LIGHT -> _state.value.copy(lightState = _state.value.lightState.copy(isLoading = true))
            Constants.DEVICE_AC -> _state.value.copy(acState = _state.value.acState.copy(isLoading = true))
            else -> _state.value
        }

        viewModelScope.launch {
            // Start a parallel 5-second timeout
            launch {
                delay(5000)
                val isStillLoading = when (deviceId) {
                    Constants.DEVICE_FAN -> _state.value.fanState.isLoading
                    Constants.DEVICE_LIGHT -> _state.value.lightState.isLoading
                    Constants.DEVICE_AC -> _state.value.acState.isLoading
                    else -> false
                }
                if (isStillLoading) {
                    resetLoading(deviceId)
                    _toastEvent.emit("Mất kết nối")
                }
            }

            try {
                val response = RetrofitClient.apiService.toggleDevice(DeviceDto(deviceId, command))
                if (!response.isSuccessful) {
                    resetLoading(deviceId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                resetLoading(deviceId)
            }
        }
    }

    private fun resetLoading(deviceId: Int) {
        _state.value = when (deviceId) {
            Constants.DEVICE_FAN -> _state.value.copy(fanState = _state.value.fanState.copy(isLoading = false))
            Constants.DEVICE_LIGHT -> _state.value.copy(lightState = _state.value.lightState.copy(isLoading = false))
            Constants.DEVICE_AC -> _state.value.copy(acState = _state.value.acState.copy(isLoading = false))
            else -> _state.value
        }
    }

    private fun updateState(json: JSONObject) {

        val temperature = json.optDouble("temperature", 0.0)
        val humidity = json.optDouble("humidity", 0.0)
        val light = json.optDouble("light", 0.0)
        val currentTime = System.currentTimeMillis()

        _state.value = _state.value.copy(
            temperature = temperature,
            humidity = humidity,
            light = light,

            tempHistory = (_state.value.tempHistory + Pair(currentTime, temperature)).takeLast(5),
            humHistory = (_state.value.humHistory + Pair(currentTime, humidity)).takeLast(5),
            lightHistory = (_state.value.lightHistory + Pair(currentTime, light)).takeLast(5)
        )

    }
    private fun connectSocket() {
        SocketManager.connect()
    }

    override fun onCleared() {
        super.onCleared()
        SocketManager.disconnect()
    }
}




