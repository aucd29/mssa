package com.example.mssa.ui.github.likeuser

import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.scope.FragmentScope
import com.example.mssa.R
import com.example.mssa.databinding.LikeUserFragmentBinding
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class LikeUserFragment @Inject constructor(
): BaseDaggerFragment<LikeUserFragmentBinding, LikeUserViewModel>() {
    override val layoutId = R.layout.like_user_fragment

    override fun initViewBinding() {
    }

    override fun initViewModelEvents() {
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
}