package com.cabbage.flatearth.dagger

import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityScope @Provides
    fun providesRxPermission(): RxPermissions = RxPermissions(activity)
}