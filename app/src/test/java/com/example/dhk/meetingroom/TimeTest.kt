package com.example.dhk.meetingroom

import brigitte.d
import brigitte.m
import brigitte.toDateString
import brigitte.y
import com.example.dhk.model.local.meetingroom.ConvertMeetingRoom
import com.example.dhk.model.local.meetingroom.MeetingRoom
import com.example.dhk.model.local.meetingroom.ReservationTime
import junit.framework.TestCase.*
import org.junit.Test
import java.util.*

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019-12-23 <p/>
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
        assertTrue(result.isAvailableRoom(c.timeInMillis))

        c.set(c.y, c.m,  c.d, 13, 30, 0)
        println("date 1330 : ${c.timeInMillis.toDateString()}")
        assertTrue(result.isAvailableRoom(c.timeInMillis))

        c.set(c.y, c.m,  c.d, 16, 40, 0)
        println("date 1640 : ${c.timeInMillis.toDateString()}")
        assertTrue(result.isAvailableRoom(c.timeInMillis))

        c.set(c.y, c.m,  c.d, 18, 40, 0)
        println("date 1840 : ${c.timeInMillis.toDateString()}")
        assertFalse(result.isAvailableRoom(c.timeInMillis))
    }

    data class Time(
        val star: Long, val end: Long
    )

    private fun todayTime(hour: Int, min: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(cal.y, cal.m, cal.d, hour, min, 0)

        return cal.timeInMillis
    }

    fun drawingIdx(current: Long):Map<Int, String?> {
        val list = arrayListOf<Time>()

        var start = todayTime(9, 0)
        var end = todayTime(11, 0)
        list.add(Time(start, end))

        start = todayTime(11, 0)
        end = todayTime(13, 0)
        list.add(Time(start, end))

        start = todayTime(14, 0)
        end = todayTime(16, 30)
        list.add(Time(start, end))

        start = todayTime(17, 0)
        end = todayTime(18, 0)
        list.add(Time(start, end))

        val drawingMap = hashMapOf<Int, String?>()
        val divTime = 1800000 // 30min

        var i = 0
        while (i < list.size) {
            val it = list[i]
            val s  = it.star - current
            val e  = it.end - current

            val sidx = s / divTime
            val eidx = e / divTime

            //println("s: $sidx ~ e: $eidx")

            (sidx..eidx).forEach {
                drawingMap.put(it.toInt(), null)
            }

            ++i
        }

        //drawingMap.forEach { println("idx: ${it.key}") }
        val time9 = todayTime(9, 0)

        println("current : $current, 9am: $time9")
        val sliceTime = (current - time9) / divTime

        val it = drawingMap.iterator()
        while (it.hasNext()) {
            val tm = it.next()
            if (tm.key < sliceTime) {
                it.remove()
            }
        }

        println("size: ${drawingMap.size}")

        return drawingMap
    }

    @Test
    fun calTime() {
        assertEquals(drawingIdx(todayTime(10, 30)).size, 13)
//        assertEquals(drawingIdx(todayTime(9, 30)).size, 17)
//        assertEquals(drawingIdx(todayTime(9, 0)).size, 18)
    }

}