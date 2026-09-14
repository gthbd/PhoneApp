package com.example.phoneapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.phoneapp.model.CallLogEntry
import com.example.phoneapp.model.ContactEntry
import com.yourapp.phoneapp.ui.theme.HyperColors

enum class DialerTab {
    RECENTS, CONTACTS
}

private val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.CALL_PHONE,
    Manifest.permission.WRITE_CONTACTS
)

@Composable
fun DialerMainScreen() {
    val context = LocalContext.current

    var permissionsGranted by remember {
        mutableStateOf(hasAllPermissions(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        permissionsGranted = results.values.all { it }
    }

    LaunchedEffect(Unit) {
        if (!permissionsGranted) {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    if (!permissionsGranted) {
        PermissionRequestScreen(
            onRequestClick = { permissionLauncher.launch(REQUIRED_PERMISSIONS) }
        )
        return
    }

    // Khi khác null, hiển thị màn hình chi tiết cuộc gọi thay cho toàn bộ Scaffold bên dưới.
    var selectedCallEntry by remember { mutableStateOf<CallLogEntry?>(null) }
    selectedCallEntry?.let { entry ->
        CallDetailScreen(entry = entry, onBack = { selectedCallEntry = null })
        return
    }

    var selectedContact by remember { mutableStateOf<ContactEntry?>(null) }
    selectedContact?.let { contact ->
        ContactDetailScreen(contact = contact, onBack = { selectedContact = null })
        return
    }

    var showAddContact by remember { mutableStateOf(false) }
    if (showAddContact) {
        AddContactScreen(
            onClose = { showAddContact = false },
            onSaved = { showAddContact = false }
        )
        return
    }


    var selectedTab by remember { mutableStateOf(DialerTab.RECENTS) }

    Scaffold(
        containerColor = HyperColors.Background,
        bottomBar = {
            DialerBottomTaskBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                DialerTab.RECENTS -> RecentsScreen(
                    onEntryClick = { selectedCallEntry = it }
                )
                DialerTab.CONTACTS -> ContactsScreen(
                    onContactClick = { selectedContact = it },
                    onAddContactClick = { showAddContact = true }
                )
            }
        }
    }
}

private fun hasAllPermissions(context: android.content.Context): Boolean {
    return REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}