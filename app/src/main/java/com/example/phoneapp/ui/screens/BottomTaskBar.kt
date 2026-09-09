package com.example.phoneapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourapp.phoneapp.ui.theme.HyperColors
import com.example.phoneapp.R

@Composable
fun DialerBottomTaskBar(
    selectedTab: DialerTab,
    onTabSelected: (DialerTab) -> Unit
) {
    Surface(
        color = HyperColors.Surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TaskBarItem(
                filledIconRes = R.drawable.ic_call_filled,
                outlinedIconRes = R.drawable.ic_call_outlined,
                label = "Gần đây",
                isSelected = selectedTab == DialerTab.RECENTS,
                onClick = { onTabSelected(DialerTab.RECENTS) }
            )
            TaskBarItem(
                filledIconRes = R.drawable.ic_contacts_filled,
                outlinedIconRes = R.drawable.ic_contacts_outlined,
                label = "Danh bạ",
                isSelected = selectedTab == DialerTab.CONTACTS,
                onClick = { onTabSelected(DialerTab.CONTACTS) }
            )
        }
    }
}

@Composable
fun TaskBarItem(
    filledIconRes: Int,
    outlinedIconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) HyperColors.AccentBlue else HyperColors.TextSecondary
    val iconRes = if (isSelected) filledIconRes else outlinedIconRes

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}