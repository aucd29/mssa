package com.example.dhk

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.example.dhk.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
 */

class MainApp : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        Log.e("[BK]", "== START ${BuildConfig.APPLICATION_ID} ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})  ==")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DAGGER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private val mComponent: AndroidInjector<MainApp> by lazy(LazyThreadSafetyMode.NONE) {
        DaggerAppComponent.factory().create(this)
    }

    override fun applicationInjector() =
        mComponent

    companion object {
        private val mLog = LoggerFactory.getLogger(MainApp::class.java)
    }
}
