package com.example.dhk.ui.github

import android.os.Bundle
import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.SCOPE_ACTIVITY
import brigitte.di.dagger.scope.FragmentScope
import com.example.dhk.R
import com.example.dhk.databinding.GithubFragmentBinding
import com.example.dhk.ui.github.search.SearchFragment
import dagger.Binds
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
 */

class GithubFragment @Inject constructor(
): BaseDaggerFragment<GithubFragmentBinding, GithubViewModel>() {
    override val layoutId = R.layout.github_fragment

    init {
        mViewModelScope = SCOPE_ACTIVITY
    }

//    @Inject lateinit var mAdapter: GithubPageAdapter

    private val mTabViewModel by activityInject<GithubTabViewModel>()

    override fun bindViewModel() {
        super.bindViewModel()

        mBinding.tabModel = mTabViewModel
    }

    override fun initViewBinding() = mBinding.run {
        with (githubViewPager) {
            adapter = GithubPageAdapter.create(requireContext(), childFragmentManager)
            githubTabs.setupWithViewPager(this)
        }
    }

    override fun initViewModelEvents() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mViewModel.let {
            it.searchKeyword.value?.let {
                outState.putString(SearchFragment.STS_KEYWORD, it)
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            it.getString(SearchFragment.STS_KEYWORD)?.let {
                mViewModel.searchKeyword.value = it
            }
        }
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
            @JvmStatic
            @Provides
            @Named("githubFragmentManager")
            fun provideGithubFragmentManager(fragment: Fragment) =
                fragment.childFragmentManager
        }
    }
}