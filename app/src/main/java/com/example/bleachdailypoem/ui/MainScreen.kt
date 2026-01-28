// File: ui/MainScreen.kt
package com.example.bleachdailypoem.ui

import android.content.res.Resources
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Import per ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.bleachdailypoem.R
import com.example.bleachdailypoem.data.SettingsDataStore
import kotlinx.coroutines.flow.first
import kotlin.random.Random

@Composable
fun MainScreen(
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStore = remember { SettingsDataStore(context) }
    var poemNumberToShow by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(key1 = true) {
        val currentPoem = dataStore.dailyPoemNumber.first()
        if (currentPoem != -1) {
            poemNumberToShow = currentPoem
        } else {
            val firstPoem = Random.nextInt(1, 75)
            dataStore.saveDailyPoemNumber(firstPoem)
            poemNumberToShow = firstPoem
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (contentColumn, settingsIcon, copyrightColumn) = createRefs()

        if (poemNumberToShow == null) {
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(createRef()) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .constrainAs(contentColumn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    text = stringResource(id = R.string.main_daily_poem_title, poemNumberToShow!!),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                FlippablePoemCard(poemNumber = poemNumberToShow!!)

                Text(
                    text = stringResource(id = R.string.tap_to_reveal),
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.constrainAs(settingsIcon) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.info_button_desc)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(copyrightColumn) {
                centerHorizontallyTo(parent)
                bottom.linkTo(settingsIcon.bottom)
                top.linkTo(settingsIcon.top)
            }
        ) {
            Text(
                text = stringResource(id = R.string.copyright_line_1),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = stringResource(id = R.string.copyright_line_2),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun FlippablePoemCard(poemNumber: Int) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "FlipAnimation"
    )
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .clickable { isFlipped = !isFlipped }
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12 * density.density
                }
            ) {
                if (rotation < 90f) {
                    Image(
                        painter = painterResource(id = getDrawableResourceId("cover_$poemNumber")),
                        contentDescription = stringResource(id = R.string.cover_image_desc, poemNumber)
                    )
                } else {
                    Image(
                        painter = painterResource(id = getDrawableResourceId("poem_$poemNumber")),
                        contentDescription = stringResource(id = R.string.poem_image_desc, poemNumber),
                        modifier = Modifier.graphicsLayer { rotationY = 180f }
                    )
                }
            }
        }

        if (!isFlipped) {
            Icon(
                imageVector = Icons.Default.Cached,
                contentDescription = "Tocca per girare",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .alpha(0.8f)
            )
        }
    }
}

@Composable
private fun getDrawableResourceId(name: String): Int {
    val context = LocalContext.current
    val resources: Resources = context.resources
    val resourceId = remember(name) {
        resources.getIdentifier(name, "drawable", context.packageName)
    }
    return if (resourceId != 0) resourceId else R.drawable.ic_launcher_background
}

