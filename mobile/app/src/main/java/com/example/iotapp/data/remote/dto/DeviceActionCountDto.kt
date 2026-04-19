package com.example.iotapp.data.remote.dto

data class DeviceActionCountDto(
    val deviceId: Int,
    val name: String,
    val ON: Int,
    val OFF: Int
)

data class DeviceActionCountResponse(
    val success: Boolean,
    val data: List<DeviceActionCountDto>
)
