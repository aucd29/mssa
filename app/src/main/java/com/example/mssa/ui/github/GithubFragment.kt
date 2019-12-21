package com.example.mssa.ui.github

import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.scope.FragmentScope
import com.example.mssa.R
import com.example.mssa.databinding.GithubFragmentBinding
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class GithubFragment @Inject constructor(
): BaseDaggerFragment<GithubFragmentBinding, GithubViewModel>() {
    override val layoutId = R.layout.github_fragment

    @Inject lateinit var mAdapter: GithubPageAdapter

    private val mTabViewModel by activityInject<GithubTabViewModel>()

    override fun bindViewModel() {
        super.bindViewModel()

        mBinding.tabModel = mTabViewModel
    }

    override fun initViewBinding() = mBinding.run {
        with (githubViewPager) {
            adapter = mAdapter
            githubTabs.setupWithViewPager(this)
        }
    }

    override fun initViewModelEvents() {
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MODULE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @dagger.Module
    abstract class Module {
        @FragmentScope
        @ContributesAndroidInjector(modules = [GithubFragmentModule::class])
        abstract fun contributeGithubFragmentInjector(): GithubFragment
    }

    @dagger.Module
    abstract class GithubFragmentModule {
        @Binds
        abstract fun bindGithubFragment(fragment: GithubFragment): Fragment

        @dagger.Module
        companion object {
        }
    }
}