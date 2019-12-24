package com.example.mssa.common.bindingadapter

import androidx.databinding.BindingAdapter
import com.example.mssa.common.widget.ReservationTimeView
import com.example.mssa.model.local.meetingroom.ConvertReservationTime
import org.slf4j.LoggerFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-23 <p/>
 */

object ReservationTimeViewBindingAdapter {
    private val mLog = LoggerFactory.getLogger(ReservationTimeViewBindingAdapter::class.java)

    @JvmStatic
    @BindingAdapter("bindReservationTime")
    fun bindReservation(view: ReservationTimeView, reservationTime: List<ConvertReservationTime>) {
        view.reservationTime(reservationTime)
    }
}