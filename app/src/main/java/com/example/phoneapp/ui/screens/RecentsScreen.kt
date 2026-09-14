package com.example.phoneapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phoneapp.R
import com.example.phoneapp.data.CallLogRepository
import com.example.phoneapp.model.CallLogEntry
import com.example.phoneapp.model.CallType
import com.example.phoneapp.ui.screens.components.AppSearchBar
import com.example.phoneapp.ui.screens.components.CallHistoryItem
import com.example.phoneapp.ui.screens.components.FilterDropdownRow
import com.example.phoneapp.util.PhoneCallHelper
import com.yourapp.phoneapp.ui.theme.HyperColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private enum class CallFilterOption(val label: String) {
    ALL("Tất cả cuộc gọi"),
    MISSED("Cuộc gọi nhỡ"),
    INCOMING("Cuộc gọi đến"),
    OUTGOING("Cuộc gọi đi")
}


@Composable
fun RecentsScreen(onEntryClick: (CallLogEntry) -> Unit) {
    val context = LocalContext.current

    val callLogEntries by produceState(
        initialValue = emptyList<CallLogEntry>(),
        key1 = context
    ) {
        value = withContext(Dispatchers.IO) {
            CallLogRepository.getCallLogEntries(context)
        }
    }

    var showDialpad by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(CallFilterOption.ALL) }

    val filteredEntries = remember(callLogEntries, searchQuery, selectedFilter) {
        callLogEntries
            .filter { entry ->
                when (selectedFilter) {
                    CallFilterOption.ALL -> true
                    CallFilterOption.MISSED -> entry.callType == CallType.MISSED
                    CallFilterOption.INCOMING -> entry.callType == CallType.INCOMING
                    CallFilterOption.OUTGOING -> entry.callType == CallType.OUTGOING
                }
            }
            .filter { entry ->
                searchQuery.isBlank() ||
                        entry.displayName.contains(searchQuery, ignoreCase = true) ||
                        entry.phoneNumber.contains(searchQuery)
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
                    text = "Gần đây",
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
                selectedOption = selectedFilter,
                options = CallFilterOption.entries,
                optionLabel = { it.label },
                onOptionSelected = { selectedFilter = it }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(filteredEntries, key = { it.id }) { entry ->
                    CallHistoryItem(
                        entry = entry,
                        onItemClick = { PhoneCallHelper.placeCall(context, it.phoneNumber) },
                        onDetailClick = onEntryClick
                    )
                }
            }
        }

        if (showDialpad) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showDialpad = false }
            )

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                DialpadScreen(onClose = { showDialpad = false })
            }
        } else {
            FloatingActionButton(
                onClick = { showDialpad = true },
                containerColor = HyperColors.AccentGreen,
                contentColor = HyperColors.Background,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.apps),
                    contentDescription = "Mở bàn phím số"
                )
            }
        }
    }
}