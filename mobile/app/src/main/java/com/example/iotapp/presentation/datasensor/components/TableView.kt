package com.example.iotapp.presentation.datasensor.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iotapp.R
import com.example.iotapp.data.remote.dto.SensorItemDto

@Composable
fun TableView(
    data: List<SensorItemDto>,
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
            TableHeaderText("ID", Modifier.weight(1f))
            TableHeaderText("Sensor", Modifier.weight(2f))
            TableHeaderText("Value", Modifier.weight(1.5f))
            TableHeaderText("Time", Modifier.weight(2f))
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
                TableRow(item)
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
fun TableHeaderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.White.copy(alpha = 0.4f),
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

@Composable
fun TableRow(data: SensorItemDto) {
    
    val iconRes = when (data.idSensor) {
        3 -> R.drawable.ic_light
        2 -> R.drawable.ic_humidity
        1 -> R.drawable.ic_temp
        else -> R.drawable.ic_dashboard // fallback
    }
    
    val color = when (data.idSensor) {
        3 -> Color(0xFFFACC15)
        2 -> Color(0xFF3B82F6)
        1 -> Color(0xFFEF4444)
        else -> Color.White
    }

    val displaySensorName = when (data.idSensor) {
        3 -> "Ánh sáng"
        2 -> "Độ ẩm"
        1 -> "Nhiệt độ"
        else -> "Sensor ${data.idSensor}"
    }

    val unit = when (data.idSensor) {
        3 -> " lx"
        2 -> "%"
        1 -> "°C"
        else -> ""
    }

    val value = when (data.idSensor) {
        1 -> data.value
        2 -> data.value.toInt()
        3 -> data.value.toInt()
        else -> data.value
    }

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
            text = data.displayId,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        // Sensor
        Row(
            modifier = Modifier.weight(2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = displaySensorName,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Value
        Text(
            text = "$value$unit",
            color = color,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f)
        )

        // Time
        Text(
            text = displayDate,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier.weight(2f),
            lineHeight = 16.sp,
            maxLines = 2
        )
    }
}
