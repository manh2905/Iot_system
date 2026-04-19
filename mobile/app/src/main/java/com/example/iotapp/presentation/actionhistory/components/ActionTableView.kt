package com.example.iotapp.presentation.actionhistory.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iotapp.R
import com.example.iotapp.data.remote.dto.ActionItemDto
import com.example.iotapp.presentation.datasensor.components.TableHeaderText

@Composable
fun ActionTableView(
    data: List<ActionItemDto>,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0C1D12), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Table Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableHeaderText("ID", Modifier.weight(0.8f))
            TableHeaderText("Device", Modifier.weight(1.5f))
            TableHeaderText("Action", Modifier.weight(1f))
            TableHeaderText("Status", Modifier.weight(1.5f))
            TableHeaderText("Time", Modifier.weight(1.5f))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = Color.White.copy(alpha = 0.1f))
        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0DBA48))
            }
        } else if (data.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No data available", color = Color.White.copy(alpha = 0.5f))
            }
        } else {
            // Data Rows
            data.forEachIndexed { index, item ->
                ActionTableRow(item)
                if (index < data.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color.White.copy(alpha = 0.05f))
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ActionTableRow(data: ActionItemDto) {
    // Lấy context để dùng cho Clipboard và Toast
    val context = LocalContext.current

    val iconRes = when (data.idDevice) {
        1 -> R.drawable.ic_light
        2 -> R.drawable.ic_fan
        3 -> R.drawable.ic_ac
        else -> R.drawable.ic_light // fallback
    }

    val deviceColor = when (data.idDevice) {
        1 -> Color(0xFF0DBA48) // Green for light
        2 -> Color(0xFF0DBA48) // Green
        3 -> Color(0xFF0DBA48) // Green
        else -> Color(0xFF0DBA48)
    }

    val displayDeviceName = when (data.idDevice) {
        1 -> "Light"
        2 -> "Fan"
        3 -> "AC"
        4 -> "Light 2"
        5 -> "Light 2"
        else -> "Device ${data.idDevice}"
    }

    // Action formatting
    val actionColor = if (data.action.equals("ON", ignoreCase = true)) Color(0xFF0DBA48) else Color(0xFFEF4444)
    val displayAction = data.action.uppercase()

    // Status formatting
    val statusUpper = data.status.uppercase()
    val statusColor = when (statusUpper) {
        "ON" -> Color(0xFF0DBA48)
        "OFF" -> Color(0xFFEF4444)
        "FAIL" -> Color(0xFFEF4444)
        "PROCESSING", "WAITING" -> Color(0xFFFACC15) // Yellow
        else -> Color.White
    }
    val displayStatus = if (statusUpper == "PROCESSING") "WAITING" else statusUpper

    // Format display ID
    val formattedId = String.format("#A%02d", data.id)

    // Format date string assuming "2026-03-24 23:28:22"
    val parts = data.createdAt.split(" ")
    val displayDate = if (parts.size == 2) {
        val dateParts = parts[0].split("-")
        if (dateParts.size == 3) {
            "${dateParts[2]}/${dateParts[1]}/${dateParts[0]}\n${parts[1]}"
        } else data.createdAt
    } else {
        data.createdAt
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ID
        Text(
            text = formattedId,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            modifier = Modifier.weight(0.8f)
        )

        // Device
        Row(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = deviceColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = displayDeviceName,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Action
        Text(
            text = displayAction,
            color = actionColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        // Status
        Text(
            text = displayStatus,
            color = statusColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f)
        )

        // Time
        Text(
            text = displayDate,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1.5f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            val formattedTimeForCopy = data.createdAt.replace("-", "/")

                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Copied Action Time", formattedTimeForCopy)
                            clipboard.setPrimaryClip(clip)

                            // Hiển thị Toast
                            Toast.makeText(context, "Đã sao chép: $formattedTimeForCopy", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
            lineHeight = 16.sp,
            maxLines = 2
        )
    }
}