package com.example.phoneapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneapp.R
import com.example.phoneapp.data.CallLogRepository
import com.example.phoneapp.model.CallLogEntry
import com.example.phoneapp.model.CallType
import com.example.phoneapp.ui.screens.components.CallDirectionIndicator
import com.example.phoneapp.util.PhoneCallHelper
import com.yourapp.phoneapp.ui.theme.HyperColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CallDetailScreen(entry: CallLogEntry, onBack: () -> Unit) {
    val context = LocalContext.current

    // Lấy toàn bộ lịch sử cuộc gọi với số này (không gộp), hiển thị tạm entry hiện tại trong lúc chờ.
    val history by produceState(
        initialValue = listOf(entry),
        key1 = entry.phoneNumber
    ) {
        value = withContext(Dispatchers.IO) {
            CallLogRepository.getCallHistoryForNumber(context, entry.phoneNumber)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperColors.Background)
            .statusBarsPadding()
    ) {

        // ----- Nút quay lại -----
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Quay lại",
            tint = HyperColors.TextPrimary,
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .clickable { onBack() }
        )

        // ----- Avatar + tên + số -----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(HyperColors.Surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = HyperColors.TextSecondary,
                    modifier = Modifier.size(42.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = entry.displayName,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = HyperColors.TextPrimary
            )
            if (entry.displayName != entry.phoneNumber) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.phoneNumber,
                    fontSize = 15.sp,
                    color = HyperColors.TextSecondary
                )
            }
        }

        // ----- Nút gọi lại -----
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .clickable { PhoneCallHelper.placeCall(context, entry.phoneNumber) }
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(HyperColors.Surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call_filled),
                    contentDescription = "Gọi",
                    tint = HyperColors.AccentGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Gọi lại", fontSize = 12.sp, color = HyperColors.TextSecondary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----- Lịch sử cuộc gọi với số này -----
        Text(
            text = "Lịch sử cuộc gọi",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = HyperColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(history, key = { it.id }) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CallDirectionIndicator(callType = row.callType)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = row.date,
                        fontSize = 15.sp,
                        color = if (row.callType == CallType.MISSED) {
                            HyperColors.MissedCallColor
                        } else {
                            HyperColors.TextPrimary
                        }
                    )
                }
            }
        }
    }
}