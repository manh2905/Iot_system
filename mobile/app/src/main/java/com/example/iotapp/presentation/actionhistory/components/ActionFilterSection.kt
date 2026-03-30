package com.example.iotapp.presentation.actionhistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.example.iotapp.presentation.datasensor.components.FilterDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionFilterSection(
    date: String,
    deviceId: String,
    action: String,
    status: String,
    pageSizeStr: String,
    sortOrder: String,
    onDateChange: (String) -> Unit,
    onDeviceChange: (String) -> Unit,
    onActionChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onRowsChange: (String) -> Unit,
    onSortChange: (String) -> Unit
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
            ActionSortDialog(
                currentSortOrder = sortOrder,
                onDismiss = { showSortDialog = false },
                onApply = { newSortOrder ->
                    onSortChange(newSortOrder)
                    showSortDialog = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Row 2: Device, Action, Status, Rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text("Device", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.padding(end=4.dp))
                Box(modifier = Modifier.weight(1f)) {
                    ActionFilterDropdown(
                        selectedText = deviceId,
                        options = listOf("All", "Light", "Fan", "AC"),
                        onSelect = onDeviceChange
                    )
                }
            }

            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text("Action", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.padding(end=4.dp))
                Box(modifier = Modifier.weight(1f)) {
                    ActionFilterDropdown(
                        selectedText = action,
                        options = listOf("All", "ON", "OFF"),
                        onSelect = onActionChange
                    )
                }
            }

            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text("Status", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.padding(end=4.dp))
                Box(modifier = Modifier.weight(1f)) {
                    ActionFilterDropdown(
                        selectedText = status,
                        options = listOf("All", "ON", "OFF", "WAITING", "FAIL"),
                        onSelect = onStatusChange
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rows", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, modifier = Modifier.padding(end=4.dp))
                TextField(
                    value = pageSizeStr,
                    onValueChange = onRowsChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(0.dp)
                        .width(45.dp),
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
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, textAlign = TextAlign.Center)
                )
            }
        }
    }
}

// A slightly smaller dropdown for Action History since there are 3 drop downs in one row
@Composable
fun ActionFilterDropdown(
    selectedText: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color(0xFF1A2F21), RoundedCornerShape(15.27.dp))
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedText, color = Color.White, fontSize = 12.sp, maxLines = 1)
                Text("▼", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF152A1B))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White, fontSize = 12.sp) },
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
fun ActionSortDialog(
    currentSortOrder: String,
    onDismiss: () -> Unit,
    onApply: (String) -> Unit
) {
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
                Text("Sort By Time", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                val orderOptions = listOf("DESC" to "Newest First", "ASC" to "Oldest First")
                orderOptions.forEach { (value, label) ->
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
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF0DBA48),
                                unselectedColor = Color.White.copy(alpha = 0.5f)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(label, color = Color.White, fontSize = 16.sp)
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
                        onClick = { onApply(selectedSortOrder) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00571E))
                    ) {
                        Text("Apply", color = Color.White)
                    }
                }
            }
        }
    }
}
