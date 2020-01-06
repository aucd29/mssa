package com.example.dhk.ui.github.search

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import brigitte.*
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import brigitte.widget.viewpager.OffsetDividerItemDecoration
import com.example.dhk.R
import com.example.dhk.model.local.LocalDb
import com.example.dhk.model.local.room.Dibs
import com.example.dhk.model.remote.GithubSearchService
import com.example.dhk.model.remote.github.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
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
    val db: LocalDb
) : RecyclerViewModel<User>(app), LifecycleEventObserver {

    private val mDp = CompositeDisposable()

    val itemDecoration = ObservableField(OffsetDividerItemDecoration(app,
            R.drawable.shape_divider_gray,  0, 0))

    val viewMoreSearching = ObservableBoolean(false)
    val mDibsMap = hashMapOf<Int, String?>()

    fun init() {
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

    fun setSearchedListItems(page: Int, list: List<User>) {
        viewMoreSearching.set(false)

        // 로딩 시 이미 체크되어 있는 항목 설정 하기
        list.forEach {
            if (mDibsMap.containsKey(it.id)) {
                it.enableDibs()
            }
        }

        if (page == 1) {
            items.set(list)
        } else {
            items.get()?.toMutableList()?.let { currentItems ->
                currentItems.addAll(list)
                items.set(currentItems)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // COMMAND
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            CMD_DIBS   -> checkDibs(data as User)
            else       -> super.command(cmd, data)
        }
    }

    fun performCheckDibs(id: Int) {
        items.get()?.iterator()?.let {
            while (it.hasNext()) {
                val item = it.next()
                if (item.id == id) {
                    checkDibs(item)
                    break
                }
            }
        }
    }

    private fun checkDibs(item: User) {
        if (mLog.isDebugEnabled) {
            mLog.debug("CHECK DIBS ${item.login} - (${item.isEnabled()})")
        }

        app.vibrate(1)

        item.anim.set(ToLargeAlphaAnimParams(5f, endListener = {
            toggleDibsItem(item)
            item.toggleDibs()
            item.anim.set(null)
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

    @VisibleForTesting
    fun dibsMapCount() =
        mDibsMap.size

    @VisibleForTesting
    fun isDisposed() =
        mDp.isDisposed

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

        const val CMD_MORE   = "more"
        const val CMD_DIBS   = "dibs"
    }
}