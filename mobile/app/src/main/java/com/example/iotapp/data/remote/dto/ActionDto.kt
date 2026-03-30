package com.example.iotapp.data.remote.dto

data class PaginatedActionResponse(
    val success: Boolean,
    val totalRecords: Int,
    val totalPages: Int,
    val currentPage: Int,
    val data: List<ActionItemDto>
)

data class ActionItemDto(
    val id: Int,
    val action: String,
    val status: String,
    val idDevice: Int,
    val createdAt: String
)
