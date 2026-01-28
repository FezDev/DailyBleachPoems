// File: notification/PoemNotificationWorker.kt
package com.example.bleachdailypoem.notification

import android.content.Context
import android.content.res.Configuration
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bleachdailypoem.R
import com.example.bleachdailypoem.data.SettingsDataStore
import kotlinx.coroutines.flow.first
import java.util.Locale
import kotlin.random.Random

class PoemNotificationWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val dataStore = SettingsDataStore(appContext)
        val areNotificationsEnabled = dataStore.notificationsEnabled.first()

        if (!areNotificationsEnabled) {
            return Result.success()
        }

        // --- MODIFICA CHIAVE: Creiamo un contesto con la lingua corretta ---
        val savedLang = dataStore.language.first()
        val locale = Locale(savedLang)
        val config = Configuration(appContext.resources.configuration)
        config.setLocale(locale)
        val localizedContext = appContext.createConfigurationContext(config)
        // Da ora in poi, useremo 'localizedContext' per ottenere le stringhe.
        // --------------------------------------------------------------------

        val randomPoemNumber = Random.nextInt(1, 75)
        val poemName = getPoemName(randomPoemNumber)
        dataStore.saveDailyPoemNumber(randomPoemNumber)

        val spoilerPreference = dataStore.spoilerPreference.first()

        val (title, content) = when (spoilerPreference) {
            "option1" -> {
                val notificationTitle = "Daily Bleach #${randomPoemNumber} - $poemName"
                // Usa il contesto localizzato
                val notificationContent = localizedContext.getString(R.string.notif_open_app_content)
                notificationTitle to notificationContent
            }
            "option2" -> {
                val notificationTitle = "Daily Bleach #${randomPoemNumber}"
                // Usa il contesto localizzato
                val notificationContent = localizedContext.getString(R.string.notif_spoiler_content)
                notificationTitle to notificationContent
            }
            else -> {
                // Usa il contesto localizzato
                val notificationTitle = localizedContext.getString(R.string.notif_poem_ready_title)
                val notificationContent = localizedContext.getString(R.string.spoiler_opt3_example)
                notificationTitle to notificationContent
            }
        }

        val notificationService = PoemNotificationService(appContext)
        notificationService.showNotification(title, content)

        return Result.success()
    }

    private fun getPoemName(poemNumber: Int): String {
        val poemNames = mapOf(
            1 to "The Death and The Strawberry",
            2 to "Goodbye, Parakeet, Goodnite My Sista",
            3 to "Memories in the Rain",
            4 to "Quincy Archer Hates You",
            5 to "Rightarm of the Giant",
            6 to "The Death Trilogy Overture",
            7 to "The Broken Coda",
            8 to "The Blade and Me",
            9 to "Fourteen Days for Conspiracy",
            10 to "Tattoo on the Sky",
            11 to "A Star and a Stray Dog",
            12 to "Flower on the Precipice",
            13 to "The Undead",
            14 to "White Tower Rocks",
            15 to "Beginning of the End of Tomorrow",
            16 to "Night of Wijnruit",
            17 to "Rosa Rubicundior, Lilio Candidior",
            18 to "The Deathberry Returns",
            19 to "The Black Moon Rising",
            20 to "End of Hypnosis",
            21 to "Be My Family or Not",
            22 to "Conquistadores",
            23 to "Mala Suerte!",
            24 to "Immanent God Blues",
            25 to "No Shaking Throne",
            26 to "The Mascaron Drive",
            27 to "Goodbye, Halcyon Days.",
            28 to "Baron's Lecture Full-Course",
            29 to "The Slashing Opera",
            30 to "There is No Heart Without You",
            31 to "Don't Kill My Volupture",
            32 to "Howling",
            33 to "The Bad Joke",
            34 to "King of the Kill",
            35 to "Higher Than the Moon",
            36 to "Turn Back the Pendulum",
            37 to "Beauty is So Solitary",
            38 to "Fear for Fight",
            39 to "El Verdugo",
            40 to "The Lust",
            41 to "Heart",
            42 to "Shock of the Queen",
            43 to "Kingdom of Hollows",
            44 to "Vice It",
            45 to "The Burnout Inferno",
            46 to "Back from the Dead",
            47 to "End of the Chrysalis Age",
            48 to "God is Dead",
            49 to "The Lost Agent",
            50 to "The Six Fullbringers",
            51 to "Love Me Bitterly, Loth Me Sweetly",
            52 to "End of Bond",
            53 to "The Deathberry Returns 2",
            54 to "Goodbye to Our Xcution",
            55 to "The Blood Warfare",
            56 to "March of the StarCross",
            57 to "Out of Bloom",
            58 to "The Fire",
            59 to "The Battle",
            60 to "Everything But the Rain",
            61 to "The Last 9 Days",
            62 to "Heart of Wolf",
            63 to "Heard, Fear, Here",
            64 to "Death in Vision",
            65 to "Marching Out the Zombies",
            66 to "Sorry I Am Strong",
            67 to "Black",
            68 to "The Ordinary Peace",
            69 to "Against the Judgement",
            70 to "Friend",
            71 to "The Princess Dissection",
            72 to "My Last Words",
            73 to "The End of the End",
            74 to "The Death and The Strawberry"
        )
        return poemNames[poemNumber] ?: "Unknown Volume"
    }
}
