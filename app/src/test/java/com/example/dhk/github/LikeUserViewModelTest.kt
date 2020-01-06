package com.example.dhk.github

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019-12-24 <p/>
 */

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import brigitte.ICommandEventAware
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import brigitte.shield.*
import brigitte.string
import com.example.dhk.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.slf4j.LoggerFactory
import com.example.dhk.model.local.LocalDb
import com.example.dhk.model.local.room.Dibs
import com.example.dhk.model.local.room.DibsDao
import com.example.dhk.model.remote.GithubSearchService
import com.example.dhk.ui.github.likeuser.LikeUserViewModel
import com.example.dhk.ui.github.search.SearchViewModel
import io.reactivex.Flowable
import io.reactivex.Single


/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019-12-24 <p/>
 */
@RunWith(RobolectricTestRunner::class)
class LikeUserViewModelTest: BaseRoboViewModelTest<LikeUserViewModel>() {
    @Mock lateinit var db: LocalDb
    @Mock lateinit var dibsDao: DibsDao

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = LikeUserViewModel(app, db)
    }


    @Test
    fun initCountTest() {
        mockReactiveX()

        db.dibsDao().mockReturn(dibsDao)
        dibsDao.count().mockReturn(Single.just(3))
        dibsDao.select().mockReturn(Flowable.just(arrayListOf()))

        viewmodel.init()
        viewmodel.total.get().assertEquals(3)
        viewmodel.items.get()?.size.asserteq(0)
    }

    @Test
    fun checkDibsTest() {
        mockReactiveX()

        val dibs = mock(Dibs::class.java)
        val anim = mock(ObservableField::class.java)

        dibs.anim.mockReturn(anim)

        viewmodel.apply {
            mockObserver<Pair<String, Any>>(commandEvent).apply {
                viewmodel.command(LikeUserViewModel.CMD_DIBS, dibs)
                dibs.anim.get().assertNull()

                verifyChanged(
                    LikeUserViewModel.CMD_DIBS to dibs)
            }
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
        private val mLog = LoggerFactory.getLogger(LikeUserViewModelTest::class.java)
    }
}