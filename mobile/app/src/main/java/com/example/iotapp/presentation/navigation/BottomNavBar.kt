package com.example.iotapp.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.iotapp.core.navigation.Screen
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.iotapp.core.ui.theme.AccentGreen
import com.example.iotapp.core.ui.theme.DeepGreen

@Composable
fun BottomNavBar (navController: NavHostController) {

    val items = listOf(
        Screen.Home,
        Screen.DataSensor,
        Screen.ActionHistory,
        Screen.Profile
    )

    NavigationBar (
        modifier = Modifier
            .height(95.dp)
            .drawBehind {
                drawLine(
                    color = AccentGreen.copy(alpha = 0.7f), // Increased visibility
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 5.dp.toPx()
                )
            },
        containerColor = DeepGreen
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                label = { Text(screen.title) },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (isSelected)
                                screen.iconSelected
                            else
                                screen.iconUnselected
                        ),
                        contentDescription = screen.title,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(top = 9.dp)
                            .size(26.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentGreen,
                    selectedTextColor = AccentGreen,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color.Transparent
                )
            )
        }

    }
}
