package com.cabbage.flatearth.misc

import android.app.Application

import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(ForestFire())
    }
}
