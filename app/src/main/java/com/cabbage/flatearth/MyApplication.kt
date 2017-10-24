package com.cabbage.flatearth

import android.app.Application
import com.cabbage.flatearth.dagger.AppComponent
import com.cabbage.flatearth.dagger.AppModule
import com.cabbage.flatearth.dagger.DaggerAppComponent

import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(ForestFire())

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
