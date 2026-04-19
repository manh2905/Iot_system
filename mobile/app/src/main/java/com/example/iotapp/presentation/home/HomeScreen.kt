package com.example.iotapp.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import com.example.iotapp.R
import com.example.iotapp.core.utils.Constants
import com.example.iotapp.core.ui.theme.AccentGreen
import com.example.iotapp.core.ui.theme.DeepGreen
import com.example.iotapp.presentation.home.components.SensorCard
import com.example.iotapp.presentation.home.components.ChartView
import com.example.iotapp.presentation.home.components.DeviceSwitch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    0.0f to Color(0xFF00571E),
                    0.49f to Color(0xFF102216),
                    1.0f to Color(0xFF102216),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f) // Adjusted for a diagonal feel
                )
            )
    ) {
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar or Icon placeholder
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = DeepGreen.copy(alpha = 1f),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home_top),
                        contentDescription = "Profile",
                        tint = AccentGreen,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = "Smart Home",
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Welcome back, Mạnh", // Đã sửa lỗi chính tả chữ Welcome
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SensorCard(
                        title = "Temp",
                        value = state.temperature.toString() + "°C",
                        icon = R.drawable.ic_temp,
                        color = Color(0xFFA7191B),
                        intensity = (state.temperature / 50.0).toFloat() // 0-50°C
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    SensorCard(
                        title = "Humidity",
                        value = state.humidity.toInt().toString() + "%",
                        icon = R.drawable.ic_humidity,
                        color = Color(0xFF387AE2),
                        intensity = state.humidity.toFloat() / 100f // 0-100%
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    SensorCard(
                        title = "Light",
                        value = state.light.toInt().toString() + "Lm",
                        icon = R.drawable.ic_light,
                        color = Color(0xFFFDC124),
                        intensity = state.light.toFloat() / 2095f // 0-1000 Lm
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Charts Section
            Text(
                text = "Real-time Data",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            ChartView(data = state.tempHistory, label = "Temperature", yMax = 50f, fillColor = Color(0xFFA7191B), lineColor = Color(0xFFA7191B))
            Spacer(modifier = Modifier.height(20.dp))
            ChartView(data = state.humHistory, label = "Humidity", yMax = 100f, fillColor = Color(0xFF387AE2), lineColor = Color(0xFF387AE2))
            Spacer(modifier = Modifier.height(20.dp))
            ChartView(data = state.lightHistory, label = "Light", yMax = 4095f, fillColor = Color(0xFFFDC124), lineColor = Color(0xFFFDC124))

            Spacer(modifier = Modifier.height(30.dp))

            // Device Controls
            Text(
                text = "Quick Controls",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(30.dp))

            // --- THAY THẾ LAZYVERTICALGRID BẰNG COLUMN CHỨA ROWS ĐỂ TỰ ĐỘNG TÍNH CHIỀU CAO ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val chunkedDevices = state.devices.chunked(3)
                chunkedDevices.forEach { rowDevices ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (device in rowDevices) {
                            Box(modifier = Modifier.weight(1f)) {
                                val iconRes = when {
                                    device.name.contains("fan", ignoreCase = true) -> R.drawable.ic_fan
                                    device.name.contains("ac", ignoreCase = true) -> R.drawable.ic_ac
                                    else -> R.drawable.ic_light_led
                                }
                                DeviceSwitch(
                                    name = device.name,
                                    icon = iconRes,
                                    isChecked = device.isChecked,
                                    isLoading = device.isLoading,
                                    onToggle = { isChecked ->
                                        viewModel.toggleDevice(device.id, if (isChecked) "ON" else "OFF")
                                    }
                                )
                            }
                        }
                        // Box rỗng chiếm các phần trống để giữ đúng kích thước của lưới 3 cột
                        val emptySpots = 3 - rowDevices.size
                        for (i in 0 until emptySpots) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp)) // Padding for bottom bar
        }
    }
}