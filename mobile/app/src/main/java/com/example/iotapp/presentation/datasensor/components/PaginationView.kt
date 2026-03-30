package com.example.iotapp.presentation.datasensor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaginationView(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    if (totalPages <= 1) return // Hide pagination if only 1 or 0 pages

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Arrow
        ArrowButton("◀", enabled = currentPage > 1) {
            onPageChange(currentPage - 1)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Page Numbers Logic
        val pagesToShow = mutableListOf<Int>()
        if (totalPages <= 5) {
            pagesToShow.addAll(1..totalPages)
        } else {
            if (currentPage <= 3) {
                pagesToShow.addAll(1..4)
                pagesToShow.add(-1) // -1 represents "..."
                pagesToShow.add(totalPages)
            } else if (currentPage >= totalPages - 2) {
                pagesToShow.add(1)
                pagesToShow.add(-1)
                pagesToShow.addAll((totalPages - 3)..totalPages)
            } else {
                pagesToShow.add(1)
                pagesToShow.add(-1)
                pagesToShow.add(currentPage - 1)
                pagesToShow.add(currentPage)
                pagesToShow.add(currentPage + 1)
                pagesToShow.add(-1)
                pagesToShow.add(totalPages)
            }
        }

        pagesToShow.forEach { pageNum ->
            if (pageNum == -1) {
                Text("...", color = Color.White.copy(alpha = 0.4f), modifier = Modifier.padding(horizontal = 4.dp))
            } else {
                PageButton(
                    text = pageNum.toString(),
                    active = (pageNum == currentPage),
                    onClick = { onPageChange(pageNum) }
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right Arrow
        ArrowButton("▶", enabled = currentPage < totalPages) {
            onPageChange(currentPage + 1)
        }
    }
}

@Composable
fun PageButton(text: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(36.dp)
            .background(
                if (active) Color(0xFF0DBA48) else Color(0xFF152A1B),
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (active) Color.Black else Color.White,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ArrowButton(arrow: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                if (enabled) Color(0xFF152A1B) else Color(0xFF0C1D12), 
                RoundedCornerShape(8.dp)
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = arrow, 
            color = if (enabled) Color.White.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.2f), 
            fontSize = 12.sp
        )
    }
}
