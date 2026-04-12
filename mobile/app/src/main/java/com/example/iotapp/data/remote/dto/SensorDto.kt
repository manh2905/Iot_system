package com.example.iotapp.data.remote.dto

data class PaginatedSensorResponse(
    val success: Boolean,
    val totalRecords: Int,
    val totalPages: Int,
    val currentPage: Int,
    val data: List<SensorItemDto>
)

data class SensorItemDto(
    val id: Int,
    val value: Double,
    val idSensor: Int,
    val createdAt: String
) {
    val displayId: String
        get() = "#$id"
}

data class SensorListResponse(
    val success: Boolean,
    val data: List<SensorTypeDto>
)

data class SensorTypeDto(
    val id: Int,
    val name: String
)
