package com.example.phoneapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneapp.data.ContactsRepository
import com.yourapp.phoneapp.ui.theme.HyperColors

@Composable
fun AddContactScreen(onClose: () -> Unit, onSaved: () -> Unit) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperColors.Background)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {

        // ----- Thanh trên cùng: đóng / tiêu đề / lưu -----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Huỷ",
                tint = HyperColors.TextPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClose() }
            )
            Text(
                text = "Liên hệ mới",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = HyperColors.TextPrimary
            )
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Lưu",
                tint = if (name.isNotBlank()) HyperColors.AccentGreen else HyperColors.TextSecondary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (name.isNotBlank()) {
                            val success = ContactsRepository.insertContact(
                                context = context,
                                name = name,
                                phoneNumber = phoneNumber,
                                email = email
                            )
                            if (success) onSaved()
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ----- Avatar placeholder -----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
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
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ----- Các trường nhập -----
        AddContactField(label = "Tên", value = name, onValueChange = { name = it })
        Spacer(modifier = Modifier.height(12.dp))
        AddContactField(
            label = "Số điện thoại",
            value = phoneNumber,
            onValueChange = { phoneNumber = it }
        )
        Spacer(modifier = Modifier.height(12.dp))
        AddContactField(label = "Email", value = email, onValueChange = { email = it })
    }
}

@Composable
private fun AddContactField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = HyperColors.TextPrimary,
            unfocusedTextColor = HyperColors.TextPrimary,
            focusedLabelColor = HyperColors.AccentGreen,
            unfocusedLabelColor = HyperColors.TextSecondary,
            focusedBorderColor = HyperColors.AccentGreen,
            unfocusedBorderColor = HyperColors.Surface,
            cursorColor = HyperColors.TextPrimary
        )
    )
}