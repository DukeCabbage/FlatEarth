package com.cabbage.flatearth.dagger

import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Component
import javax.inject.Named

@ApplicationScope
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    @Named("appContext")
    fun appContext(): Context

    fun rxPreference(): RxSharedPreferences
}