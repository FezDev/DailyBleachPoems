// File: ui/theme/Theme.kt
package com.example.bleachdailypoem.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.bleachdailypoem.data.SettingsDataStore

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun appGradient(isDarkTheme: Boolean): Brush {
    return if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF252525),
                Color(0xFF121212)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF7F7F9),
                MaterialTheme.colorScheme.background
            )
        )
    }
}

@Composable
fun BleachDailyPoemTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Leggiamo la preferenza del tema dal DataStore
    val context = LocalContext.current
    val dataStore = remember { SettingsDataStore(context) }
    val themePreference by dataStore.themePreference.collectAsState(initial = "system")

    // Decidiamo se usare il tema scuro in base alla preferenza
    val darkTheme = when (themePreference) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme() // "system" o qualsiasi altro valore
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val localContext = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(localContext) else dynamicLightColorScheme(localContext)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
