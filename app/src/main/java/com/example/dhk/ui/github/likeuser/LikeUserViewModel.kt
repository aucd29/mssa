package com.example.dhk.ui.github.likeuser

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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
) : RecyclerViewModel<Dibs>(app), LifecycleEventObserver {

    private val mDp = CompositeDisposable()

    val itemDecoration = ObservableField(OffsetDividerItemDecoration(
        app, R.drawable.shape_divider_gray,  0, 0))
    var total = ObservableInt(0)

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

                items.set(it)
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