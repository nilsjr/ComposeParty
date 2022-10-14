package de.nilsdruyen.composeparty

import android.app.Application
import timber.log.Timber

class ComposePartyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}