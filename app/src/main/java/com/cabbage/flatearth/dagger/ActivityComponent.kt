package com.cabbage.flatearth.dagger

import com.cabbage.flatearth.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)
}