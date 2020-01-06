package com.example.dhk.common

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import brigitte.systemService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020-1-6 <p/>
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