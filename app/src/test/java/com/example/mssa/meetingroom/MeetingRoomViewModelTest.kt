package com.example.mssa.meetingroom

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-24 <p/>
 */

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.slf4j.LoggerFactory
import brigitte.shield.*
import brigitte.string
import brigitte.toDateString
import com.example.mssa.R
import com.example.mssa.ui.meetingroom.MeetingRoomViewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-24 <p/>
 */
@RunWith(RobolectricTestRunner::class)
class MeetingRoomViewModelTest: BaseRoboViewModelTest<MeetingRoomViewModel>() {
    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = MeetingRoomViewModel(app)
    }

//    @Test
//    fun dateStringTest() = viewmodel.run {
//        val format = app.string(R.string.meeting_room_date_format)
//        val target = System.currentTimeMillis().toDateString(
//            SimpleDateFormat(format, Locale.getDefault())
//        )
//
//        dateString.get().asserteq(target)
//    }

    @Test
    fun loadTest() {
        mockReactiveX()

        viewmodel.apply {
            loadJson()
            reservationItems.get()?.size.asserteq(3)
        }
    }

    @Test
    fun stateChangeTest() {
        val owner = mock(LifecycleOwner::class.java)

        viewmodel.apply {
            var event = Lifecycle.Event.ON_CREATE
            onStateChanged(owner, event)
            isDisposed().assertFalse()
            println("check create ${isDisposed()}")

            event = Lifecycle.Event.ON_PAUSE
            onStateChanged(owner, event)
            isDisposed().assertFalse()
            println("check pause ${isDisposed()}")

            event = Lifecycle.Event.ON_RESUME
            onStateChanged(owner, event)
            isDisposed().assertFalse()
            println("check resume ${isDisposed()}")

            event = Lifecycle.Event.ON_DESTROY
            onStateChanged(owner, event)
            isDisposed().assertTrue()
            println("check destroy ${isDisposed()}")
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOCK
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private val mLog = LoggerFactory.getLogger(MeetingRoomViewModelTest::class.java)
    }
}