package com.example.iotapp.presentation.home

import androidx.navigationevent.NavigationEventHistory

data class DeviceUiState(
    val id: Int,
    val name: String,
    val isChecked: Boolean = false,
    val isLoading: Boolean = false
)

data class HomeState(
    val temperature: Double = 0.0,
    val humidity: Double = 0.0,
    val light: Double = 0.0,

    val tempHistory : List<Pair<Long, Double>> = emptyList(),
    val humHistory: List<Pair<Long, Double>> = emptyList(),
    val lightHistory: List<Pair<Long, Double>> = emptyList(),

    val devices: List<DeviceUiState> = emptyList(),
)