// File: notification/PoemNotificationService.kt
package com.example.bleachdailypoem.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.bleachdailypoem.MainActivity
import com.example.bleachdailypoem.R

class PoemNotificationService(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "poem_notification_channel"
        const val NOTIFICATION_ID = 1
        // Non ci serve più passare il numero del poema nell'intent
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Bleach Poem",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifiche giornaliere per il poema di Bleach"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    // --- MODIFICA: Ripristiniamo la funzione originale ma con logica aggiornata ---
    fun showNotification(title: String, content: String) {
        // L'intent ora è semplice: apre solo l'app, senza passare dati.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE // Usiamo IMMUTABLE perché l'intent non cambia
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title) // Usa il titolo personalizzato
            .setContentText(content) // Usa il testo personalizzato
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
