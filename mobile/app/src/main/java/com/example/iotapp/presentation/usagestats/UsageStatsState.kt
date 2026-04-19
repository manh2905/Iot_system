package com.example.iotapp.presentation.usagestats

import com.example.iotapp.data.remote.dto.DeviceActionCountDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UsageStatsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<DeviceActionCountDto> = emptyList(),
    val date: String = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
)
