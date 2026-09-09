package com.example.phoneapp.ui.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.phoneapp.R
import com.example.phoneapp.model.CallType
import com.yourapp.phoneapp.ui.theme.HyperColors

@Composable
fun CallDirectionIndicator(callType: CallType) {
    val rotationDegrees = when (callType) {
        CallType.OUTGOING -> 45f
        CallType.INCOMING, CallType.MISSED -> 225f
    }
    val tint = if (callType == CallType.MISSED) {
        HyperColors.MissedCallColor
    } else {
        HyperColors.TextSecondary
    }

    Icon(
        painter = painterResource(id = R.drawable.ic_arrow_upward),
        contentDescription = when (callType) {
            CallType.OUTGOING -> "Cuộc gọi đi"
            CallType.INCOMING -> "Cuộc gọi đến"
            CallType.MISSED -> "Cuộc gọi nhỡ"
        },
        tint = tint,
        modifier = Modifier
            .size(14.dp)
            .rotate(rotationDegrees)
    )
}