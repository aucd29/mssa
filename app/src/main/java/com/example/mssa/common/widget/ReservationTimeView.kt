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

    private val mDivTime     = 1_800_000 // 30min
    private val mDrawingMap  = hashMapOf<Int, String?>()
    private val mCurrentTime = System.currentTimeMillis()
    private var mOffsetTime  = 0
    private var mTime9       = 0L

    private val mPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.deep_sky_blue)
    }

    private val mPaint2 = Paint().apply {
        color = context.getColor(R.color.very_light_blue)
    }

    init {
        // 기준 시간은 9시로
        val cal = Calendar.getInstance()
        cal.set(cal.y, cal.m, cal.d, 9, 0, 0)

        mTime9      = cal.timeInMillis
        mOffsetTime = ((mCurrentTime - mTime9) / 1_800_000).toInt()

        if (mLog.isTraceEnabled) {
            mLog.trace("OFFSET TIME : $mOffsetTime")
        }
    }

    fun reservationTime(reservationTime: List<ConvertReservationTime>) {
        var i = 0
        mDrawingMap.clear()

        // 그려야할 위치 지정하고
        while (i < reservationTime.size) {
            val it = reservationTime[i]
            val s  = it.startTime - mTime9
            val e  = it.endTime   - mTime9

            val sIdx = s / mDivTime
            val eIdx = e / mDivTime

            if (mLog.isTraceEnabled) {
                mLog.trace("S-IDX: $sIdx, E-IDX: $eIdx")
            }

            var index = sIdx.toInt() + 1
            while (index <= eIdx) {
                mDrawingMap[index] = null

                ++index
            }

            ++i
        }

        // 삭제해야 할 부분 제거하고
        val it = mDrawingMap.iterator()
        while (it.hasNext()) {
            val tm = it.next()
            if (tm.key < mOffsetTime) {
                it.remove()
            }
        }

        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val cellWidth = width / WIDTH_DIV  // 9 ~ 18 시 30분간

        mDrawingMap.forEach {
            val start = it.key * cellWidth
            val end = start + cellWidth

            canvas?.drawRect(Rect(start, 0, end, height), mPaint)
        }

        val cw = width / WIDTH_DIV
        var i = 0
        while (i <= WIDTH_DIV) {
            val l =  i * cw

            if (mLog.isTraceEnabled) {
                mLog.trace("DRAW $l, 0, ${l + 1.dpToPx(context)}, $height")
            }

            val top = height / 2
            canvas?.drawRect(Rect(l, top,  l + 1.dpToPx(context), height), mPaint2)

            ++i
        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(ReservationTimeView::class.java)

        private const val WIDTH_DIV = 18
    }
}