package com.cabbage.flatearth.dagger

import dagger.Component

@ConfigPersistent
@Component(dependencies = arrayOf(AppComponent::class))
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent
}