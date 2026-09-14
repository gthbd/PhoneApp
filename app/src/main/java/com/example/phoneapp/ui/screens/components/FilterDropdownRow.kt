package com.example.phoneapp.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourapp.phoneapp.ui.theme.HyperColors
import com.example.phoneapp.R


/**
 * Dòng filter dạng dropdown dùng chung — VD: "Tất cả cuộc gọi", "Tất cả danh bạ".
 * Hiện chỉ là UI tĩnh, logic đổi bộ lọc sẽ làm ở bước xử lý dữ liệu thật.
 */
@Composable
fun <T> FilterDropdownRow(
    selectedOption: T,
    options: List<T>,
    optionLabel: (T) -> String,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { expanded = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = optionLabel(selectedOption),
            fontSize = 15.sp,
            color = HyperColors.TextMuted
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_unfold_more),
            contentDescription = "Chọn bộ lọc",
            tint = HyperColors.TextMuted,
            modifier = Modifier.size(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}