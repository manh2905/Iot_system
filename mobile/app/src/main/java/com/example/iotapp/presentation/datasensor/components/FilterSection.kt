package com.example.iotapp.presentation.datasensor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    date: String,
    sensorId: String,
    value: String,
    limitStr: String,
    sortBy: String,
    sortOrder: String,
    sensorOptions: List<String>,
    onDateChange: (String) -> Unit,
    onSensorChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onRowsChange: (String) -> Unit,
    onSortChange: (sortBy: String, sortOrder: String) -> Unit
) {
    var showSortDialog by remember { mutableStateOf(false) }

    Column {
        // Row 1: Date , Sort
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = date,
                onValueChange = onDateChange,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                placeholder = { Text("YYYY/MM/DD HH:mm:ss", color = Color.White.copy(alpha = 0.5f)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1A2F21),
                    unfocusedContainerColor = Color(0xFF1A2F21),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(15.27.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { showSortDialog = true },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(15.27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A2F21))
            ) {
                Text("Sort", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.List, contentDescription = null, tint = Color.White)
            }
        }

        if (showSortDialog) {
            SortDialog(
                currentSortBy = sortBy,
                currentSortOrder = sortOrder,
                onDismiss = { showSortDialog = false },
                onApply = { newSortBy, newSortOrder ->
                    onSortChange(newSortBy, newSortOrder)
                    showSortDialog = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Sensor, Value, Rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ĐÃ SỬA: Tăng weight lên 1.2f để lấy thêm không gian cho ô Sensor
            Row(modifier = Modifier.weight(1.2f), verticalAlignment = Alignment.CenterVertically) {
                Text("Sensor:", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.padding(end=8.dp))
                FilterDropdown(
                    selectedText = sensorId,
                    options = sensorOptions,
                    onSelect = onSensorChange
                )
            }

            // ĐÃ SỬA: Giảm weight xuống 0.8f vì ô Value giờ đã bé lại
            Row(modifier = Modifier.weight(0.8f), verticalAlignment = Alignment.CenterVertically) {
                Text("Value:", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.padding(end=8.dp))
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(0.dp)
                        .width(65.dp), // ĐÃ SỬA: Giảm từ 90.dp xuống 65.dp cho bé lại
                    placeholder = {
                        Text(
                            text = "---",
                            color = Color.White.copy(alpha = 0.3f) ,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
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
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rows:", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.padding(end=8.dp))
                TextField(
                    value = limitStr,
                    onValueChange = onRowsChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(0.dp)
                        .width(50.dp),
                    placeholder = { Text("6", color = Color.White.copy(alpha = 0.3f)) },
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
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )
            }
        }
    }
}

@Composable
fun FilterDropdown(
    selectedText: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(125.dp) // ĐÃ SỬA: Tăng từ 100.dp lên 125.dp cho dài ra
                .background(Color(0xFF1A2F21), RoundedCornerShape(15.27.dp))
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedText, color = Color.White, fontSize = 14.sp)
                Text("▼", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
            }
        }

        // Phần DropdownMenu giữ nguyên...
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF152A1B))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SortDialog(
    currentSortBy: String,
    currentSortOrder: String,
    onDismiss: () -> Unit,
    onApply: (String, String) -> Unit
) {
    var selectedSortBy by remember { mutableStateOf(currentSortBy) }
    var selectedSortOrder by remember { mutableStateOf(currentSortOrder) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1A2F21),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text("Sort Options", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Sort By Column
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sort By", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        val sortByOptions = listOf("value" to "Value", "createdAt" to "Time")
                        sortByOptions.forEach { (value, label) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSortBy = value }
                                    .padding(vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = selectedSortBy == value,
                                    onClick = { selectedSortBy = value },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0DBA48), unselectedColor = Color.White.copy(alpha=0.5f))
                                )
                                Text(label, color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }

                    // Sort Order Column
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Order", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        val sortOrderOptions = listOf("ASC" to "Ascending", "DESC" to "Descending")
                        sortOrderOptions.forEach { (value, label) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSortOrder = value }
                                    .padding(vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = selectedSortOrder == value,
                                    onClick = { selectedSortOrder = value },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0DBA48), unselectedColor = Color.White.copy(alpha=0.5f))
                                )
                                Text(label, color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onApply(selectedSortBy, selectedSortOrder) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0DBA48))
                    ) {
                        Text("Apply", color = Color.White)
                    }
                }
            }
        }
    }
}
