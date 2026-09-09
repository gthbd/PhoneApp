package com.example.phoneapp.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourapp.phoneapp.ui.theme.HyperColors

private val alphabetLetters = ('A'..'Z').map { it.toString() } + "#"

/**
 * Thanh chỉ mục chữ cái bên phải màn hình Danh bạ.
 * Hiện chỉ hiển thị UI — logic scroll-to-letter khi vuốt/chạm sẽ làm ở bước xử lý dữ liệu thật.
 */
@Composable
fun AlphabetIndexBar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(HyperColors.Surface.copy(alpha = 0.4f))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Danh bạ yêu thích",
            tint = HyperColors.MissedCallColor,
            modifier = Modifier
                .size(12.dp)
                .padding(bottom = 4.dp)
        )
        alphabetLetters.forEach { letter ->
            Text(
                text = letter,
                fontSize = 10.sp,
                color = HyperColors.TextMuted,
                modifier = Modifier.padding(vertical = 1.dp)
            )
        }
    }
}