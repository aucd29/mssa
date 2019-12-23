package com.example.mssa.common

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import brigitte.jsonParse
import brigitte.systemService
import com.example.mssa.model.local.meetingroom.MeetingRoom
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-21 <p/>
 */

@Singleton
class Config @Inject constructor(val context: Context) {
    val SCREEN = Point()

    init {
        //
        // W / H
        //
        val windowManager = context.systemService<WindowManager>()
        windowManager?.defaultDisplay?.getSize(SCREEN)


    }
}