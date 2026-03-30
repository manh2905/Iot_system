package com.example.iotapp.presentation.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SensorCard(
    title: String,
    value: String,
    icon: Int,
    color: Color,
    intensity: Float = 0.5f // 0f = thấp nhất, 1f = cao nhất
) {
    // Animate the intensity for smooth transitions
    val animatedIntensity by animateFloatAsState(
        targetValue = intensity.coerceIn(0f, 1f),
        animationSpec = tween(600),
        label = "intensityAnim"
    )

    // Gradient fill position: intensity controls where the color starts
    // At 1.0 → color fills from the very top (full color)
    // At 0.0 → color only at the very bottom
    val fillStart = 1f - animatedIntensity // e.g. 0.3 intensity → color starts at 70% from top
    val darkColor = Color(0xFF1A2F21)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(13.92.dp))
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to darkColor,
                        fillStart.coerceIn(0f, 0.95f) to darkColor,
                        1f to color
                    )
                )
            )
            .border(
                width = (1f + animatedIntensity).dp,
                color = color,
                shape = RoundedCornerShape(13.92.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = color.copy(alpha = 0.4f + animatedIntensity * 0.6f),
                modifier = Modifier.size(25.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.6f + animatedIntensity * 0.4f),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
