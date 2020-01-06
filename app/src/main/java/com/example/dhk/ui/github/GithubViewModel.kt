package com.example.dhk.ui.github

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import brigitte.isNetworkConntected
import brigitte.toggle
import brigitte.viewmodel.CommandEventViewModel
import com.example.dhk.R
import com.example.dhk.model.remote.GithubSearchService
import com.example.dhk.model.remote.github.User
import com.example.dhk.model.remote.github.Users
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
 */

class GithubViewModel @Inject constructor(
    app: Application,
    val searchApi: GithubSearchService
    ) : CommandEventViewModel(app), LifecycleEventObserver {
    private val mDp    = CompositeDisposable()

    val offscreenLimit = ObservableInt(2)
    val searchKeyword  = MutableLiveData<String>("android")
    val editorAction   = ObservableField<(String?) -> Boolean>()

    var totalValue = 0
    var pageValue  = 1

    val viewIsSearching     = ObservableBoolean(false)
    var searchList          = MutableLiveData<List<User>>()

    init {
        editorAction.set {
            // 검색 버튼을 선택시에는 페이지를 초기화 한다.
            pageValue = 1
            searchUser(pageValue, it)

            true
        }
    }

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            ITN_SEARCH -> {
                // 검색 버튼을 선택시에는 페이지를 초기화 한다.
                pageValue = 1
                searchUser(pageValue, searchKeyword.value)
            }
            ITN_CLEAR  -> searchKeyword.value = ""
        }
    }

    fun searchNext() {
        searchUser(pageValue + 1, searchKeyword.value)
    }

    private fun searchUser(page: Int, keyword: String?) {
        if (mLog.isDebugEnabled) {
            mLog.debug("SEARCH KEYWORD $keyword")
        }

        if (!app.isNetworkConntected()) {
            snackbar(R.string.network_invalid_connectivity)
            return
        }

        val viewProgress = if (page == 1) {
            viewIsSearching
        } else {
            null
        }

        viewProgress?.toggle()

        if (keyword.isNullOrEmpty()) {
            viewProgress?.toggle()
            toast(string(R.string.search_pls_input_search_keyword))
            return
        }

        mDp.add(searchApi.users(keyword, page.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewProgress?.toggle()

                if (it.incomplete_results) {
                    if (mLog.isDebugEnabled) {
                        mLog.debug("INCOMPLETE RESULTS ($keyword)")
                    }

                    toast("INCOMPLETE")

                    return@subscribe
                }

                totalValue = it.total_count

                if (mLog.isDebugEnabled) {
                    mLog.debug("SEARCHED LIST = ${it.items.size}")
                }

//                // 로딩 시 이미 체크되어 있는 항목 설정 하기
//                it.items.forEach {
//                    if (mDibsMap.containsKey(it.id)) {
//                        it.enableDibs()
//                    }
//                }

                // 올바르게 데이터를 가져왔을 경우에만 페이지를 증가 시킨다.


//                if (page == 1) {
                    searchList.value = it.items
//                    items.set(it.items)
//                } else {
//                    items.get()?.toMutableList()?.let { currentItems ->
//                        currentItems.addAll(it.items)
//                        items.set(currentItems)
//                    }
//                }

                ++pageValue
            }, {
                viewProgress?.toggle()

                errorLog(it, mLog)
            }))
    }

    @VisibleForTesting
    fun isDisposed() =
        mDp.isDisposed

//    @VisibleForTesting
//    fun dibsMapCount() =
//        mDibsMap.size

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // LifecycleEventObserver
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> mDp.dispose()
        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(GithubViewModel::class.java)

        const val ITN_SEARCH = "search"
        const val ITN_CLEAR  = "clear"

        const val CMD_DIBS   = "dibs"
    }
}