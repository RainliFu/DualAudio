package com.qytech.dualaudio.components

import android.app.Presentation
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.view.Display
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner

class SecondaryDisplay(
    outerContext: Context,
    display: Display,
    private val url: AssetFileDescriptor,
    private val audioSession: AudioSession,
    private val lifecycleOwner: SavedStateRegistryOwner
) : Presentation(outerContext, display) {
    init {
        setContentView(contentView())
    }

    private fun contentView() = ComposeView(context).apply {
        setContent {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                VideoPlayer(url = url, audioSession = audioSession)
            }

            DisposableEffect(lifecycleOwner) {
                onDispose {
                    dismiss()
                }
            }
        }
        ViewTreeLifecycleOwner.set(this, lifecycleOwner)
        ViewTreeSavedStateRegistryOwner.set(this, lifecycleOwner)
    }
}