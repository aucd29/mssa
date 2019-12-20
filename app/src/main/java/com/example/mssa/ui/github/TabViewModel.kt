package com.example.mssa.ui.github

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import brigitte.TabSelectedCallback
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class TabViewModel @Inject constructor(
) : ViewModel() {
    val tabChangedCallback = ObservableField<TabSelectedCallback>()
    var tabLive = MutableLiveData<TabLayout.Tab?>()

    init {
        tabChangedCallback.set(TabSelectedCallback {
            tabLive.value = it
        })
    }
}