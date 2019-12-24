package com.example.mssa.ui.github

import android.app.Application
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class GithubViewModel @Inject constructor(
) : ViewModel() {
    val offscreenLimit = ObservableInt(2)
}