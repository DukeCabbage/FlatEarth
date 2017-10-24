package com.cabbage.flatearth.dagger

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule(private val context: Context) {

    @ApplicationScope @Provides
    @Named("appContext")
    fun providesContext(): Context = context

    @ApplicationScope @Provides
    fun providesPreference(): SharedPreferences =
            context.getSharedPreferences("default", MODE_PRIVATE)

    @ApplicationScope @Provides
    fun providesRxPermission(sharedPreferences: SharedPreferences): RxSharedPreferences =
            RxSharedPreferences.create(sharedPreferences)
}