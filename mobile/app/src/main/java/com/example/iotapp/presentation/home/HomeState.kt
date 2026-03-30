package com.example.iotapp.presentation.home

import androidx.navigationevent.NavigationEventHistory

data class DeviceState(
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

    val fanState: DeviceState = DeviceState(),
    val lightState: DeviceState = DeviceState(),
    val acState: DeviceState = DeviceState()
)