package com.qytech.dualaudio

import android.app.Application
import timber.log.Timber

class DualAudioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}