package com.example.dhk.ui.github.likeuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.SCOPE_ACTIVITY
import brigitte.di.dagger.scope.FragmentScope
import brigitte.widget.ITabFocus
import brigitte.widget.observeTabFocus
import com.example.dhk.R
import com.example.dhk.databinding.LikeUserFragmentBinding
import com.example.dhk.model.local.room.Dibs
import com.example.dhk.ui.github.GithubTabViewModel
import com.example.dhk.ui.github.GithubViewModel
import com.example.dhk.ui.github.search.SearchViewModel
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
 */

class LikeUserFragment @Inject constructor(
): BaseDaggerFragment<LikeUserFragmentBinding, LikeUserViewModel>(), ITabFocus {
    override val layoutId = R.layout.like_user_fragment

    private val mTabViewModel by activityInject<GithubTabViewModel>()
    private val mSearchViewModel by activityInject<SearchViewModel>()

    override fun initViewBinding() {
    }

    override fun initViewModelEvents() {
        observeTabFocus(mTabViewModel.tabLive, this, R.string.main_tab_local)
        mViewModel.init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (mLog.isDebugEnabled) {
            mLog.debug("SAVE STATE PAGE : ${mViewModel.pageValue}")
        }

        outState.putInt(K_PAGE, mViewModel.pageValue)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            it.getInt(K_PAGE)?.let { page ->

                if (mLog.isDebugEnabled) {
                    mLog.debug("LOAD STATE PAGE : $page")
                }

                mViewModel.pageValue = page
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // command
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onCommandEvent(cmd: String, data: Any) {
        when (cmd) {
            LikeUserViewModel.CMD_DIBS -> {
                val dibs = data as Dibs
                mSearchViewModel.performCheckDibs(dibs.sid)
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ITabFocus
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onTabFocusIn() {
        if (mLog.isDebugEnabled) {
            mLog.debug("FOCUS IN (LIKE USER)")
        }

//        mViewModel.init()
    }

    override fun onTabFocusOut() {
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MODULE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @FragmentScope
        @ContributesAndroidInjector(modules = [LikeUserFragmentModule::class])
        abstract fun contributeLikeUserFragmentInjector(): LikeUserFragment
    }

    @dagger.Module
    abstract class LikeUserFragmentModule {
        @Binds
        abstract fun bindLikeUserFragment(fragment: LikeUserFragment): Fragment

        @dagger.Module
        companion object {
        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(LikeUserFragment::class.java)
        private val K_PAGE = "page"

        fun create() =
            LikeUserFragment()
    }
}