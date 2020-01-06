package com.example.dhk.di.module

import com.example.dhk.ui.github.GithubFragment
import com.example.dhk.ui.github.likeuser.LikeUserFragment
import com.example.dhk.ui.github.search.SearchFragment
import dagger.Module

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 */

@Module(includes = [
    SearchFragment.Module::class,
    GithubFragment.Module::class,
    LikeUserFragment.Module::class
])
abstract class FragmentModule