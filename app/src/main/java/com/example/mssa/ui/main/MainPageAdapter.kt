package com.example.mssa.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import brigitte.string
import brigitte.widget.pageradapter.FragmentPagerAdapter
import com.example.mssa.R
import com.example.mssa.ui.customui.CustomUiFragment
import com.example.mssa.ui.search.SearchFragment
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

private val TAB_TITLES = arrayOf(
    R.string.main_tab_search,
    R.string.main_tab_custom_ui
)

class MainPageAdapter @Inject constructor(
    private val context: Context,
    @param:Named("activityFragmentManager") val fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    @Inject lateinit var mSearchFragment: SearchFragment
    @Inject lateinit var mCustomUiFragment: CustomUiFragment

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0    -> mSearchFragment
            else -> mCustomUiFragment
        }
    }

    override fun getPageTitle(position: Int) = context.string(TAB_TITLES[position])
    override fun getCount() = TAB_TITLES.size
}