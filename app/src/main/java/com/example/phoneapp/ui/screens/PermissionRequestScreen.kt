package com.example.phoneapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourapp.phoneapp.ui.theme.HyperColors

@Composable
fun PermissionRequestScreen(onRequestClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperColors.Background)
            .statusBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Cần quyền truy cập",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = HyperColors.TextPrimary,
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.height(12.dp))
            Text(
                text = "Ứng dụng cần quyền truy cập lịch sử cuộc gọi và danh bạ " +
                        "để hiển thị dữ liệu thật trên máy của bạn.",
                fontSize = 15.sp,
                color = HyperColors.TextSecondary,
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRequestClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HyperColors.AccentGreen,
                    contentColor = HyperColors.Background
                )
            ) {
                Text(text = "Cấp quyền")
            }
        }
    }
}