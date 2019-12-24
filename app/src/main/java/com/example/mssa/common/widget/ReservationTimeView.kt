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
        color = context.getColor(R.color.black_two)
    }

    init {
        // 기준 시간은 9시로
        val cal = Calendar.getInstance()
        cal.set(cal.y, cal.m, cal.d, 9, 0, 0)
        mTime9 = cal.timeInMillis

        mOffsetTime = ((mCurrentTime - mTime9) / 1_800_000).toInt()

        if (mLog.isDebugEnabled) {
            mLog.debug("OFFSET TIME : $mOffsetTime")
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

            if (mLog.isDebugEnabled) {
                mLog.debug("S-IDX: $sIdx, E-IDX: $eIdx")
            }

            var index = sIdx.toInt()
            while (index < eIdx) {
                if (mLog.isDebugEnabled) {
                    mLog.debug("DRAW IDX : $index")
                }

                mDrawingMap[index] = null

                ++index
            }

            ++i
        }

        // 삭제해야할 부분 제거하고
        val it = mDrawingMap.iterator()
        while (it.hasNext()) {
            val tm = it.next()
            if (tm.key < mOffsetTime) {
                it.remove()
            }
        }

        if (mLog.isDebugEnabled) {
            mLog.debug("DRAWING INDEX : ${mDrawingMap.size}")
        }

        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val cellWidth = width / 19  // 9 ~ 18 시 30분간

        mDrawingMap.forEach {
            val start = it.key * cellWidth
            val end = start + cellWidth

            canvas?.drawRect(Rect(start, 0, end, height), mPaint)
        }

        //val left = mOffsetTime * cellWidth
        //canvas?.drawRect(Rect(left, 0, width, height), mPaint)

        (1..19).forEach {
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