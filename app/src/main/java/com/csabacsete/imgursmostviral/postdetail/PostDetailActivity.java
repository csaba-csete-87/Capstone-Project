package com.csabacsete.imgursmostviral.postdetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.util.EspressoIdlingResource;


public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST_ID = "postId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_post_detail);

        setupFragmentIfNeeded(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupFragmentIfNeeded(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            initFragment(PostDetailFragment.newInstance(getIntent().getStringExtra(EXTRA_POST_ID)));
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
