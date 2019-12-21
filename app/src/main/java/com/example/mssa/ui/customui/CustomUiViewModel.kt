package com.example.mssa.ui.customui

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import brigitte.RecyclerAdapter
import brigitte.RecyclerViewModel
import com.example.mssa.R
import com.example.mssa.common.Config
import com.example.mssa.model.local.meetingroom.AvailableMeetingRoom
import com.example.mssa.model.local.meetingroom.ReservationInfo
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class CustomUiViewModel @Inject constructor(
    app: Application,
    val config: Config
) : RecyclerViewModel<AvailableMeetingRoom>(app) {

    val availableCount = MutableLiveData<Int>()
    val dateString = MutableLiveData<String>()

    val reservationItems   = ObservableField<List<ReservationInfo>>()
    val reservationAdapter = ObservableField<RecyclerAdapter<ReservationInfo>>()

    init {
        availableMeetingRoom()
        reservationRoom()
    }

    fun availableMeetingRoom() {
        initAdapter(R.layout.current_meeting_room_item)

        items.set(arrayListOf(
            AvailableMeetingRoom(1, "대회의실"),
            AvailableMeetingRoom(2, "회의실2"),
            AvailableMeetingRoom(3, "회의실3")
        ))

        availableCount.postValue(3)
    }

    fun reservationRoom() {

    }

}