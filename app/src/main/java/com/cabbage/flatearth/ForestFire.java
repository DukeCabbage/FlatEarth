package com.cabbage.flatearth;

import timber.log.Timber;

/**
 * Created by Leo on 2017-02-27.
 * This tree will print to logcat, with line number in front of message.
 */

public final class ForestFire extends Timber.DebugTree {

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return super.createStackElementTag(element) + ':' + element.getLineNumber();
    }
}