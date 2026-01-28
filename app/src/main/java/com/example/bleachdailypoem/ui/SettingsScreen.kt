// File: ui/SettingsScreen.kt
package com.example.bleachdailypoem.ui

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.WorkManager
import com.example.bleachdailypoem.R
import com.example.bleachdailypoem.data.SettingsDataStore
import com.example.bleachdailypoem.notification.DAILY_POEM_WORK_NAME
import com.example.bleachdailypoem.notification.scheduleDailyPoemNotification
import com.example.bleachdailypoem.notification.testImmediatePoemNotification
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onInfoClick: () -> Unit,
    onLanguageChanged: () -> Unit
) {
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    // Leggiamo tutte le preferenze
    val currentLanguage by settingsDataStore.language.collectAsState(initial = "en")
    val currentTheme by settingsDataStore.themePreference.collectAsState(initial = "system")
    val notificationsEnabled by settingsDataStore.notificationsEnabled.collectAsState(initial = true)
    val currentTime by settingsDataStore.notificationTime.collectAsState(initial = 9 to 0)
    val currentSpoilerOption by settingsDataStore.spoilerPreference.collectAsState(initial = "option1")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_desc)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(id = R.string.info_button_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LanguageSetting(
                selectedLanguage = currentLanguage,
                onLanguageSelected = { newLang ->
                    coroutineScope.launch {
                        settingsDataStore.saveLanguage(newLang)
                        onLanguageChanged()
                    }
                }
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            ThemeSetting(
                selectedTheme = currentTheme,
                onThemeSelected = { newTheme ->
                    coroutineScope.launch {
                        settingsDataStore.saveThemePreference(newTheme)
                    }
                }
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(id = R.string.daily_notification), style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { isEnabled ->
                        coroutineScope.launch {
                            settingsDataStore.saveNotificationsEnabled(isEnabled)
                            val toastMsg = if (isEnabled) {
                                scheduleDailyPoemNotification(context, currentTime.first, currentTime.second)
                                context.getString(R.string.notification_enabled_toast)
                            } else {
                                WorkManager.getInstance(context).cancelUniqueWork(DAILY_POEM_WORK_NAME)
                                context.getString(R.string.notification_disabled_toast)
                            }
                            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            val dependentSettingsAlpha = if (notificationsEnabled) 1.0f else 0.5f
            Column(modifier = Modifier.alpha(dependentSettingsAlpha)) {
                TimeSetting(
                    hour = currentTime.first,
                    minute = currentTime.second,
                    enabled = notificationsEnabled,
                    onTimeSelected = { newHour, newMinute ->
                        if (notificationsEnabled) {
                            coroutineScope.launch {
                                settingsDataStore.saveNotificationTime(newHour, newMinute)
                                scheduleDailyPoemNotification(context, newHour, newMinute)
                                Toast.makeText(context, context.getString(R.string.time_updated_toast), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                SpoilerSettings(
                    selectedOption = currentSpoilerOption,
                    enabled = notificationsEnabled,
                    onOptionSelected = { newOption ->
                        if (notificationsEnabled) {
                            coroutineScope.launch {
                                settingsDataStore.saveSpoilerPreference(newOption)
                                Toast.makeText(context, context.getString(R.string.spoiler_updated_toast), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Button(
                onClick = {
                    testImmediatePoemNotification(context)
                    Toast.makeText(context, context.getString(R.string.test_notification_toast), Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text(stringResource(id = R.string.test_notification_button))
            }
        }
    }
}

// --- MODIFICA CHIAVE QUI ---
@Composable
fun ThemeSetting(selectedTheme: String, onThemeSelected: (String) -> Unit) {
    // Ora usiamo le risorse stringa invece di testi fissi
    data class ThemeOption(val key: String, val title: String)
    val themeOptions = listOf(
        ThemeOption("light", stringResource(id = R.string.theme_light)),
        ThemeOption("dark", stringResource(id = R.string.theme_dark)),
        ThemeOption("system", stringResource(id = R.string.theme_system))
    )

    Column {
        Text(stringResource(id = R.string.theme_setting), style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            themeOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onThemeSelected(option.key) }
                ) {
                    RadioButton(
                        selected = (option.key == selectedTheme),
                        onClick = { onThemeSelected(option.key) }
                    )
                    Text(text = option.title, modifier = Modifier.padding(start = 2.dp, end = 8.dp))
                }
            }
        }
    }
}

// Il resto dei file (LanguageSetting, TimeSetting, SpoilerSettings)
// è già corretto e non necessita di modifiche.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSetting(selectedLanguage: String, onLanguageSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val languages = mapOf("en" to "English", "it" to "Italiano")

    Column {
        Text(stringResource(id = R.string.choose_language), style = MaterialTheme.typography.bodyLarge)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = languages[selectedLanguage] ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onLanguageSelected(code)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TimeSetting(hour: Int, minute: Int, enabled: Boolean, onTimeSelected: (Int, Int) -> Unit) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(context, { _, newHour, newMinute -> onTimeSelected(newHour, newMinute) }, hour, minute, true)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { timePickerDialog.show() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(stringResource(id = R.string.notification_time), style = MaterialTheme.typography.bodyLarge)
            Text(stringResource(id = R.string.notification_time_desc), style = MaterialTheme.typography.bodySmall)
        }
        Text(text = String.format("%02d:%02d", hour, minute), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun SpoilerSettings(selectedOption: String, enabled: Boolean, onOptionSelected: (String) -> Unit) {
    data class SpoilerOption(val key: String, val title: String, val example: String)

    val spoilerOptions = listOf(
        SpoilerOption("option1", stringResource(id = R.string.spoiler_opt1_title), stringResource(id = R.string.spoiler_opt1_example)),
        SpoilerOption("option2", stringResource(id = R.string.spoiler_opt2_title), stringResource(id = R.string.spoiler_opt2_example)),
        SpoilerOption("option3", stringResource(id = R.string.spoiler_opt3_title), stringResource(id = R.string.spoiler_opt3_example))
    )

    Column {
        Text(stringResource(id = R.string.spoiler_preference), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        spoilerOptions.forEach { option ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { onOptionSelected(option.key) }
                    .padding(vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (option.key == selectedOption),
                        enabled = enabled,
                        onClick = { onOptionSelected(option.key) }
                    )
                    Text(text = option.title, modifier = Modifier.padding(start = 8.dp))
                }
                Text(
                    text = option.example,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 48.dp)
                )
            }
        }
    }
}
