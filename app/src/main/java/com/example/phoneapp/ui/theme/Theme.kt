package com.example.phoneapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.yourapp.phoneapp.ui.theme.HyperColors

private val DarkColorScheme = darkColorScheme(
    primary = HyperColors.AccentGreen,
    secondary = HyperColors.AccentBlue,
    background = HyperColors.Background,
    surface = HyperColors.Surface,
    onBackground = HyperColors.TextPrimary,
    onSurface = HyperColors.TextPrimary
)

@Composable
fun PhoneAppTheme(
    // App luôn dùng theme tối cố định theo thiết kế mẫu — không theo hệ thống, không dynamic color.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(context)
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}