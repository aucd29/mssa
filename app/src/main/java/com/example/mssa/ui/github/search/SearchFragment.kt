package com.example.mssa.ui.github.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.scope.FragmentScope
import brigitte.widget.ITabFocus
import brigitte.widget.observeTabFocus
import com.example.mssa.R
import com.example.mssa.databinding.SearchFragmentBinding
import com.example.mssa.ui.github.GithubTabViewModel
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class SearchFragment @Inject constructor(
): BaseDaggerFragment<SearchFragmentBinding, SearchViewModel>(), ITabFocus {
    override val layoutId = R.layout.search_fragment

    private val mTabViewModel by activityInject<GithubTabViewModel>()

    override fun initViewBinding() = mBinding.run {

    }

    override fun initViewModelEvents() {
        mViewModel.init()
        observeTabFocus(mTabViewModel.tabLive, this, R.string.main_tab_search)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            it.getString(STS_KEYWORD)?.let {
                mViewModel.searchKeyword.value = it
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mViewModel.let {
            it.searchKeyword.value?.let {
                outState.putString(STS_KEYWORD, it)
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
            mLog.debug("FOCUS IN (SEARCH)")
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
        @ContributesAndroidInjector(modules = [SearchFragmentModule::class])
        abstract fun contributeSearchFragmentInjector(): SearchFragment
    }

    @dagger.Module
    abstract class SearchFragmentModule {
        @Binds
        abstract fun bindSearchFragment(fragment: SearchFragment): Fragment

        @dagger.Module
        companion object {
        }
    }

    companion object {
        private val mLog = LoggerFactory.getLogger(SearchFragment::class.java)

        const val STS_KEYWORD = "search-keyword"
    }

}