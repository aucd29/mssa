package com.example.mssa.ui.github.likeuser

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import brigitte.RecyclerViewModel
import brigitte.widget.viewpager.OffsetDividerItemDecoration
import com.example.mssa.R
import com.example.mssa.model.local.LocalDb
import com.example.mssa.model.local.room.Dibs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class LikeUserViewModel @Inject constructor(
    app: Application,
    val db: LocalDb
) : RecyclerViewModel<Dibs>(app), LifecycleEventObserver {

    private val mDp      = CompositeDisposable()
    private var mPage    = 1
    private val mLimit   = 3

    val itemDecoration = ObservableField(OffsetDividerItemDecoration(
        app, R.drawable.shape_divider_gray,  0, 0))
    var total = ObservableInt(0)

    init {
        initAdapter(R.layout.like_user_item)

        init()
    }

    fun init() {
        mDp.add(db.dibsDao().count()
            .subscribeOn(Schedulers.io())
            .subscribe({
                total.set(it)

                if (mPage > total.get()) {
                    mPage = total.get()
                }

                if (mLog.isDebugEnabled) {
                    mLog.debug("TOTAL : $it, PAGE : $mPage")
                }

                load(mPage)
            },::errorLog))
    }

    fun load(page: Int) {
        if (mLog.isDebugEnabled) {
            mLog.debug("LOAD LIKE USER ($page)")
        }

        // FIXME [aucd29][2019-12-23]
        // FIXME android 특성 상 메모리가 날라가니
        // FIXME 리스트에 데이터를 add 하는 것보다 항상 새로 가져오는 편이 나을듯?
        mDp.add(db.dibsDao().select(0, page * mLimit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (mLog.isDebugEnabled) {
                    mLog.debug("LIKE USER = ${it.size}")
                }

                items.set(it)
            }, ::errorLog))
    }

    override fun command(cmd: String, data: Any) {
        when (cmd) {
            ITN_MORE -> load(++mPage)
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

    ////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private val mLog = LoggerFactory.getLogger(LikeUserViewModel::class.java)

        const val ITN_MORE = "more"
    }
}