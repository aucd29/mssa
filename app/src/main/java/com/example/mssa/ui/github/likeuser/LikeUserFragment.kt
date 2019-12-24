package com.example.mssa.ui.github.likeuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.scope.FragmentScope
import brigitte.observe
import brigitte.widget.ITabFocus
import brigitte.widget.observeTabFocus
import com.example.mssa.R
import com.example.mssa.databinding.LikeUserFragmentBinding
import com.example.mssa.ui.github.GithubTabViewModel
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class LikeUserFragment @Inject constructor(
): BaseDaggerFragment<LikeUserFragmentBinding, LikeUserViewModel>(), ITabFocus {
    override val layoutId = R.layout.like_user_fragment

    private val mTabViewModel by activityInject<GithubTabViewModel>()

    override fun initViewBinding() {
    }

    override fun initViewModelEvents() {
        observeTabFocus(mTabViewModel.tabLive, this, R.string.main_tab_like_user)
        mViewModel.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            it.getInt(K_PAGE)?.let { page ->

                if (mLog.isDebugEnabled) {
                    mLog.debug("LOAD STATE PAGE : $page")
                }

                mViewModel.pageValue = page
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (mLog.isDebugEnabled) {
            mLog.debug("SAVE STATE PAGE : ${mViewModel.pageValue}")
        }

        outState.putInt(K_PAGE, mViewModel.pageValue)
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

        mViewModel.init()
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