package com.cabbage.flatearth.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cabbage.flatearth.MyApplication
import com.cabbage.flatearth.dagger.ActivityComponent
import com.cabbage.flatearth.dagger.ActivityModule
import com.cabbage.flatearth.dagger.ConfigPersistentComponent
import com.cabbage.flatearth.dagger.DaggerConfigPersistentComponent
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong


abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private val KEY_ACTIVITY_ID = "ACTIVITY_ID"
        private val NextId = AtomicLong(0)
        private val ComponentMap: MutableMap<Long, ConfigPersistentComponent> = HashMap()
    }

    protected lateinit var activityComponent: ActivityComponent
    private var mActivityId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NextId.getAndIncrement()

        val configPersistentComponent = ComponentMap[mActivityId!!] ?: DaggerConfigPersistentComponent.builder()
                .appComponent(MyApplication.appComponent)
                .build()

        if (!ComponentMap.containsKey(mActivityId!!)) {
            ComponentMap.put(mActivityId!!, configPersistentComponent)
        }

        activityComponent = configPersistentComponent.activityComponent(ActivityModule(this))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.v("Saving activity id=$mActivityId")
        outState.putLong(KEY_ACTIVITY_ID, mActivityId!!)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=$mActivityId")
            ComponentMap.remove(mActivityId)
        }
        super.onDestroy()
    }
}
