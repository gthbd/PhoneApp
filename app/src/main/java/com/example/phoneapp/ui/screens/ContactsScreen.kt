package com.example.phoneapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneapp.R
import com.example.phoneapp.data.ContactsRepository
import com.example.phoneapp.model.ContactEntry
import com.example.phoneapp.ui.screens.components.AlphabetIndexBar
import com.example.phoneapp.ui.screens.components.AlphabetSectionHeader
import com.example.phoneapp.ui.screens.components.AppSearchBar
import com.example.phoneapp.ui.screens.components.ContactListItem
import com.example.phoneapp.ui.screens.components.FilterDropdownRow
import com.yourapp.phoneapp.ui.theme.HyperColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ContactsScreen() {
    val context = LocalContext.current

    val groupedContacts by produceState(
        initialValue = emptyMap<String, List<ContactEntry>>(),
        key1 = context
    ) {
        value = withContext(Dispatchers.IO) {
            ContactsRepository.getContactsGroupedByLetter(context)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Danh bạ",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = HyperColors.TextPrimary
                )
                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Cài đặt",
                    tint = HyperColors.TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                AppSearchBar(placeholder = "Tìm kiếm danh bạ")
            }

            FilterDropdownRow(label = "Tất cả danh bạ")

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                item { MyProfileRow() }
                item { SectionDivider() }
                item { MyGroupsRow() }
                item { SectionDivider() }

                groupedContacts.forEach { (letter, contacts) ->
                    item { AlphabetSectionHeader(letter = letter) }
                    items(contacts, key = { it.id }) { contact ->
                        ContactListItem(contact = contact)
                    }
                }
            }
        }

        AlphabetIndexBar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp)
        )

        FloatingActionButton(
            onClick = { /* TODO: mở màn hình thêm liên hệ */ },
            containerColor = HyperColors.AccentGreen,
            contentColor = HyperColors.Background,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Thêm liên hệ"
            )
        }
    }
}

@Composable
private fun MyProfileRow() {
    Text(
        text = "My profile",
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        color = HyperColors.TextPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: mở màn hình hồ sơ cá nhân */ }
            .padding(horizontal = 20.dp, vertical = 14.dp)
    )
}

@Composable
private fun MyGroupsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: mở màn hình nhóm liên hệ */ }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My groups",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = HyperColors.TextPrimary
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "Xem nhóm liên hệ",
            tint = HyperColors.TextSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(1.dp)
            .background(HyperColors.Divider)
    )
}
