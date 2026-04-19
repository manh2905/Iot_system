package com.example.iotapp.data.remote.dto

data class DeviceListResponse(
    val success: Boolean,
    val data: List<DeviceItem>
)

data class DeviceItem(
    val id: Int,
    val name: String,
    val createdAt: String
)
