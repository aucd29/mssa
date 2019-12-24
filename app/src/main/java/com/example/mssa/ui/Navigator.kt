package com.example.mssa.ui

import androidx.fragment.app.FragmentManager
import brigitte.FragmentAnim
import brigitte.FragmentParams
import brigitte.show
import com.example.mssa.R
import com.example.mssa.ui.meetingroom.MeetingRoomFragment
import com.example.mssa.ui.github.GithubFragment
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

class Navigator @Inject constructor(
    @param:Named("activityFragmentManager") val manager: FragmentManager
) {
    companion object {
        private val mLog = LoggerFactory.getLogger(Navigator::class.java)

        const val CONTAINER = R.id.fragment_container
    }

    fun githubFragment() {
        if (mLog.isInfoEnabled) {
            mLog.info("GithubFragment")
        }

        manager.show<GithubFragment>(FragmentParams(CONTAINER,
            anim = FragmentAnim.RIGHT))
    }

    fun meetingRoomFragment() {
        if (mLog.isInfoEnabled) {
            mLog.info("MEETING ROOM FRAGMENT")
        }

        manager.show<MeetingRoomFragment>(
            FragmentParams(CONTAINER,
                anim = FragmentAnim.RIGHT))
    }
}