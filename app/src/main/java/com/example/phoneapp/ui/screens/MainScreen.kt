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
import com.yourapp.phoneapp.ui.theme.HyperColors

enum class DialerTab {
    RECENTS, CONTACTS
}

private val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.READ_CONTACTS
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
                DialerTab.RECENTS -> RecentsScreen()
                DialerTab.CONTACTS -> ContactsScreen()
            }
        }
    }
}

private fun hasAllPermissions(context: android.content.Context): Boolean {
    return REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}