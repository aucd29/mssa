package com.example.mssa.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import brigitte.m
import brigitte.y
import brigitte.d
import brigitte.dpToPx
import com.example.mssa.R
import com.example.mssa.model.local.meetingroom.ConvertReservationTime
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.lang.Exception
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
    private val mDp          = CompositeDisposable()

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
        mDp.add(Single.just(reservationTime)
            .subscribeOn(Schedulers.computation())
            .map(::progressReservationTime)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it) {
                    invalidate()
                }
            },{
                if (mLog.isDebugEnabled) {
                    it.printStackTrace()
                }

                mLog.error("ERROR: ${it.message}")

                Toast.makeText(context, R.string.reservation_time_view_unknown_error, Toast.LENGTH_SHORT).show()
            }))
    }

    private fun progressReservationTime(reservationTime: List<ConvertReservationTime>): Boolean {
        var i = 0
        mDrawingMap.clear()

        try {
            // 그려야할 위치 지정하고
            while (i < reservationTime.size) {
                val it = reservationTime[i]
                val s = it.startTime - mTime9
                val e = it.endTime - mTime9

                val sIdx = s / mDivTime
                val eIdx = e / mDivTime

                if (mLog.isTraceEnabled) {
                    mLog.trace("S-IDX: $sIdx, E-IDX: $eIdx")
                }

                var index = sIdx.toInt() + 1
                while (index <= eIdx) {
                    // 현재 시간을 참조해 이전 시간이면 데이터를 무시
                    if (index < mOffsetTime) {
                        if (mLog.isDebugEnabled) {
                            mLog.debug("IGNORE TIME = $index")
                        }
                    } else {
                        mDrawingMap[index] = null
                    }

                    ++index
                }

                ++i
            }

            // 삭제해야 할 부분 제거하고
//            val it = mDrawingMap.iterator()
//            while (it.hasNext()) {
//                val tm = it.next()
//                if (tm.key < mOffsetTime) {
//                    it.remove()
//                }
//            }
        } catch (e: Exception) {
            if (mLog.isDebugEnabled) {
                e.printStackTrace()
            }

            mLog.error("ERROR: ${e.message}")

            return false
        }

        return true
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val cellWidth = width / WIDTH_DIV  // 9 ~ 18 시 30분간

        mDrawingMap.forEach {
            val start = it.key * cellWidth
            val end = start + cellWidth

            canvas?.drawRect(Rect(start, 0, end, height), mPaint)
        }

        // 위치 파악이 힘들어서  눈금 추가
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