package com.example.iotapp.presentation.actionhistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapp.data.remote.api.RetrofitClient
import com.example.iotapp.data.remote.dto.ActionItemDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ActionHistoryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<ActionItemDto> = emptyList(),
    
    // Pagination & Filters
    val deviceId: String = "All", // "All", "Light", "Fan", "AC"
    val page: Int = 1,
    val pageSizeStr: String = "6",
    val action: String = "All", // "All", "ON", "OFF"
    val status: String = "All", // "All", "ON", "OFF", "WAITING", "FAIL"
    val sortOrder: String = "DESC",
    val date: String = "",
    val totalPages: Int = 1
)

class ActionHistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(ActionHistoryState())
    val state: StateFlow<ActionHistoryState> = _state

    private var fetchJob: Job? = null

    init {
        fetchData()
    }

    fun onDeviceChange(deviceId: String) {
        _state.value = _state.value.copy(deviceId = deviceId, page = 1)
        fetchData()
    }

    fun onActionChange(action: String) {
        _state.value = _state.value.copy(action = action, page = 1)
        fetchData()
    }

    fun onStatusChange(status: String) {
        _state.value = _state.value.copy(status = status, page = 1)
        fetchData()
    }

    fun onDateChange(date: String) {
        _state.value = _state.value.copy(date = date, page = 1)
        fetchData()
    }

    fun onRowsChange(pageSizeStr: String) {
        _state.value = _state.value.copy(pageSizeStr = pageSizeStr, page = 1)
        fetchData()
    }

    fun onPageChange(page: Int) {
        if (page in 1.._state.value.totalPages) {
            _state.value = _state.value.copy(page = page)
            fetchData() 
        }
    }

    fun onSortChange(sortOrder: String) {
        _state.value = _state.value.copy(sortOrder = sortOrder, page = 1)
        fetchData()
    }

    private fun fetchData() {
        fetchJob?.cancel()

        val currentState = _state.value
        _state.value = currentState.copy(isLoading = true, error = null)

        fetchJob = viewModelScope.launch {
            try {
                // Map filter values to what API expects
                val deviceFilter = when (currentState.deviceId) {
                    "Light" -> "1"
                    "Fan" -> "2"
                    "AC" -> "3"
                    else -> "all"
                }

                val actionFilter = currentState.action.takeIf { it != "All" }
                val statusFilter = when (currentState.status) {
                    "All" -> null
                    "WAITING" -> "processing"
                    "FAIL" -> "fail"
                    else -> currentState.status // ON, OFF
                }

                val dateFilter = currentState.date.takeIf { it.isNotBlank() }
                val limitInt = currentState.pageSizeStr.toIntOrNull() ?: 6

                Log.d("ActionHistoryViewModel", "Fetching data with: deviceId=\$deviceFilter, page=\${currentState.page}, pageSize=\$limitInt, action=\$actionFilter, status=\$statusFilter, sortOrder=\${currentState.sortOrder}, date=\$dateFilter")

                val res = RetrofitClient.apiService.getActionHistory(
                    deviceId = deviceFilter,
                    page = currentState.page,
                    pageSize = limitInt,
                    limit = limitInt,
                    action = actionFilter,
                    status = statusFilter,
                    sortOrder = currentState.sortOrder,
                    date = dateFilter
                )

                if (res.isSuccessful && res.body() != null) {
                    val body = res.body()!!
                    _state.value = _state.value.copy(
                        data = body.data,
                        totalPages = body.totalPages,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = "Failed to load data: \${res.message()}",
                        isLoading = false
                    )
                    Log.e("ActionHistoryViewModel", "API Error: \${res.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
                Log.e("ActionHistoryViewModel", "Exception", e)
            }
        }
    }
}
