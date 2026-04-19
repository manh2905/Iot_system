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
        fetchDevicesAndStatus()
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
                    
                    var currentState = _state.value
                    
                    // Update dynamic list
                    val updatedDevices = currentState.devices.map {
                        if (it.id == deviceId) {
                            it.copy(isChecked = isChecked, isLoading = false)
                        } else {
                            it
                        }
                    }
                    currentState = currentState.copy(devices = updatedDevices)
                    
                    _state.value = currentState
                }
            }
        }
    }

    private fun fetchDevicesAndStatus() {
        viewModelScope.launch {
            try {
                // Fetch the list of devices
                val deviceResponse = RetrofitClient.apiService.getDevices()
                if (deviceResponse.isSuccessful) {
                    val devicesData = deviceResponse.body()?.data ?: emptyList()
                    val currentDevices = _state.value.devices.associateBy { it.id }
                    
                    val newDevices = devicesData.map { item ->
                        currentDevices[item.id] ?: DeviceUiState(
                            id = item.id,
                            name = item.name,
                            isChecked = false,
                            isLoading = false
                        )
                    }
                    _state.value = _state.value.copy(devices = newDevices)
                }

                // Fetch the latest status
                val statusResponse = RetrofitClient.apiService.getLatestDeviceStatus()
                if (statusResponse.isSuccessful) {
                    val statuses = statusResponse.body()?.data ?: emptyList()
                    var currentState = _state.value
                    
                    // Update dynamic list
                    val updatedDevices = currentState.devices.map { device ->
                        val statusInfo = statuses.find { it.deviceId == device.id }
                        if (statusInfo != null) {
                            device.copy(isChecked = statusInfo.status == "ON")
                        } else {
                            device
                        }
                    }
                    currentState = currentState.copy(devices = updatedDevices)

                    _state.value = currentState
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleDevice(deviceId: Int, command: String) {
        // Set loading state
        var currentState = _state.value
        val updatedDevices = currentState.devices.map {
            if (it.id == deviceId) it.copy(isLoading = true) else it
        }
        currentState = currentState.copy(devices = updatedDevices)

        _state.value = currentState

        viewModelScope.launch {
            // Start a parallel 5-second timeout
            launch {
                delay(5000)
                val isStillLoading = _state.value.devices.find { it.id == deviceId }?.isLoading ?: false
                
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
        var currentState = _state.value
        val updatedDevices = currentState.devices.map {
            if (it.id == deviceId) it.copy(isLoading = false) else it
        }
        currentState = currentState.copy(devices = updatedDevices)

        _state.value = currentState
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




