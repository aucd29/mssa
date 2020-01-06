package com.example.dhk.github

import brigitte.shield.*
import com.example.dhk.ui.github.GithubTabViewModel
import com.google.android.material.tabs.TabLayout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019-12-24 <p/>
 */

@RunWith(RobolectricTestRunner::class)
class TabTest: BaseRoboViewModelTest<GithubTabViewModel>()  {
    @Before
    @Throws(Exception::class)
    fun setup() {
        initMock()

        viewmodel = GithubTabViewModel()
    }

    @Test
    fun tabTest() {
        val tab = mock(TabLayout.Tab::class.java)

        repeat(2) {
            tab.position.mockReturn(it)
            println("TAB CHANGE : $it")

            viewmodel.apply {
                mockObserver(tabLive).apply {
                    tabChangedCallback.get()?.onTabSelected(tab)
                    verifyChanged(tab)

                    println("TAB LIVE : ${tabLive.value?.position}")
                    tabLive.value?.position.assertEquals(it)
                }
            }
        }
    }
}