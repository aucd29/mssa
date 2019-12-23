package com.example.mssa.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import brigitte.m
import brigitte.y
import brigitte.d
import brigitte.dpToPx
import com.example.mssa.R
import com.example.mssa.model.local.meetingroom.ConvertReservationTime
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-23 <p/>
 */

class ReservationTimeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    var reservationTime: List<ConvertReservationTime>? = null
    val currentTime = System.currentTimeMillis()
    var offsetTime = 0

    private val mPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.deep_sky_blue)
    }

    private val mPaint2 = Paint().apply {
        color = context.getColor(R.color.black_two)
    }

    init {
        val cal = Calendar.getInstance()
        cal.set(cal.y, cal.m, cal.d, 9, 0, 0)

        offsetTime = ((currentTime - cal.timeInMillis) / 1_800_000).toInt()

        if (mLog.isDebugEnabled) {
            mLog.debug("OFFSET TIME : $offsetTime")
        }
    }

    fun reservationTime(reservationTime: List<ConvertReservationTime>) {
        reservationTime.forEach {
            it.startTime
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val cellWidth = width / 20  // 9 ~ 18 시 30분간
        val left = offsetTime * cellWidth
        canvas?.drawRect(Rect(left, 0, width, height), mPaint)

        (1..20).forEach {
            var l =  it * cellWidth

            if (mLog.isDebugEnabled) {
                mLog.debug("DRAW $l, 0, ${l + 1.dpToPx(context)}, $height")
            }

            canvas?.drawRect(Rect(l, 0,  l + 1.dpToPx(context), height / 2), mPaint2)
        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(ReservationTimeView::class.java)
    }
}