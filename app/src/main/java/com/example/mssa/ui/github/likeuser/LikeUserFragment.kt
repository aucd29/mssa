package com.example.mssa.ui.github.likeuser

import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.scope.FragmentScope
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
    }


//    fun likeUserFragment() {
//        if (mLog.isInfoEnabled) {
//            mLog.info("LikeUserFragment")
//        }
//
//        manager.show<LikeUserFragment>(FragmentParams(CONTAINER))
//    }


    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ITabFocus
    //
    ////////////////////////////////////////////////////////////////////////////////////

    override fun onTabFocusIn() {
        if (mLog.isDebugEnabled) {
            mLog.debug("FOCUS IN (LIKE USER)")
        }
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
    }
}