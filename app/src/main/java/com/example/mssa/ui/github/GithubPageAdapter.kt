package com.example.mssa.ui.github

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import brigitte.string
import brigitte.widget.pageradapter.FragmentPagerAdapter
import com.example.mssa.R
import com.example.mssa.ui.github.likeuser.LikeUserFragment
import com.example.mssa.ui.github.search.SearchFragment
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

private val TAB_TITLES = arrayOf(
    R.string.main_tab_search,
    R.string.main_tab_like_user
)

class GithubPageAdapter @Inject constructor(
    private val context: Context,
    @param:Named("githubFragmentManager") val fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    @Inject lateinit var mSearchFragment: SearchFragment
    @Inject lateinit var mLikeUserFragment: LikeUserFragment

    override fun getItem(position: Int): Fragment {
        if (mLog.isDebugEnabled) {
            mLog.debug("ADAPTER ITEM = $position")
        }

        return when(position) {
            0    -> mSearchFragment
            else -> mLikeUserFragment
        }
    }

    override fun getPageTitle(position: Int) = context.string(TAB_TITLES[position])
    override fun getCount() = TAB_TITLES.size

    companion object {
        private val mLog = LoggerFactory.getLogger(GithubPageAdapter::class.java)
    }
}