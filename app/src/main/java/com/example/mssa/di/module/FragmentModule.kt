package com.example.mssa.di.module

import com.example.mssa.ui.meetingroom.MeetingRoomFragment
import com.example.mssa.ui.github.GithubFragment
import com.example.mssa.ui.github.likeuser.LikeUserFragment
import com.example.mssa.ui.github.search.SearchFragment
import dagger.Module

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 */

@Module(includes = [
    SearchFragment.Module::class,
    GithubFragment.Module::class,
    LikeUserFragment.Module::class,
    MeetingRoomFragment.Module::class
])
abstract class FragmentModule