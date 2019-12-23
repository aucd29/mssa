package com.example.mssa.meetingroom

import brigitte.d
import brigitte.m
import brigitte.toDateString
import brigitte.y
import com.example.mssa.model.local.meetingroom.ConvertMeetingRoom
import com.example.mssa.model.local.meetingroom.MeetingRoom
import com.example.mssa.model.local.meetingroom.ReservationTime
import org.junit.Test
import java.util.*

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-23 <p/>
 */

class TimeTest {
    @Test
    fun dateConvert() {
        val dummy = MeetingRoom("name", "location",
            arrayListOf(ReservationTime("0900", "1100"),
                ReservationTime("1100", "1300"),
                ReservationTime("1400", "1630"),
                ReservationTime("1700", "1800")))

        val result = ConvertMeetingRoom(1, dummy)

        // CURRENT TIME
        result.isAvailableRoom()

        // 1301
        val c = Calendar.getInstance()
        c.set(c.y, c.m,  c.d, 13, 1, 0)
        println("date 1301 : ${c.timeInMillis.toDateString()}")
        result.isAvailableRoom(c.timeInMillis)

        c.set(c.y, c.m,  c.d, 13, 30, 0)
        println("date 1330 : ${c.timeInMillis.toDateString()}")
        result.isAvailableRoom(c.timeInMillis)

        c.set(c.y, c.m,  c.d, 16, 40, 0)
        println("date 1640 : ${c.timeInMillis.toDateString()}")
        result.isAvailableRoom(c.timeInMillis)

        c.set(c.y, c.m,  c.d, 18, 40, 0)
        println("date 1840 : ${c.timeInMillis.toDateString()}")
        result.isAvailableRoom(c.timeInMillis)
    }
}