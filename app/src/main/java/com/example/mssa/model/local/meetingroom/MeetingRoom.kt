package com.example.mssa.model.local.meetingroom

import brigitte.*
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-21 <p/>
 */

data class MeetingRoom(
    val name: String,
    val location: String,
    val reservations: List<ReservationTime>
)

data class ReservationTime(
    val startTime: String,
    val endTime: String
)

////////////////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////////////////

data class ConvertMeetingRoom(
    val id: Int,
    var name: String,
    var location: String,
    var reservations: ArrayList<ConvertReservationTime>
): IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff) =
        id == (item as ConvertMeetingRoom).id

    override fun contentsSame(item: IRecyclerDiff) =
        name == (item as ConvertMeetingRoom).name &&
        location == item.location &&
        reservations.size == item.reservations.size

    constructor(id: Int, data: MeetingRoom):
        this(id, data.name, data.location, data.reservations.run {

        val convertList = arrayListOf<ConvertReservationTime>()
        this.forEach {
            val startTime = dateConvert(it.startTime)
            val endTime = dateConvert(it.endTime)

            convertList.add(ConvertReservationTime(startTime, endTime))
        }

        convertList
    })

    fun isAvailableRoom(current: Long = System.currentTimeMillis()): Boolean {
        var result = true
        var i = 0

        val c = Calendar.getInstance()
        c.set(c.y, c.m,  c.d, 9, 0, 0)
        if (current < c.timeInMillis) {
            // 9시 이전
            if (mLog.isDebugEnabled) {
                mLog.debug("RESULT false")
            }

            return false
        }
        c.set(c.y, c.m,  c.d, 18, 0, 0)
        if (current > c.timeInMillis) {
            // 18시 이후
            if (mLog.isDebugEnabled) {
                mLog.debug("RESULT false")
            }

            return false
        }

        while (i < reservations.size) {
            val it = reservations[i]

            if (current >= it.startTime && current <= it.endTime) {
                if (mLog.isDebugEnabled) {
                    mLog.debug("CHECK\n" +
                            "S (${it.startTime.toDateString()})" +
                            "E (${it.endTime.toDateString()})" +
                            "C (${current.toDateString()})")
                }

                result = false
                break
            }

            ++i
        }

        if (mLog.isDebugEnabled) {
            mLog.debug("RESULT $result")
        }

        return result
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(ConvertMeetingRoom::class.java)

        fun dateConvert(date: String): Long {
            val hour = date.substring(0, 2).toInt()
            val min = date.substring(2, 4).toInt()

            val cal = Calendar.getInstance()
            cal.set(cal.y, cal.m, cal.d, hour, min, 0)

            if (mLog.isDebugEnabled) {
                mLog.debug("CONVERT = ${cal.timeInMillis.toDateString()}")
            }

            return cal.timeInMillis
        }
    }
}

data class ConvertReservationTime(
    val startTime: Long,
    val endTime: Long
)

////////////////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////////////////

data class AvailableMeetingRoom(
    val id: Int,
    val name: String
): IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff) =
        id == (item as AvailableMeetingRoom).id

    override fun contentsSame(item: IRecyclerDiff) =
        name == (item as AvailableMeetingRoom).name
}

//
//data class ReservationInfo(
//    val id: Int,
//    val name: String,
//    val location: String
//): IRecyclerDiff {
//    override fun itemSame(item: IRecyclerDiff) =
//        id == (item as ReservationInfo).id
//
//    override fun contentsSame(item: IRecyclerDiff) =
//        name == (item as ReservationInfo).name
//}