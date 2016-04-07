package com.nordlogic.imgursmostviral.posts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.nordlogic.imgursmostviral.R;
import com.nordlogic.imgursmostviral.databinding.ActivityPostsBinding;
import com.nordlogic.imgursmostviral.util.EspressoIdlingResource;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPostsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_posts);
        setSupportActionBar(binding.toolbar);

        setupFragmentIfNeeded(savedInstanceState);
    }

    private void setupFragmentIfNeeded(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            initFragment(PostsFragment.newInstance());
        }
    }

    private void initFragment(Fragment postsFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, postsFragment);
        transaction.commit();
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

}
