package com.example.iotapp.presentation.actionhistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iotapp.R
import com.example.iotapp.presentation.actionhistory.components.ActionFilterSection
import com.example.iotapp.presentation.actionhistory.components.ActionTableView
import com.example.iotapp.presentation.datasensor.components.PaginationView


@Composable
fun ActionHistoryScreen() {
    val viewModel: ActionHistoryViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val scrollState = rememberScrollState()

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
                        androidx.compose.material3.Icon(
                            painter = painterResource(id = R.drawable.ic_history_unselect),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Action History",
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
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Filter Section
                ActionFilterSection(
                    date = state.date,
                    deviceId = state.deviceId,
                    action = state.action,
                    status = state.status,
                    pageSizeStr = state.pageSizeStr,
                    sortOrder = state.sortOrder,
                    onDateChange = viewModel::onDateChange,
                    onDeviceChange = viewModel::onDeviceChange,
                    onActionChange = viewModel::onActionChange,
                    onStatusChange = viewModel::onStatusChange,
                    onRowsChange = viewModel::onRowsChange,
                    onSortChange = viewModel::onSortChange
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Table Section
                ActionTableView(
                    data = state.data,
                    isLoading = state.isLoading
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Pagination Section
                PaginationView(
                    currentPage = state.page,
                    totalPages = state.totalPages,
                    onPageChange = viewModel::onPageChange
                )

                Spacer(modifier = Modifier.height(120.dp)) // Bottom Nav Padding
            }
        }
    }
}