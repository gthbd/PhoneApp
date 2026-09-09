package com.example.phoneapp.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourapp.phoneapp.ui.theme.HyperColors

@Composable
fun AlphabetSectionHeader(letter: String) {
    Text(
        text = letter,
        color = HyperColors.TextSecondary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}