package com.example.iotapp.presentation.usagestats

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iotapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageStatsScreen() {
    val viewModel: UsageStatsViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF102216))
    ) {
        Scaffold(
            topBar = {
                // Header with Vertical Gradient like DataSensorScreen
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
                        androidx.compose.material3.Icon(
                            painter = painterResource(id = R.drawable.ic_data_unselect),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Usage Stats",
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
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Date Filter Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = state.date,
                        onValueChange = viewModel::onDateChange,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        placeholder = { Text("YYYY/MM/DD", color = Color.White.copy(alpha = 0.5f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1A2F21),
                            unfocusedContainerColor = Color(0xFF1A2F21),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(15.27.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { viewModel.search() },
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(15.27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0DBA48))
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF0DBA48))
                    }
                } else if (state.error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.error!!, color = Color.Red, fontSize = 16.sp)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (state.data.isEmpty()) {
                            Text(
                                "No data available",
                                color = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            // AndroidView handling the MPAndroidChart BarChart
                            AndroidView(
                                factory = { context ->
                                    BarChart(context).apply {
                                        setNoDataText("Loading chart...")
                                        setNoDataTextColor(AndroidColor.WHITE)
                                        description.isEnabled = false
                                        legend.textColor = AndroidColor.WHITE
                                        legend.textSize = 12f
                                        
                                        // XAxis config
                                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                                        xAxis.textColor = AndroidColor.WHITE
                                        xAxis.setDrawGridLines(false)
                                        xAxis.granularity = 1f
                                        xAxis.isGranularityEnabled = true
                                        xAxis.setCenterAxisLabels(true) // Required for grouped bars

                                        // Left Axis
                                        axisLeft.textColor = AndroidColor.WHITE
                                        axisLeft.setDrawGridLines(true)
                                        axisLeft.gridColor = AndroidColor.parseColor("#33FFFFFF")
                                        axisLeft.axisMinimum = 0f
                                        
                                        // Disable right axis
                                        axisRight.isEnabled = false
                                        
                                        // Extra offset to prevent label clipping
                                        setExtraOffsets(0f, 0f, 0f, 10f)
                                        
                                        // Value formatter for integers
                                        val intFormatter = object : ValueFormatter() {
                                            override fun getFormattedValue(value: Float): String {
                                                return value.toInt().toString()
                                            }
                                        }
                                        axisLeft.valueFormatter = intFormatter
                                    }
                                },
                                update = { chart ->
                                    val onEntries = ArrayList<BarEntry>()
                                    val offEntries = ArrayList<BarEntry>()
                                    val labels = ArrayList<String>()

                                    state.data.forEachIndexed { index, dto ->
                                        onEntries.add(BarEntry(index.toFloat(), dto.ON.toFloat()))
                                        offEntries.add(BarEntry(index.toFloat(), dto.OFF.toFloat()))
                                        labels.add(dto.name)
                                    }

                                    val onDataSet = BarDataSet(onEntries, "ON").apply {
                                        color = AndroidColor.parseColor("#0DBA48")
                                        valueTextColor = AndroidColor.WHITE
                                        valueTextSize = 10f
                                        valueFormatter = object : ValueFormatter() {
                                            override fun getFormattedValue(value: Float): String {
                                                return if(value > 0) value.toInt().toString() else ""
                                            }
                                        }
                                    }

                                    val offDataSet = BarDataSet(offEntries, "OFF").apply {
                                        color = AndroidColor.parseColor("#E63946") // A distinct color for OFF
                                        valueTextColor = AndroidColor.WHITE
                                        valueTextSize = 10f
                                        valueFormatter = object : ValueFormatter() {
                                            override fun getFormattedValue(value: Float): String {
                                                return if(value > 0) value.toInt().toString() else ""
                                            }
                                        }
                                    }

                                    val data = BarData(onDataSet, offDataSet)
                                    // Grouping configuration
                                    val groupSpace = 0.2f
                                    val barSpace = 0.05f
                                    val barWidth = 0.35f
                                    
                                    data.barWidth = barWidth
                                    chart.data = data
                                    
                                    chart.groupBars(0f, groupSpace, barSpace)
                                    
                                    chart.xAxis.axisMinimum = 0f
                                    chart.xAxis.axisMaximum = 0f + chart.barData.getGroupWidth(groupSpace, barSpace) * labels.size
                                    
                                    // Make sure X-Axis shows proper device names, centering labels on grouped bars
                                    chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                                    chart.invalidate()
                                    chart.animateY(1000)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp) // Chart height constraint
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp)) // Nav Bar pad
            }
        }
    }
}
