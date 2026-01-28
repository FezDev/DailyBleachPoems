package com.example.bleachdailypoem.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            // Aggiungiamo una barra del titolo con un pulsante "Indietro"
            TopAppBar(
                title = { Text("Informazioni e Disclaimer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Torna indietro"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Usiamo una Column scrollabile per assicurarci che il testo sia sempre leggibile
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Rende la colonna scorrevole
        ) {
            // Disclaimer in Italiano
            Text("Disclaimer (Italiano)", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Questa è un'applicazione non ufficiale, creata da un fan per altri fan, e non è in alcun modo affiliata, approvata o supportata da Shueisha Inc., Tite Kubo o qualsiasi altra entità associata all'opera originale.\n\n" +
                        "Tutte le immagini, i testi (poemi) e i nomi relativi a \"Bleach\" sono marchi registrati e/o copyright di Shueisha Inc. e Tite Kubo. L'uso di questo materiale è inteso a scopo di tributo e intrattenimento, senza alcuna intenzione di violare i diritti d'autore.\n\n" +
                        "Quest'app è e rimarrà sempre gratuita. Non è previsto alcun guadagno dalla sua distribuzione.\n\n" +
                        "Se sei un detentore dei diritti e ritieni che questa applicazione violi il tuo copyright, ti prego di contattarmi all'indirizzo fez.devmail@gmail.com e provvederò a rimuovere immediatamente l'applicazione dal Google Play Store.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Disclaimer in Inglese
            Text("Disclaimer (English)", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This is an unofficial, fan-made application created for fans, and is in no way affiliated with, endorsed, or supported by Shueisha Inc., Tite Kubo, or any entity associated with the original work.\n\n" +
                        "All images, texts (poems), and names related to \"Bleach\" are registered trademarks and/or copyrights of Shueisha Inc. and Tite Kubo. The use of this material is intended for tribute and entertainment purposes only, with no intention of copyright infringement.\n\n" +
                        "This app is and will always be free. No profit is being made from its distribution.\n\n" +
                        "If you are a copyright holder and believe that this application infringes upon your copyright, please contact me at fez.devmail@gmail.com, and I will promptly remove the application from the Google Play Store.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
