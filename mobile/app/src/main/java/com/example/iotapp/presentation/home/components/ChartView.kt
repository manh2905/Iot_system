package com.example.iotapp.presentation.home.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChartView(
    data: List<Pair<Long, Double>>,
    label: String,
    yMax: Float,
    lineColor: Color = Color(0xFF4AEA85),
    fillColor: Color = Color(0xFF4AEA85)
) {
    Column(
        modifier = Modifier
            .border(width = 1.dp, color = lineColor, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0C1D12))
            .padding(16.dp)

    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(20.dp))

        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    // Chart styling
                    description.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(true)
                    isDragEnabled = true
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDrawGridBackground(false)
                    setBackgroundColor(AndroidColor.TRANSPARENT)
                    setViewPortOffsets(40f, 10f, 40f, 30f)

                    // X Axis
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(true)
                        setDrawAxisLine(false)
                        textColor = AndroidColor.argb(100, 255, 255, 255)
                        textSize = 13f
                        granularity = 1f
                    }

                    // Left Y Axis
                    axisLeft.apply {
                        setDrawGridLines(true)
                        gridColor = AndroidColor.argb(30, 255, 255, 255)
                        setDrawAxisLine(false)
                        textColor = AndroidColor.argb(100, 255, 255, 255)
                        textSize = 13f
                        axisMinimum = 0f
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return if (value >= 1000f) {
                                    "${(value / 1000f).toInt()}k"
                                } else {
                                    value.toInt().toString()
                                }
                            }
                        }
                    }

                    // Right Y Axis
                    axisRight.isEnabled = false

                    // Animate
                    animateX(500)
                }
            },
            update = { chart ->
                chart.axisLeft.axisMaximum = yMax
                
                chart.xAxis.valueFormatter = object : ValueFormatter() {
                    private val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        if (data.isNotEmpty() && index >= 0 && index < data.size) {
                            return sdf.format(Date(data[index].first))
                        }
                        return ""
                    }
                }

                if (data.isNotEmpty()) {
                    val entries = data.mapIndexed { index, pair ->
                        Entry(index.toFloat(), pair.second.toFloat())
                    }

                    val dataSet = LineDataSet(entries, label).apply {
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        cubicIntensity = 0.2f
                        setDrawCircles(true)
                        circleRadius = 3f
                        setCircleColor(lineColor.toArgb())
                        circleHoleColor = lineColor.toArgb()
                        color = lineColor.toArgb()
                        lineWidth = 2.5f
                        setDrawValues(false)
                        setDrawFilled(true)
                        this.fillColor = fillColor.toArgb()
                        fillAlpha = 40
                    }

                    chart.data = LineData(dataSet)
                    chart.invalidate()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}
