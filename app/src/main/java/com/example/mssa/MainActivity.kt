package com.example.mssa

import android.os.Bundle
import androidx.annotation.LayoutRes
import brigitte.BaseDaggerActivity
import brigitte.chromeInspector
import brigitte.exceptionCatcher
import com.example.mssa.databinding.MainActivityBinding
import com.example.mssa.ui.Navigator
import com.example.mssa.ui.main.MainViewModel
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MainActivity : BaseDaggerActivity<MainActivityBinding, MainViewModel>() {
    @LayoutRes
    override var layoutId: Int = R.layout.main_activity

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        exceptionCatcher { mLog.error("ERROR: $it") }
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        if (mLog.isDebugEnabled) {
            mLog.debug("START ACTIVITY")
        }
    }

    override fun initViewBinding() {
    }

    override fun initViewModelEvents() {
    }

    override fun onCommandEvent(cmd: String, data: Any) {
        when (cmd) {
            MainViewModel.CMD_MOVE_GITHUB ->
                navigator.githubFragment()

            MainViewModel.CMD_MOVE_CUSTOM_UI ->
                navigator.meetingRoomFragment()
        }
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