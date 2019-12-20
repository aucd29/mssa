package com.example.mssa.ui.customui

import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.scope.FragmentScope
import com.example.mssa.R
import com.example.mssa.databinding.CustomUiFragmentBinding
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class CustomUiFragment @Inject constructor(
): BaseDaggerFragment<CustomUiFragmentBinding, CustomUiViewModel>() {
    override val layoutId = R.layout.custom_ui_fragment

    override fun initViewBinding() {
    }

    override fun initViewModelEvents() {
    }

//    fun customUiFragment() {
//        if (mLog.isInfoEnabled) {
//            mLog.info("CustomUiFragment")
//        }
//
//        manager.show<CustomUiFragment>(FragmentParams(CONTAINER))
//    }
//

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MODULE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @FragmentScope
        @ContributesAndroidInjector(modules = [CustomUiFragmentModule::class])
        abstract fun contributeCustomUiFragmentInjector(): CustomUiFragment
    }

    @dagger.Module
    abstract class CustomUiFragmentModule {
        @Binds
        abstract fun bindCustomUiFragment(fragment: CustomUiFragment): Fragment

        @dagger.Module
        companion object {
        }
    }
}