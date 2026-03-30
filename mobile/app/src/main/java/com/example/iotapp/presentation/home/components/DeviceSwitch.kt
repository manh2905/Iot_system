package com.example.iotapp.presentation.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeviceSwitch(
    name: String,
    icon: Int,
    isChecked: Boolean,
    isLoading: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val backgroundColor = if (isChecked) Color(0xFF0DBA48) else Color(0xFF1A1F2B)
    

    val infiniteTransition = rememberInfiniteTransition(label = "iconRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )


    val animatedOffsetY = remember { Animatable(0f) }
    LaunchedEffect(isChecked) {
        if (isChecked && name.contains("Light", ignoreCase = true)) {
            // Jump up quickly
            animatedOffsetY.animateTo(-15f, animationSpec = tween(150, easing = FastOutSlowInEasing))
            // Return to original position with a bounce
            animatedOffsetY.animateTo(0f, animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow))
        } else {
            animatedOffsetY.snapTo(0f)
        }
    }

    // Active color based on device name
    val activeIconColor = when {
        name.contains("Light", ignoreCase = true) -> Color(0xFFFFEB3B)
        name.contains("AC", ignoreCase = true) -> Color(0xFF387AE2)
        name.contains("Fan", ignoreCase = true) -> Color.White
        else -> Color.White
    }

    val contentColor = if (isChecked) activeIconColor else Color.White.copy(alpha = 0.4f)
    val subTextColor = if (isChecked) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.2f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .then(
                if (isChecked) {
                    Modifier.dropShadow(
                        shape = RoundedCornerShape(20.dp),
                        shadow = Shadow(
                            radius = 20.dp,
                            spread = 5.dp,
                            color = Color(0xFF0DBA48),
                            offset = DpOffset(x = 0.dp, y = 0.dp)
                        )
                    )
                } else Modifier
            )
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!isLoading) {
                    onToggle(!isChecked)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isChecked) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                tint = contentColor,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer {
                        // Continuous rotation for AC and Fan when ON
                        if (isChecked && (name.contains("AC", ignoreCase = true) || name.contains("Fan", ignoreCase = true))) {
                            rotationZ = rotation
                        }
                        // Bouncing offset for Light when ON
                        if (name.contains("Light", ignoreCase = true)) {
                            translationY = animatedOffsetY.value * density
                        }
                    }
            )
        }

        // Text Labels
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = name,
                color = contentColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isChecked) "ON" else "OFF",
                color = subTextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        }

        // Show loading overlay spanning the actual box
        if (isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}
