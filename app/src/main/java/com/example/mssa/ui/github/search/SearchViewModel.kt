package com.example.mssa.ui.github.search

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import brigitte.RecyclerViewModel
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import brigitte.toggle
import brigitte.vibrate
import brigitte.widget.viewpager.OffsetDividerItemDecoration
import com.example.mssa.R
import com.example.mssa.model.local.LocalDb
import com.example.mssa.model.local.room.Dibs
import com.example.mssa.model.remote.GithubSearchService
import com.example.mssa.model.remote.github.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

/*
json format
{
  "total_count": 1,
  "incomplete_results": false,
  "items": [
    {
      "login": "aucd29",
      "id": 306378,
      "node_id": "MDQ6VXNlcjMwNjM3OA==",
      "avatar_url": "https://avatars3.githubusercontent.com/u/306378?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/aucd29",
      "html_url": "https://github.com/aucd29",
      "followers_url": "https://api.github.com/users/aucd29/followers",
      "following_url": "https://api.github.com/users/aucd29/following{/other_user}",
      "gists_url": "https://api.github.com/users/aucd29/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/aucd29/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/aucd29/subscriptions",
      "organizations_url": "https://api.github.com/users/aucd29/orgs",
      "repos_url": "https://api.github.com/users/aucd29/repos",
      "events_url": "https://api.github.com/users/aucd29/events{/privacy}",
      "received_events_url": "https://api.github.com/users/aucd29/received_events",
      "type": "User",
      "site_admin": false,
      "score": 692.1837
    }
  ]
}
 */

class SearchViewModel @Inject constructor(
    app: Application,
    val searchApi: GithubSearchService,
    val db: LocalDb
) : RecyclerViewModel<User>(app), LifecycleEventObserver {
    /** 좋아요 연동을 위한 변수 */
    private var mDibsMap = hashMapOf<Int, String?>()
    private val mDp      = CompositeDisposable()

    val searchKeyword  = MutableLiveData<String>("android")
    val editorAction   = ObservableField<(String?) -> Boolean>()
    val itemDecoration = ObservableField(OffsetDividerItemDecoration(app,
            R.drawable.shape_divider_gray,  0, 0))

    val viewIsSearching = ObservableBoolean(false)


    init {
        editorAction.set {
            searchUser(it)

            true
        }

        initAdapter(R.layout.search_item)

        mDp.add(db.dibsDao().selectAll()
            .subscribeOn(Schedulers.io())
            .subscribe({
                it.forEach { dibs -> mDibsMap[dibs.sid] = null }

                if (mLog.isDebugEnabled) {
                    mLog.debug("INIT DIBS MAP COUNT = ${it.size}")
                }
            }, ::errorLog))
    }

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            ITN_SEARCH -> searchUser(searchKeyword.value)
            ITN_CLEAR  -> searchKeyword.value = ""
            CMD_DIBS   -> checkDibs(data as User)
            else -> super.command(cmd, data)
        }
    }

    private fun searchUser(keyword: String?) {
        if (mLog.isDebugEnabled) {
            mLog.debug("SEARCH KEYWORD $keyword")
        }

        viewIsSearching.toggle()

        if (keyword.isNullOrEmpty()) {
            viewIsSearching.toggle()
            toast(string(R.string.search_pls_input_search_keyword))
            return
        }

        mDp.add(searchApi.users(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewIsSearching.toggle()

                if (it.incomplete_results) {
                    if (mLog.isDebugEnabled) {
                        mLog.debug("INCOMPLETE RESULTS ($keyword)")
                    }

                    toast("INCOMPLETE")

                    return@subscribe
                }

                if (mLog.isDebugEnabled) {
                    mLog.debug("SEARCHED LIST = ${it.items.size}")
                }

                // 로딩 시 이미 체크되어 있는 항목 설정 하기
                it.items.forEach {
                    if (mDibsMap.containsKey(it.id)) {
                        it.enableDibs()
                    }
                }

                items.set(it.items)
            }, {
                viewIsSearching.toggle()

                errorLog(it)
            }))
    }

    private fun checkDibs(item: User) {
        if (mLog.isDebugEnabled) {
            mLog.debug("CHECK DIBS ${item.login} - (${item.isEnabled()})")
        }

        app.vibrate(1)

        item.anim.set(ToLargeAlphaAnimParams(5f, endListener = {
            toggleDibsItem(item)
            item.toggleDibs()
        }))
    }

    private fun toggleDibsItem(item: User) {
        if (mDibsMap.containsKey(item.id)) {
            mDibsMap.remove(item.id)
            mDp.add(db.dibsDao().delete(item.id)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (mLog.isDebugEnabled) {
                        mLog.debug("DIBS DELETE OK : ${item.login}(${item.id})")
                    }
                },::errorLog))
        } else {
            mDibsMap[item.id] = null
            mDp.add(db.dibsDao().insert(Dibs(item.id,
                item.login,
                item.avatar_url,
                item.score,
                item.starred_url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (mLog.isDebugEnabled) {
                        mLog.debug("DIBS INSERT OK : ${item.login}(${item.id})")
                    }
                }, ::errorLog))
        }

        if (mLog.isDebugEnabled) {
            mLog.debug("DIBS MAP COUNT : ${mDibsMap.size}")
        }
    }

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

    ////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private val mLog = LoggerFactory.getLogger(SearchViewModel::class.java)

        const val ITN_SEARCH = "search"
        const val ITN_CLEAR  = "clear"

        const val CMD_DIBS   = "dibs"
    }
}