package com.example.iotapp.data.remote.dto

data class DeviceLatestStatusResponse(
    val success: Boolean,
    val data: List<DeviceLatestStatusDto>
)

data class DeviceLatestStatusDto(
    val deviceId: Int,
    val action: String,
    val status: String,
    val lastUpdate: String
)
