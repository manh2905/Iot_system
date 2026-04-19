package com.example.iotapp.core.navigation

import com.example.iotapp.R

sealed class Screen(
    val route: String,
    val title: String,
    val iconSelected: Int,
    val iconUnselected: Int
) {

    object Home : Screen(
        "home",
        "Home",
        R.drawable.ic_home_select,
        R.drawable.ic_home_unselect
    )

    object DataSensor : Screen(
        "datasensor",
        "Data",
        R.drawable.ic_data_select,
        R.drawable.ic_data_unselect
    )

    object ActionHistory : Screen(
        "actionhistory",
        "History",
        R.drawable.ic_history_select,
        R.drawable.ic_history_unselect
    )

    object Profile : Screen(
        "profile",
        "Profile",
        R.drawable.ic_profile_select,
        R.drawable.ic_profile_unselect
    )

    object UsageStats : Screen(
        "usagestats",
        "Statistics",
        R.drawable.ic_data_select,
        R.drawable.ic_data_unselect
    )
}