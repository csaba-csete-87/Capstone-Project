package com.csabacsete.imgursmostviral;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;

import com.csabacsete.imgursmostviral.postdetail.PostDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by csaba.csete on 2016-02-25.
 */
@RunWith(Enclosed.class)
@LargeTest
public class PostDetailsScreenTest {

    private static final String NON_EXISTING_ID = "aaaa";
    private static final String POPULAR_ID = "oVR0Z"; //hopefully it never gets deleted
    private static final String POST_TITLE = "It's Imgur's Birthday Week! It's been a wonderful seven years.";

    @Test
    public void testDummy() throws Exception {

    }

    public static class NullReceivedContext {
        @Rule
        public ActivityTestRule<PostDetailActivity> mPostDetailActivityTestRule =
                new ActivityTestRule<>(
                        PostDetailActivity.class,
                        true /* Initial touch mode  */,
                        false /* Lazily launch activity */
                );

        @Before
        public void intentWithStubbedNoteId() throws Exception {
            Intent startIntent = new Intent();
            startIntent.putParcelableArrayListExtra(PostDetailActivity.EXTRA_POST_ID, null);
            mPostDetailActivityTestRule.launchActivity(startIntent);

            registerIdlingResource();
        }

        @Test
        public void onNullReceived_NotFoundImageIsShown() throws Exception {
//            onView(withId(R.id.progress_indicator)).check(matches(not(isDisplayed())));
//            onView(withId(R.id.not_found)).check(matches(allOf(
//                hasDrawable(),
//                isDisplayed()
//            )));
        }

        @After
        public void unregisterIdlingResource() {
            Espresso.unregisterIdlingResources(
                    mPostDetailActivityTestRule.getActivity().getCountingIdlingResource());
        }

        private void registerIdlingResource() {
            Espresso.registerIdlingResources(
                    mPostDetailActivityTestRule.getActivity().getCountingIdlingResource());
        }
    }

    public static class NonExistentPostIdReceivedContext {
        @Rule
        public ActivityTestRule<PostDetailActivity> mPostDetailActivityTestRule =
                new ActivityTestRule<>(
                        PostDetailActivity.class,
                        true /* Initial touch mode  */,
                        false /* Lazily launch activity */
                );

        @Before
        public void intentWithStubbedNoteId() throws Exception {
            Intent startIntent = new Intent();
            startIntent.putExtra(PostDetailActivity.EXTRA_POST_ID, NON_EXISTING_ID);
            mPostDetailActivityTestRule.launchActivity(startIntent);

            registerIdlingResource();
        }

        @Test
        public void onNonExistingIdLoaded_NotFoundImageIsShown() throws Exception {
            onView(withId(R.id.progress_indicator)).check(matches(not(isDisplayed())));
            onView(withId(R.id.not_found)).check(matches(allOf(
                    hasDrawable(),
                    isDisplayed()
            )));
        }

        @After
        public void unregisterIdlingResource() {
            Espresso.unregisterIdlingResources(
                    mPostDetailActivityTestRule.getActivity().getCountingIdlingResource());
        }

        private void registerIdlingResource() {
            Espresso.registerIdlingResources(
                    mPostDetailActivityTestRule.getActivity().getCountingIdlingResource());
        }
    }

    public static class ProperPostIdReceivedContext {
        @Rule
        public ActivityTestRule<PostDetailActivity> mPostDetailActivityTestRule =
                new ActivityTestRule<>(
                        PostDetailActivity.class,
                        true /* Initial touch mode  */,
                        false /* Lazily launch activity */
                );

        @Before
        public void intentWithStubbedNoteId() throws Exception {
            Intent startIntent = new Intent();
            startIntent.putExtra(PostDetailActivity.EXTRA_POST_ID, POPULAR_ID);
            mPostDetailActivityTestRule.launchActivity(startIntent);

            registerIdlingResource();
        }

        @Test
        public void onProperNoteLoaded_DetailsAreDisplayedInUi() throws Exception {
            onView(withText(POST_TITLE)).check(matches(isDisplayed()));
            onView(withId(R.id.progress_indicator)).check(matches(not(isDisplayed())));
            onView(withId(R.id.not_found)).check(matches(not(isDisplayed())));
        }

        @After
        public void unregisterIdlingResource() {
            Espresso.unregisterIdlingResources(
                    mPostDetailActivityTestRule.getActivity().getCountingIdlingResource());
        }

        private void registerIdlingResource() {
            Espresso.registerIdlingResources(
                    mPostDetailActivityTestRule.getActivity().getCountingIdlingResource());
        }
    }

}
