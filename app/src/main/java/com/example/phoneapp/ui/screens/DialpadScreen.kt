package com.example.phoneapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.example.phoneapp.R
import com.example.phoneapp.util.PhoneCallHelper
import com.yourapp.phoneapp.ui.theme.HyperColors

// Bố cục 4 hàng phím: số + chữ cái phụ bên dưới (giống bàn phím điện thoại thật)
private val dialpadRows = listOf(
    listOf("1" to "", "2" to "ABC", "3" to "DEF"),
    listOf("4" to "GHI", "5" to "JKL", "6" to "MNO"),
    listOf("7" to "PQRS", "8" to "TUV", "9" to "WXYZ"),
    listOf("*" to ",", "0" to "+", "#" to "")
)

@Composable
fun DialpadScreen(onClose: () -> Unit) {
    var enteredNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {}
            .background(
                color = HyperColors.Surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        // ----- Tay cầm nhỏ trên cùng — chạm để đóng bàn phím -----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClose() }
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(HyperColors.TextSecondary)
            )
        }

        // ----- Số đang nhập -----
        Text(
            text = enteredNumber,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = HyperColors.TextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // ----- Lưới bàn phím số -----
        dialpadRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { (digit, letters) ->
                    DialButton(digit = digit, letters = letters) {
                        enteredNumber += digit
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ----- Hàng nút dưới cùng: menu / gọi / xoá -----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier.padding(5.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "trở lại",
                    tint = HyperColors.TextSecondary,
                    modifier = Modifier
                        .size(28.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(HyperColors.AccentGreen)
                    .clickable { PhoneCallHelper.placeCall(context, enteredNumber) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call_filled),
                    contentDescription = "Gọi",
                    tint = HyperColors.Background,
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(
                onClick = {
                    if (enteredNumber.isNotEmpty()) {
                        enteredNumber = enteredNumber.dropLast(1)
                    }
                },
                modifier = Modifier.size(48.dp) // Standard round touch target
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backspace),
                    contentDescription = "Xoá",
                    tint = HyperColors.TextSecondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun DialButton(digit: String, letters: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = digit,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            color = HyperColors.TextPrimary
        )
        if (letters.isNotEmpty()) {
            Text(
                text = letters,
                fontSize = 10.sp,
                color = HyperColors.TextSecondary
            )
        }
    }
}