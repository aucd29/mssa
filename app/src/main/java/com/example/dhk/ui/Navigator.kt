package com.example.dhk.ui

import androidx.fragment.app.FragmentManager
import brigitte.FragmentAnim
import brigitte.FragmentCommit
import brigitte.FragmentParams
import brigitte.show
import com.example.dhk.R
import com.example.dhk.ui.github.GithubFragment
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2020. 1. 6. <p/>
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
            commit = FragmentCommit.NOW,
            backStack = false))
    }
}