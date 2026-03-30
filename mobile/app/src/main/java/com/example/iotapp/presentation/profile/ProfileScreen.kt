package com.example.iotapp.presentation.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iotapp.R
import com.example.iotapp.core.ui.theme.AccentGreen

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Handlers for intents
    val openUrl = { url: String ->
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF102216))
    ) {
        Scaffold(
            topBar = {
                // Header with Vertical Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color(0xFF03722A),
                                0.59f to Color(0xFF0B411E),
                                1.0f to Color(0xFF102216)
                            )
                        )
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx() 
                            val y = size.height - strokeWidth / 2 
                            drawLine(
                                color = Color(0xFF1F2F28), 
                                start = Offset(0f, y), 
                                end = Offset(size.width, y), 
                                strokeWidth = strokeWidth
                            )
                        }
                        .statusBarsPadding()
                        .padding(vertical = 16.dp, horizontal = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile_unselect), 
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Profile",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Avatar
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .border(3.dp, AccentGreen, CircleShape)
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF152A1B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Name
                Text(
                    text = "Vũ Tiến Mạnh",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Details
                val infoColor = Color.White.copy(alpha = 0.6f)
                val infoFontSize = 14.sp
                
                Text("Student ID: B22DCPT163", color = infoColor, fontSize = infoFontSize, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(0.dp))
                Text("Phone: 0912440842", color = infoColor, fontSize = infoFontSize, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(0.dp))
                Text("Email: manhvu337@gmail.com", color = infoColor, fontSize = infoFontSize, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(0.dp))
                Text("Address: Vĩnh Phúc - Việt Nam", color = infoColor, fontSize = infoFontSize, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(20.dp))

                // Section Title
                Text(
                    text = "PROJECT RESOURCES",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Resource Cards
                ResourceCard(
                    title = "Open PDF Report",
                    subtitle = "Project documentation and analysis",
                    icon = R.drawable.ic_pdf,
                    iconTint = AccentGreen,
                    onClick = { openUrl("https://drive.google.com/file/d/1DGHkxMXjDcTmpCSsWu1xNwXrV7ys6CYy/view?usp=sharing") } // Vui lòng thay thế bằng link thực tế
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                ResourceCard(
                    title = "API Documentation",
                    subtitle = "Backend endpoint specifications",
                    icon = R.drawable.ic_docs,
                    iconTint = AccentGreen,
                    onClick = { openUrl("http://172.20.10.4:3000/api-docs/#/") } // Vui lòng thay thế bằng host backend thực tế
                )

                Spacer(modifier = Modifier.height(12.dp))

                ResourceCard(
                    title = "GitHub",
                    subtitle = "Source code and repository",
                    icon = R.drawable.ic_git,
                    iconTint = AccentGreen,
                    onClick = { openUrl("https://github.com/manhvu337") } // Vui lòng thay thế 
                )

                Spacer(modifier = Modifier.height(12.dp))

                ResourceCard(
                    title = "Figma",
                    subtitle = "UI/UX Prototypes and assets",
                    icon = R.drawable.ic_figma,
                    iconTint = AccentGreen,
                    onClick = { openUrl("https://figma.com/") } // Vui lòng thay thế
                )

                Spacer(modifier = Modifier.height(120.dp)) // Bottom Nav Padding
            }
        }
    }
}

@Composable
fun ResourceCard(
    title: String,
    subtitle: String,
    icon: Int,
    iconTint: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF152A1B), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF0C1D12), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }

            // Arrow
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "Open",
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}