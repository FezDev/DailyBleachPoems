// File: notification/WorkerScheduler.kt

// Definiamo il pacchetto
package com.example.bleachdailypoem.notification

// Import necessari
import android.content.Context
// L'IMPORT SBAGLIATO 'privacysandbox' È STATO RIMOSSO DA QUI
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder // Aggiunto import corretto
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

// Nome univoco per identificare il nostro lavoro periodico.
// Serve per poterlo aggiornare o cancellare in futuro.
const val DAILY_POEM_WORK_NAME = "daily_poem_notification_work"

/**
 * Funzione che programma l'esecuzione del PoemNotificationWorker ogni 24 ore,
 * cercando di avviarlo per la prima volta all'orario specificato.
 */
fun scheduleDailyPoemNotification(context: Context, hour: Int, minute: Int) {
    // 1. Otteniamo un'istanza di WorkManager.
    val workManager = WorkManager.getInstance(context)

    // 2. Calcoliamo il ritardo iniziale.
    val currentTime = Calendar.getInstance()
    val targetTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        if (before(currentTime)) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

    // 3. Creiamo la richiesta di lavoro periodico.
    val dailyWorkRequest = PeriodicWorkRequestBuilder<PoemNotificationWorker>(
        24,
        TimeUnit.HOURS
    )
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build() // Ora questa chiamata a .build() funziona

    // 4. Accodiamo il lavoro in WorkManager.
    workManager.enqueueUniquePeriodicWork(
        DAILY_POEM_WORK_NAME,
        ExistingPeriodicWorkPolicy.UPDATE,
        dailyWorkRequest
    )
} // <-- La funzione scheduleDailyPoemNotification finisce qui correttamente.

/**
 * Funzione di DEBUG per eseguire il worker una sola volta e immediatamente.
 * ORA SI TROVA FUORI DALL'ALTRA FUNZIONE, AL POSTO GIUSTO.
 */
fun testImmediatePoemNotification(context: Context) {
    val testWorkRequest = OneTimeWorkRequestBuilder<PoemNotificationWorker>()
        // Non impostiamo nessun ritardo, quindi parte il prima possibile
        .build() // E anche questa funziona

    WorkManager.getInstance(context).enqueue(testWorkRequest)
}
