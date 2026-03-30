package com.example.iotapp.core.navigation

import com.example.iotapp.presentation.home.HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.iotapp.presentation.actionhistory.ActionHistoryScreen
import com.example.iotapp.presentation.datasensor.DataSensorScreen
import com.example.iotapp.presentation.profile.ProfileScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

@Composable
fun NavGraph (navController : NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            )
        }
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.DataSensor.route) { DataSensorScreen() }
        composable(Screen.ActionHistory.route) { ActionHistoryScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}
