package com.cabbage.flatearth.dagger

import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityScope @Provides
    fun providesRxPermission(): RxPermissions = RxPermissions(activity)
}