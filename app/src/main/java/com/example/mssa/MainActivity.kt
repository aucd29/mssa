package com.example.mssa

import android.os.Bundle
import androidx.annotation.LayoutRes
import brigitte.BaseDaggerActivity
import brigitte.chromeInspector
import brigitte.exceptionCatcher
import com.example.mssa.databinding.MainActivityBinding
import com.example.mssa.ui.main.MainPageAdapter
import com.example.mssa.ui.main.MainViewModel
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, MainViewModel>() {
    @LayoutRes
    override var layoutId: Int = R.layout.main_activity

    @Inject lateinit var mAdapter: MainPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        chromeInspector { if (mLog.isInfoEnabled) { mLog.info(it) }}
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        if (mLog.isDebugEnabled) {
            mLog.debug("START ACTIVITY")
        }
    }

    override fun initViewBinding() = mBinding.run {
        with (mainViewPager) {
            adapter = mAdapter
            mainTabs.setupWithViewPager(this)
        }
    }

    override fun initViewModelEvents() = mViewModel.run {
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(MainActivity::class.java)
    }


    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TEST
    //
    ////////////////////////////////////////////////////////////////////////////////////

    // https://github.com/chiuki/espresso-samples/tree/master/idling-resource-okhttp
//    @VisibleForTesting
//    @Inject lateinit var okhttp: OkHttpClient
}