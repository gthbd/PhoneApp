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
import com.example.phoneapp.data.ContactsRepository
import com.example.phoneapp.model.ContactEntry
import com.yourapp.phoneapp.ui.theme.HyperColors
import com.example.phoneapp.util.PhoneCallHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ContactDetailScreen(contact: ContactEntry, onBack: () -> Unit) {
    val context = LocalContext.current

    val phoneNumbers by produceState(
        initialValue = emptyList<String>(),
        key1 = contact.id
    ) {
        value = withContext(Dispatchers.IO) {
            ContactsRepository.getPhoneNumbersForContact(context, contact.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HyperColors.Background)
            .statusBarsPadding()
    ) {

        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Quay lại",
            tint = HyperColors.TextPrimary,
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .clickable { onBack() }
        )

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
                text = contact.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = HyperColors.TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (phoneNumbers.isEmpty()) {
            Text(
                text = "Liên hệ này chưa có số điện thoại nào",
                fontSize = 14.sp,
                color = HyperColors.TextSecondary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        } else {
            Text(
                text = "Số điện thoại",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = HyperColors.TextSecondary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(phoneNumbers, key = { it }) { number ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { PhoneCallHelper.placeCall(context, number) }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_call_filled),
                            contentDescription = "Gọi",
                            tint = HyperColors.AccentGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = number,
                            fontSize = 16.sp,
                            color = HyperColors.TextPrimary
                        )
                    }
                }
            }
        }
    }
}