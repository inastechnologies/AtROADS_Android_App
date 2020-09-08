package com.inas.atroads.services;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class AppLifecycleListener implements LifecycleObserver
{
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        Log.i("AppLifecycleListener", "Foreground");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        // app moved to background
        Log.i("AppLifecycleListener", "Background");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onAppDestroyed() {
        // app moved to onAppDestroyed
        Log.i("AppLifecycleListener", "onAppDestroyed");
    }

}
