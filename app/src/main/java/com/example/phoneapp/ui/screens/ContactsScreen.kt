package com.example.phoneapp.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneapp.data.ContactsRepository
import com.example.phoneapp.model.ContactEntry
import com.example.phoneapp.ui.screens.components.AlphabetSectionHeader
import com.example.phoneapp.ui.screens.components.AppSearchBar
import com.example.phoneapp.ui.screens.components.ContactListItem
import com.example.phoneapp.ui.screens.components.FilterDropdownRow
import com.yourapp.phoneapp.ui.theme.HyperColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private enum class ContactSortOption(val label: String) {
    NAME_ASC("Tên A-Z"),
    NAME_DESC("Tên Z-A")
}

@Composable
fun ContactsScreen(
    onContactClick: (ContactEntry) -> Unit,
    onAddContactClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedSort by remember { mutableStateOf(ContactSortOption.NAME_ASC) }

    val groupedContacts by produceState(
        initialValue = emptyList<Pair<String, List<ContactEntry>>>(),
        key1 = context,
        key2 = selectedSort
    ) {
        value = withContext(Dispatchers.IO) {
            ContactsRepository.getContactsGroupedByLetter(
                context = context,
                ascending = selectedSort == ContactSortOption.NAME_ASC
            )
        }
    }

    var searchQuery by remember { mutableStateOf("") }

    val filteredGroupedContacts = remember(groupedContacts, searchQuery) {
        if (searchQuery.isBlank()) {
            groupedContacts
        } else {
            groupedContacts
                .map { (letter, contacts) ->
                    letter to contacts.filter { it.name.contains(searchQuery, ignoreCase = true) }
                }
                .filter { (_, contacts) -> contacts.isNotEmpty() }
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
            }

            Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                AppSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Tìm kiếm danh bạ"
                )
            }

            FilterDropdownRow(
                selectedOption = selectedSort,
                options = ContactSortOption.entries,
                optionLabel = { it.label },
                onOptionSelected = { selectedSort = it }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                filteredGroupedContacts.forEach { (letter, contacts) ->
                    item { AlphabetSectionHeader(letter = letter) }
                    items(contacts, key = { it.id }) { contact ->
                        ContactListItem(contact = contact, onClick = onContactClick)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddContactClick,
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
