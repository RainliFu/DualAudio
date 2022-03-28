package com.qytech.dualaudio.components

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.qytech.dualaudio.extensions.setFixedVideoSize
import timber.log.Timber


enum class AudioSession(val id: Int) {
    PRIMARY(121),
    SECONDARY(57)
}

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    url: AssetFileDescriptor,
    audioSession: AudioSession
) {
    val content = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val mediaPlayer = remember {
        MediaPlayer().apply {
            audioSessionId = audioSession.id
            setDataSource(url)
            prepareAsync()
        }
    }
    val lifecycleObserver = rememberMediaPlayerLifecycleObserver(mediaPlayer)
    AndroidView(modifier = modifier, factory = {
        SurfaceView(content).apply {
            holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    holder.setKeepScreenOn(true)
                    setFixedVideoSize(mediaPlayer.videoWidth, mediaPlayer.videoHeight)
                    mediaPlayer.setDisplay(holder)
                    mediaPlayer.start()
                    mediaPlayer.isLooping = true
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    holder.setKeepScreenOn(false)
                }
            })
        }

    })

    DisposableEffect(url) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }

    }
}

@Composable
private fun rememberMediaPlayerLifecycleObserver(mediaPlayer: MediaPlayer): LifecycleEventObserver =
    remember(mediaPlayer) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    Timber.d("resume media player")
                    mediaPlayer.start()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    Timber.d("pause media player")
                    mediaPlayer.pause()
                }
                Lifecycle.Event.ON_STOP -> {
                    Timber.d("stop media player")
                    mediaPlayer.pause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    Timber.d("release media player")
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                    }
                    mediaPlayer.release()
                }
                else -> {}
            }
        }
    }

