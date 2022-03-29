package com.qytech.dualaudio.components

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.hardware.display.DisplayManager
import android.view.Display
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.unit.dp
import com.qytech.dualaudio.R
import timber.log.Timber

fun testVideoPrimary(context: Context): AssetFileDescriptor =
    context.resources.openRawResourceFd(R.raw.honeysnowcity)

fun testVideoSecondary(context: Context): AssetFileDescriptor =
    context.resources.openRawResourceFd(R.raw.pokemon)

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    Timber.d("${testVideoPrimary(context)} ${testVideoSecondary(context)}")
    val displayManager = remember {
        context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }
    val displays = displayManager.displays
    if (displays.size > 1) {
        DualScreenPlayer(context, displays[1])
    } else {
        OneScreenPlayer(context)
    }
}

@Composable
fun DualScreenPlayer(context: Context, display: Display) {
    val lifecycleOwner = LocalSavedStateRegistryOwner.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        VideoPlayer(url = testVideoPrimary(context), audioSession = AudioSession.PRIMARY)
    }
    val customDisplay = remember {
        SecondaryDisplay(
            context,
            display,
            testVideoSecondary(context),
            AudioSession.SECONDARY,
            lifecycleOwner
        )
    }
    customDisplay.show()
}

@Composable
fun OneScreenPlayer(context: Context) {
    val config = LocalConfiguration.current
    Timber.d("screen width ${config.screenWidthDp.dp} screen height ${config.screenHeightDp.dp}")
    if (config.screenWidthDp > config.screenHeightDp) {
        Row(modifier = Modifier.fillMaxSize()) {
            VideoPlayer(
                url = testVideoPrimary(context),
                audioSession = AudioSession.PRIMARY
            )
            Spacer(modifier = Modifier.weight(1f))
            VideoPlayer(
                url = testVideoSecondary(context),
                audioSession = AudioSession.SECONDARY
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            VideoPlayer(
                url = testVideoPrimary(context),
                audioSession = AudioSession.PRIMARY
            )
            Spacer(modifier = Modifier.weight(1f))
            VideoPlayer(
                url = testVideoSecondary(context),
                audioSession = AudioSession.SECONDARY
            )
        }
    }

}