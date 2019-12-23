package com.sunragav.indiecampers.feature_home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.sunragav.indiecampers.feature_home.ui.views.ComicsListFeatureActivity
import com.sunragav.indiecampers.marvelcomics.R
import com.sunragav.indiecampers.feature_home.utils.CustomMatchers.Companion.atPositionWithComis_Id_And_Title
import com.sunragav.indiecampers.feature_home.utils.RecyclerViewChildAction.Companion.clickChildViewWithId
import com.sunragav.indiecampers.feature_home.utils.RepositoryStateIdlingResource
import com.sunragav.indiecampers.feature_home.utils.comic1
import com.sunragav.indiecampers.feature_home.ui.recyclerview.viewholders.ComicsViewHolder
import com.sunragav.indiecampers.remotedata.mapper.ComicsRemoteMapper
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ComicsListFeatureActivityTest {

    @get:Rule
    val rule = ActivityTestRule<ComicsListFeatureActivity>(ComicsListFeatureActivity::class.java)

    private lateinit var repositoryIdlingResource: RepositoryStateIdlingResource
    private val comicsRemoteMapper = ComicsRemoteMapper()

    @Before
    fun setup() {
        repositoryIdlingResource = RepositoryStateIdlingResource(rule.activity.repositoryStateRelay)
        IdlingRegistry.getInstance().register(repositoryIdlingResource)
    }

    @Test
    fun test_comics_displayed() {
        onView(withId(R.id.rv_comics_list)).check(
            matches(
                atPositionWithComis_Id_And_Title(
                    0,
                    comicsRemoteMapper.from(comic1)
                )
            )
        )
    }

    @Test
    fun test_comics_clicked() {
        onView(withId(R.id.rv_comics_list)).perform(
         RecyclerViewActions.actionOnItemAtPosition<ComicsViewHolder>(0,clickChildViewWithId(R.id.card_view_item_comics))
        )
        onView(withId(R.id.tv_detail_title)).check(matches(withText(comic1.title)))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(repositoryIdlingResource)
    }
}

