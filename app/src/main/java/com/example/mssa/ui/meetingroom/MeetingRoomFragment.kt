package com.example.mssa.ui.meetingroom

import androidx.fragment.app.Fragment
import brigitte.BaseDaggerFragment
import brigitte.di.dagger.scope.FragmentScope
import com.example.mssa.R
import com.example.mssa.databinding.MeetingRoomFragmentBinding
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import javax.inject.Inject

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class MeetingRoomFragment @Inject constructor(
): BaseDaggerFragment<MeetingRoomFragmentBinding, MeetingRoomViewModel>() {
    override val layoutId = R.layout.meeting_room_fragment

    override fun initViewBinding() {
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
        @ContributesAndroidInjector(modules = [MeetingRoomFragmentModule::class])
        abstract fun contributeCustomUiFragmentInjector(): MeetingRoomFragment
    }

    @dagger.Module
    abstract class MeetingRoomFragmentModule {
        @Binds
        abstract fun bindMeetingRoomFragment(fragment: MeetingRoomFragment): Fragment

        @dagger.Module
        companion object {
        }
    }
}