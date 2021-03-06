package com.csabacsete.imgursmostviral.posts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.db.sync.ImgurSyncAdapter;
import com.csabacsete.imgursmostviral.databinding.ActivityPostsBinding;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPostsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_posts);
        setSupportActionBar(binding.toolbar);

        setupFragmentIfNeeded(savedInstanceState);

        ImgurSyncAdapter.initializeSyncAdapter(this);
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
}
