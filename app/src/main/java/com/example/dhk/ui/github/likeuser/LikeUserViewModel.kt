package com.example.dhk.ui.github.likeuser

import android.app.Application
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import brigitte.ITextChanged
import brigitte.RecyclerViewModel
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import brigitte.widget.viewpager.OffsetDividerItemDecoration
import com.example.dhk.R
import com.example.dhk.model.local.LocalDb
import com.example.dhk.model.local.room.Dibs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
 */

class LikeUserViewModel @Inject constructor(
    app: Application,
    val db: LocalDb
) : RecyclerViewModel<Dibs>(app), LifecycleEventObserver, ITextChanged {

    private val mDp = CompositeDisposable()

    val itemDecoration = ObservableField(OffsetDividerItemDecoration(
        app, R.drawable.shape_divider_gray,  0, 0))
    var total = ObservableInt(0)
    val searchKeyword  = MutableLiveData<String>("")
    var tempList = listOf<Dibs>()

    init {
        initAdapter(R.layout.like_user_item)
    }

    fun init() {
        if (mLog.isInfoEnabled) {
            mLog.info("LIKE USER $this")
        }

        mDp.add(db.dibsDao().count()
            .subscribeOn(Schedulers.io())
            .subscribe({
                total.set(it)

                if (mLog.isDebugEnabled) {
                    mLog.debug("TOTAL : $it")
                }

                load()
            },::errorLog))
    }

    fun load() {
        if (mLog.isDebugEnabled) {
            mLog.debug("LOAD LOCAL")
        }

        mDp.add(db.dibsDao().select() // 0, page * LIMIT
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (mLog.isDebugEnabled) {
                    mLog.debug("LOCAL SIZE = ${it.size}")
                }

                searchKeyword.value = ""
                items.set(it)
                tempList = it
            }, ::errorLog))
    }

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            CMD_DIBS -> {
                val dibs = data as Dibs
                dibs.anim.set(ToLargeAlphaAnimParams(5f, endListener = {
                    dibs.anim.set(null)
                }, duration = 200))

                super.command(cmd, data)
            }
            else -> super.command(cmd, data)
        }
    }

    fun visibleLocalSearch(str: String, item: Dibs): Int {
        if (mLog.isDebugEnabled) {
            mLog.debug("VISIBLE LOCAL SEARCH $str")
        }

        if (str.isEmpty()) {
            return View.VISIBLE
        }

        return if (item.login.contains(str)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    //
    // onTextChanged
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (mLog.isDebugEnabled) {
            mLog.debug("TEXT CHANGED : $s = $count")
        }

        if (s.isEmpty()) {
            items.set(tempList)

            return
        }

        val searchedList = arrayListOf<Dibs>()

        tempList.forEach {
            if (it.login.contains(s)) {
                searchedList.add(it)
            }
        }

        items.set(searchedList)
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

    @VisibleForTesting
    fun isDisposed() =
        mDp.isDisposed

    ////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private val mLog = LoggerFactory.getLogger(LikeUserViewModel::class.java)

        const val CMD_DIBS = "dibs"
    }


}