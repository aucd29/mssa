package com.example.mssa.ui.meetingroom

import android.app.Application
import android.graphics.Rect
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import brigitte.*
import brigitte.widget.viewpager.SpaceItemDecoration
import com.example.mssa.R
import com.example.mssa.model.local.meetingroom.AvailableMeetingRoom
import com.example.mssa.model.local.meetingroom.ConvertMeetingRoom
import com.example.mssa.model.local.meetingroom.MeetingRoom
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class MeetingRoomViewModel @Inject constructor(
    app: Application
) : RecyclerViewModel<AvailableMeetingRoom>(app), LifecycleEventObserver {

    private val mDp = CompositeDisposable()

    val availableCount = MutableLiveData<Int>()
    val dateString = ObservableField<String>()

    val itemDecoration = ObservableField(SpaceItemDecoration(
        Rect(0, 0, 5.dpToPx(app), 0)))

    val reservationItems   = ObservableField<List<ConvertMeetingRoom>>()
    val reservationAdapter = ObservableField<RecyclerAdapter<ConvertMeetingRoom>>()

    init {
        loadJson()
    }

    fun loadJson() {
        val format = app.string(R.string.meeting_room_date_format)
        dateString.set(System.currentTimeMillis().toDateString(SimpleDateFormat(
            format, Locale.getDefault())))

        if (mLog.isDebugEnabled) {
            mLog.debug("CURRENT DATE ${dateString.get()}")
        }

        mDp.add(Single.just(app.assets.open("meeting_room_data.json")
            .readBytes())
            .subscribeOn(Schedulers.io())
            .map { it.jsonParse<List<MeetingRoom>>() }
            .map {
                var i = 0
                val convert = arrayListOf<ConvertMeetingRoom>()
                it.forEach { convert.add(ConvertMeetingRoom(++i, it)) }

                convert
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (mLog.isDebugEnabled) {
                    mLog.debug("JSON DATA SIZE : ${it.size}")
                }

                availableMeetingRoom(it)
                reservationRoom(it)
            },::errorLog))
    }

    fun availableMeetingRoom(list: List<ConvertMeetingRoom>) {
        initAdapter(R.layout.meeting_room_item)
        val availableList = arrayListOf<AvailableMeetingRoom>()
        var i = 0

        // test code
        val c = Calendar.getInstance()
        c.set(c.y, c.m,  c.d, 19, 1, 0)

        list.forEach {
            if (it.isAvailableRoom()) {
//            if (it.isAvailableRoom(c.timeInMillis)) {
                availableList.add(AvailableMeetingRoom(i++, it.name))
            }
        }

        availableCount.postValue(i)
        items.set(availableList)
    }

    fun reservationRoom(list: List<ConvertMeetingRoom>) {
        reservationAdapter.set(RecyclerAdapter(R.layout.meeting_room_reservation_item))
        reservationAdapter.get()?.viewModel = this

        reservationItems.set(list)
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

    companion object {
        private val mLog = LoggerFactory.getLogger(MeetingRoomViewModel::class.java)
    }
}