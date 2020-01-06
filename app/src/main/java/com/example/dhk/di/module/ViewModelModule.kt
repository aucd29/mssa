package com.example.dhk.di.module

import androidx.lifecycle.ViewModel
import brigitte.di.dagger.module.ViewModelKey
import com.example.dhk.ui.github.GithubViewModel
import com.example.dhk.ui.github.GithubTabViewModel
import com.example.dhk.ui.github.likeuser.LikeUserViewModel
import com.example.dhk.ui.main.MainViewModel
import com.example.dhk.ui.github.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 6. <p/>
 */

@Module
abstract class ViewModelModule {
    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MAIN
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(vm: MainViewModel): ViewModel

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // GITHUB
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Binds
    @IntoMap
    @ViewModelKey(GithubViewModel::class)
    abstract fun bindGithubViewModel(vm: GithubViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GithubTabViewModel::class)
    abstract fun bindTabViewModel(vm: GithubTabViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(vm: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LikeUserViewModel::class)
    abstract fun bindLikeUserViewModel(vm: LikeUserViewModel): ViewModel

}
