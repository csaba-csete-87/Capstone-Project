package com.nordlogic.imgursmostviral;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.nordlogic.imgursmostviral.posts.PostsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.nordlogic.imgursmostviral.matchers.CommonViewMatchers.hasAnyText;
import static com.nordlogic.imgursmostviral.matchers.CommonViewMatchers.hasDrawable;
import static com.nordlogic.imgursmostviral.matchers.CommonViewMatchers.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PostsScreenTest {

    @Rule
    public ActivityTestRule<PostsActivity> mPostsActivityTestRule =
        new ActivityTestRule<>(PostsActivity.class);

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
            mPostsActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void testOnActivityOpened_RecyclerViewIsShown() throws Exception {
        onView(withId(R.id.posts_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testOnActivityCreated_FirstPostShowsTitle() throws Exception {
        onView(withRecyclerView(R.id.posts_list)
            .atPositionOnView(0, R.id.post_title))
            .check(matches(allOf(
                    isDisplayed(),
                    hasAnyText()
                )
            ));
    }

    @Test
    public void testOnActivityCreated_FirstPostShowsImage() throws Exception {
        onView(withRecyclerView(R.id.posts_list)
            .atPositionOnView(0, R.id.post_thumbnail))
            .check(matches(allOf(
                    isDisplayed(),
                    hasDrawable()
                )
            ));
    }

    @Test
    public void testClickOnItem_ShowsItsDetailsScreen() throws Exception {
        onView(withId(R.id.posts_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.post_title)).check(matches(allOf(
            isDisplayed(),
            hasAnyText()
        )));
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
            mPostsActivityTestRule.getActivity().getCountingIdlingResource());
    }

}
