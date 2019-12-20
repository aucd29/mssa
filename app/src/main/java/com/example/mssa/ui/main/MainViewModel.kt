package com.example.mssa.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import brigitte.viewmodel.CommandEventViewModel
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class MainViewModel @Inject constructor(
    app: Application
) : CommandEventViewModel(app) {

    companion object {
        const val CMD_MOVE_GITHUB    = "move-github"
        const val CMD_MOVE_CUSTOM_UI = "move-custom-ui"
    }
}