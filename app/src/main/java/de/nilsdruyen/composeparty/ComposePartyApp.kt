package de.nilsdruyen.composeparty

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import timber.log.Timber

class ComposePartyApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }
}