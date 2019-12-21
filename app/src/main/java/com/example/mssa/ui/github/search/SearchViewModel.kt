package com.example.mssa.ui.github.search

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import brigitte.RecyclerViewModel
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import brigitte.toggle
import brigitte.vibrate
import brigitte.viewmodel.CommandEventViewModel
import brigitte.widget.viewpager.OffsetDividerItemDecoration
import com.example.mssa.R
import com.example.mssa.model.local.LocalDb
import com.example.mssa.model.local.table.Dibs
import com.example.mssa.model.remote.GithubSearchService
import com.example.mssa.model.remote.github.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import java.lang.Exception
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
) : RecyclerViewModel<User>(app) {
    val searchKeyword = MutableLiveData<String>("android")
    val editorAction  = ObservableField<(String?) -> Boolean>()
    val itemDecoration = ObservableField(
        OffsetDividerItemDecoration(app,
            R.drawable.shape_divider_gray,  0, 0)
    )

    val dibsList        = MutableLiveData<ArrayList<User>>()

    val viewIsSearching = ObservableBoolean(false)
    val dp = CompositeDisposable()

    init {
        editorAction.set {
            searchUser(it)

            true
        }

        initAdapter(R.layout.search_item)
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

        dp.add(searchApi.users(keyword)
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

//        if (item.isEnabled()) {
//            app.vibrate(longArrayOf(0, 1, 100, 1), 1)
//        } else {
            app.vibrate(1)
//        }

        item.anim.set(ToLargeAlphaAnimParams(5f, endListener = {
            dibsList.value = toggleDibsItem(item)
            item.toggleDibs()

            // INSERT DB
            db.dibsDao().insert(Dibs(item.login,
                item.avatar_url,
                item.score,
                item.starred_url))

        }))
    }

    private fun toggleDibsItem(item: User) =
        dibsList.value?.let {
            if (mLog.isDebugEnabled) {
                mLog.debug("DIBS LIST COUNT : ${it.size}")
            }

            val f = it.find { f -> f.login == item.login }
            if (f != null) {
                if (mLog.isDebugEnabled) {
                    mLog.debug("REMOVE DIBS")
                }

                it.remove(f)
            } else {
                if (mLog.isDebugEnabled) {
                    mLog.debug("ADD DIBS")
                }

                it.add(item)
            }

            it
        }

    companion object {
        private val mLog = LoggerFactory.getLogger(SearchViewModel::class.java)

        const val ITN_SEARCH = "search"
        const val ITN_CLEAR  = "clear"

        const val CMD_DIBS   = "dibs"
    }
}