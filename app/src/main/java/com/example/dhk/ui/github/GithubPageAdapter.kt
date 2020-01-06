package com.example.dhk.ui.github

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import brigitte.string
import brigitte.widget.pageradapter.FragmentPagerAdapter
import com.example.dhk.R
import com.example.dhk.ui.github.likeuser.LikeUserFragment
import com.example.dhk.ui.github.search.SearchFragment
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
 */

private val TAB_TITLES = arrayOf(
    R.string.main_tab_api,
    R.string.main_tab_local
)

class GithubPageAdapter @Inject constructor(
    private val context: Context,
    val fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        if (mLog.isDebugEnabled) {
            mLog.debug("ADAPTER ITEM = $position")
        }

        return when(position) {
            0    -> SearchFragment.create()
            else -> LikeUserFragment.create()
        }
    }

    override fun getPageTitle(position: Int) = context.string(TAB_TITLES[position])
    override fun getCount() = TAB_TITLES.size

    companion object {
        private val mLog = LoggerFactory.getLogger(GithubPageAdapter::class.java)

        fun create(context: Context, manager: FragmentManager) =
            GithubPageAdapter(context, manager)
    }
}