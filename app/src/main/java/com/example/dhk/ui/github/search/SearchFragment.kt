package com.example.dhk.ui.github.search

import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.SCOPE_ACTIVITY
import brigitte.di.dagger.scope.FragmentScope
import brigitte.widget.ITabFocus
import brigitte.widget.observeTabFocus
import com.example.dhk.R
import com.example.dhk.databinding.SearchFragmentBinding
import com.example.dhk.ui.github.GithubTabViewModel
import com.example.dhk.ui.github.GithubViewModel
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
 */

class SearchFragment @Inject constructor(
): BaseDaggerFragment<SearchFragmentBinding, SearchViewModel>(), ITabFocus {
    override val layoutId = R.layout.search_fragment

    init {
        mViewModelScope = SCOPE_ACTIVITY
    }

    private val mTabViewModel by activityInject<GithubTabViewModel>()
    private val mGithubViewModel by activityInject<GithubViewModel>()

    override fun initViewBinding() = mBinding.run {

    }

    override fun initViewModelEvents() {
        mViewModel.init()
        observeTabFocus(mTabViewModel.tabLive, this, R.string.main_tab_api)

        observe(mGithubViewModel.searchList) {
            if (mLog.isDebugEnabled) {
                mLog.debug("SEARCHED LIST = ${it.size}")
            }

            mViewModel.viewMoreSearching.set(false)

            if (mGithubViewModel.pageValue == 1) {
                mViewModel.items.set(it)
            } else {
                mViewModel.items.get()?.toMutableList()?.let { currentItems ->
                    currentItems.addAll(it)
                    mViewModel.items.set(currentItems)
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    //
    // COMMAND
    //
    ////////////////////////////////////////////////////////////////////////////////////
    

    override fun onCommandEvent(cmd: String, data: Any) {
        when (cmd) {
            SearchViewModel.CMD_MORE -> {
                mViewModel.viewMoreSearching.set(true)
                mGithubViewModel.searchNext()
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

        fun create() =
            SearchFragment()
    }
}