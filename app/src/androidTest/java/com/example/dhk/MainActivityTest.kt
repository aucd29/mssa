package com.example.dhk


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val appCompatImageView = onView(
            allOf(
                withId(R.id.search_search),
                childAtPosition(
                    allOf(
                        withId(R.id.github_container),
                        childAtPosition(
                            withId(R.id.fragment_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        Thread.sleep(6000)

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.search_star),
                childAtPosition(
                    allOf(
                        withId(R.id.search_item_container),
                        childAtPosition(
                            withId(R.id.search_recycler),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val appCompatImageView3 = onView(
            allOf(
                withId(R.id.search_star),
                childAtPosition(
                    allOf(
                        withId(R.id.search_item_container),
                        childAtPosition(
                            withId(R.id.search_recycler),
                            1
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView3.perform(click())

        val appCompatImageView4 = onView(
            allOf(
                withId(R.id.search_star),
                childAtPosition(
                    allOf(
                        withId(R.id.search_item_container),
                        childAtPosition(
                            withId(R.id.search_recycler),
                            2
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView4.perform(click())

        val tabView = onView(
            allOf(
                withContentDescription("Local"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.github_tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())

        Thread.sleep(4000)

        val appCompatImageView5 = onView(
            allOf(
                withId(R.id.like_user_item_star),
                childAtPosition(
                    allOf(
                        withId(R.id.like_user_item_container),
                        childAtPosition(
                            withId(R.id.like_user_recycler),
                            2
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView5.perform(click())

        Thread.sleep(2000)

        val appCompatImageView6 = onView(
            allOf(
                withId(R.id.like_user_item_star),
                childAtPosition(
                    allOf(
                        withId(R.id.like_user_item_container),
                        childAtPosition(
                            withId(R.id.like_user_recycler),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView6.perform(click())

        Thread.sleep(2000)

        val appCompatImageView7 = onView(
            allOf(
                withId(R.id.like_user_item_star),
                childAtPosition(
                    allOf(
                        withId(R.id.like_user_item_container),
                        childAtPosition(
                            withId(R.id.like_user_recycler),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView7.perform(click())

        Thread.sleep(2000)

        val tabView2 = onView(
            allOf(
                withContentDescription("API"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.github_tabs),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        tabView2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
