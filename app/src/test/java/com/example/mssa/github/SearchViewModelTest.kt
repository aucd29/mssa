package com.example.mssa.github

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-24 <p/>
 */

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import brigitte.ICommandEventAware
import brigitte.shield.*
import brigitte.string
import com.example.mssa.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.slf4j.LoggerFactory
import com.example.mssa.model.local.LocalDb
import com.example.mssa.model.local.room.Dibs
import com.example.mssa.model.local.room.DibsDao
import com.example.mssa.model.remote.GithubSearchService
import com.example.mssa.ui.github.search.SearchViewModel
import io.reactivex.Single


/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-24 <p/>
 */
@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest: BaseRoboViewModelTest<SearchViewModel>() {
    @Mock lateinit var searchApi: GithubSearchService
    @Mock lateinit var db: LocalDb
    @Mock lateinit var dibsDao: DibsDao

    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = SearchViewModel(app, searchApi, db)
    }

    @Test
    fun initTest0() {
        viewmodel.apply {
            db.dibsDao().mockReturn(dibsDao)
            db.dibsDao().selectAll().mockReturn(Single.just(arrayListOf<Dibs>()))

            init()
            adapter.get().assertNotNull()
            dibsMapCount().asserteq(0)
        }
    }

    @Test
    fun initTest2() {
        viewmodel.apply {
            db.dibsDao().mockReturn(dibsDao)
            db.dibsDao().selectAll().mockReturn(Single.just(arrayListOf(
                Dibs(1,"1","1",0.0f,"1"),
                Dibs(2,"2","2",0.0f,"2")
            )))

            init()
            adapter.get().assertNotNull()
            dibsMapCount().asserteq(2)
        }
    }

    @Test
    fun networkDisableTest() {
        mockNetwork()
        mockDisableNetwork()
        viewmodel.apply {
            mockObserver<Pair<String, Any>>(commandEvent).apply {
                command(SearchViewModel.ITN_SEARCH)

                verifyChanged(
                    ICommandEventAware.CMD_SNACKBAR to app.string(R.string.network_invalid_connectivity))
            }
        }
    }

    @Test
    fun networkInputSearchKeywordTest() {
        mockNetwork()
        mockEnableNetwork()
        viewmodel.apply {
            searchKeyword.value = ""
            mockObserver<Pair<String, Any>>(commandEvent).apply {
                command(SearchViewModel.ITN_SEARCH)

                verifyChanged(
                    ICommandEventAware.CMD_TOAST to app.string(R.string.search_pls_input_search_keyword))
            }
        }
    }

    @Test
    fun searchKeywordTest() {
        viewmodel.apply {
            searchKeyword.value = "hello world"

            mockObserver<String>(searchKeyword).apply {
                verifyChanged("hello world")
                println("search keyword = ${searchKeyword.value}")
            }
        }
    }

    @Test
    fun clearSearchKeywordTest() {
        viewmodel.apply {
            command(SearchViewModel.ITN_CLEAR)

            mockObserver<String>(searchKeyword).apply {
                verifyChanged("")
                println("clear search keyword = ${searchKeyword.value}")
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
        private val mLog = LoggerFactory.getLogger(SearchViewModelTest::class.java)
    }
}