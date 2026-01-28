// File: com/example/bleachdailypoem/MainActivity.kt
package com.example.bleachdailypoem

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bleachdailypoem.data.SettingsDataStore
import com.example.bleachdailypoem.notification.scheduleDailyPoemNotification
import com.example.bleachdailypoem.ui.InfoScreen
import com.example.bleachdailypoem.ui.MainScreen // <-- ECCO L'IMPORT MANCANTE
import com.example.bleachdailypoem.ui.Routes
import com.example.bleachdailypoem.ui.SettingsScreen
import com.example.bleachdailypoem.ui.theme.BleachDailyPoemTheme
import com.example.bleachdailypoem.ui.theme.appGradient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val lang = runBlocking { SettingsDataStore(newBase).language.first() }
        val locale = Locale(lang)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scheduleDailyPoemNotification(applicationContext, 9, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BleachDailyPoemTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(appGradient(isSystemInDarkTheme()))
                ) {
                    RequestNotificationPermission()
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.MAIN_SCREEN
                    ) {
                        composable(Routes.MAIN_SCREEN) {
                            MainScreen(
                                onSettingsClick = { navController.navigate(Routes.SETTINGS_SCREEN) }
                            )
                        }
                        composable(Routes.SETTINGS_SCREEN) {
                            SettingsScreen(
                                navController = navController,
                                onInfoClick = { navController.navigate(Routes.INFO_SCREEN) },
                                onLanguageChanged = { recreate() }
                            )
                        }
                        composable(Routes.INFO_SCREEN) {
                            InfoScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun RequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val context = LocalContext.current
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            LaunchedEffect(key1 = hasPermission) {
                if (!hasPermission) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}
