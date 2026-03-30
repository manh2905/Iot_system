package com.example.iotapp

import android.app.Application
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.iotapp.core.navigation.NavGraph
import com.example.iotapp.presentation.navigation.BottomNavBar

@Composable
fun App () {
    val navController = rememberNavController()

    Scaffold (
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->
        NavGraph(navController)
        padding.toString()
    }
}
