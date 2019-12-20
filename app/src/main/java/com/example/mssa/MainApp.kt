package com.example.mssa

import android.content.Context
import androidx.multidex.MultiDex
import com.example.mssa.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class MainApp : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        if (mLog.isInfoEnabled) {
            mLog.info("== START ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})  ==")
        }
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
