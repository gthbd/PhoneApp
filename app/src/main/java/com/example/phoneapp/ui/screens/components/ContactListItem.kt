package com.example.phoneapp.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneapp.model.ContactEntry
import com.yourapp.phoneapp.ui.theme.HyperColors

@Composable
fun ContactListItem(
    contact: ContactEntry,
    onClick: (ContactEntry) -> Unit = {}
) {
    Text(
        text = contact.name,
        fontSize = 17.sp,
        fontWeight = FontWeight.Normal,
        color = HyperColors.TextPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(contact) }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    )
}