package com.nordlogic.imgursmostviral.postdetail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordlogic.imgursmostviral.Injection;
import com.nordlogic.imgursmostviral.R;
import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.databinding.FragmentPostDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment implements PostDetailContract.View {

    private static final String ARGUMENT_POST_ID = "postId";

    private PostDetailContract.UserActionsListener mPostDetailPresenter;
    private FragmentPostDetailBinding binding;

    public static PostDetailFragment newInstance(String postId) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_POST_ID, postId);

        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPostDetailPresenter = new PostDetailPresenter(Injection.providePostsRepository(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false);
        setupToolbar();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        String postId = getArguments().getString(ARGUMENT_POST_ID);
        mPostDetailPresenter.getPost(postId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        binding.progressIndicator.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPost(Post post) {
        PostDetailsAdapter adapter = new PostDetailsAdapter(post);
        binding.postDetails.setAdapter(adapter);
    }

    @Override
    public void showPostNotFoundView() {
        binding.notFound.setVisibility(View.VISIBLE);
    }


    private void setupToolbar() {
        PostDetailActivity activity = ((PostDetailActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
}
