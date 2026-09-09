package com.example.phoneapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneapp.model.CallLogEntry
import com.example.phoneapp.model.CallType
import com.yourapp.phoneapp.ui.theme.HyperColors

@Composable
fun CallHistoryItem(
    entry: CallLogEntry,
    onItemClick: (CallLogEntry) -> Unit = {},
    onDetailClick: (CallLogEntry) -> Unit = {}
) {
    val isMissed = entry.callType == CallType.MISSED
    val nameColor = if (isMissed) HyperColors.MissedCallColor else HyperColors.TextPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(entry) }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isMissed && entry.missedCount > 1) {
                        "${entry.displayName} (${entry.missedCount})"
                    } else {
                        entry.displayName
                    },
                    fontSize = 17.sp,
                    fontWeight = if (isMissed) FontWeight.SemiBold else FontWeight.Normal,
                    color = nameColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                CallDirectionIndicator(callType = entry.callType)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = entry.date,
                fontSize = 14.sp,
                color = HyperColors.TextSecondary
            )
        }

        IconButton(
            onClick = { onDetailClick(entry) },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(HyperColors.Surface)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Filled.KeyboardArrowRight,
                contentDescription = "Chi tiết cuộc gọi",
                tint = HyperColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}