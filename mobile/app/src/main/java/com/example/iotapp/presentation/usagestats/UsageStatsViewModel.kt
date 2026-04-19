package com.example.iotapp.presentation.usagestats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapp.data.remote.api.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsageStatsViewModel : ViewModel() {
    private val _state = MutableStateFlow(UsageStatsState())
    val state: StateFlow<UsageStatsState> = _state

    private var fetchJob: Job? = null

    init {
        fetchData()
    }

    fun onDateChange(date: String) {
        _state.value = _state.value.copy(date = date)
    }

    fun search() {
        fetchData()
    }

    private fun fetchData() {
        val currentDate = _state.value.date
        if (currentDate.isBlank()) return

        fetchJob?.cancel()
        _state.value = _state.value.copy(isLoading = true, error = null)

        val apiDate = currentDate.replace("/", "-")

        fetchJob = viewModelScope.launch {
            try {
                Log.d("UsageStatsViewModel", "Fetching data with: date=$apiDate")

                val res = RetrofitClient.apiService.getActionCountByDay(date = apiDate)

                if (res.isSuccessful) {
                    val body = res.body()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        data = body?.data ?: emptyList()
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
