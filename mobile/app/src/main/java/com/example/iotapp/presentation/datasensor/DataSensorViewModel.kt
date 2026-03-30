package com.example.iotapp.presentation.datasensor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapp.data.remote.api.RetrofitClient
import com.example.iotapp.data.remote.dto.SensorItemDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DataSensorState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<SensorItemDto> = emptyList(),
    
    // Pagination & Filters
    val sensorId: String = "All", // "All", "Temp", "Humidity", "Light"
    val page: Int = 1,
    val limitStr: String = "6",
    val sortBy: String = "createdAt",
    val sortOrder: String = "DESC",
    val date: String = "",
    val value: String = "",
    val totalPages: Int = 1
)

class DataSensorViewModel : ViewModel() {
    private val _state = MutableStateFlow(DataSensorState())
    val state: StateFlow<DataSensorState> = _state

    init {
        // Initial fetch
        fetchData()
    }

    fun onSensorChange(sensorId: String) {
        _state.value = _state.value.copy(sensorId = sensorId, page = 1)
        fetchData()
    }

    fun onDateChange(date: String) {
        _state.value = _state.value.copy(date = date, page = 1)
        fetchData()
    }

    fun onValueChange(value: String) {
        _state.value = _state.value.copy(value = value, page = 1)
        fetchData()
    }

    fun onRowsChange(limitStr: String) {
        _state.value = _state.value.copy(limitStr = limitStr, page = 1)
        fetchData()
    }

    fun onPageChange(page: Int) {
        if (page in 1.._state.value.totalPages) {
            _state.value = _state.value.copy(page = page)
            fetchData() // Auto-fetch on page change
        }
    }

    fun onSortChange(sortBy: String, sortOrder: String) {
        _state.value = _state.value.copy(sortBy = sortBy, sortOrder = sortOrder, page = 1)
        fetchData()
    }

    fun search() {
        _state.value = _state.value.copy(page = 1)
        fetchData()
    }

    private var fetchJob: Job? = null

    private fun fetchData() {
        fetchJob?.cancel()

        val currentState = _state.value
        _state.value = currentState.copy(isLoading = true, error = null)

        fetchJob = viewModelScope.launch {
            try {
                // Map filter values to what API expects
                val sensorFilter = when (currentState.sensorId) {
                    "Temp" -> "1"
                    "Humidity" -> "2"
                    "Light" -> "3"
                    else -> "all"
                }

                val dateFilter = currentState.date.takeIf { it.isNotBlank() }
                val valueFilter = currentState.value.toDoubleOrNull()
                val limitInt = currentState.limitStr.toIntOrNull() ?: 6

                Log.d("DataSensorViewModel", "Fetching data with: sensorId=$sensorFilter, page=${currentState.page}, limit=$limitInt, sortBy=${currentState.sortBy}, sortOrder=${currentState.sortOrder}, date=$dateFilter, value=$valueFilter")

                val res = RetrofitClient.apiService.getSensorData(
                    sensorId = sensorFilter,
                    page = currentState.page,
                    limit = limitInt,
                    sortBy = currentState.sortBy,
                    sortOrder = currentState.sortOrder,
                    date = dateFilter,
                    value = valueFilter
                )

                if (res.isSuccessful) {
                    val body = res.body()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        data = body?.data ?: emptyList(),
                        totalPages = body?.totalPages ?: 1
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Failed to load: ${res.code()}"
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Network error"
                )
            }
        }
    }
}
