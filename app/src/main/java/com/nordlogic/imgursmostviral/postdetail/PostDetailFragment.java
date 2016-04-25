package com.nordlogic.imgursmostviral.postdetail;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordlogic.imgursmostviral.Injection;
import com.nordlogic.imgursmostviral.R;
import com.nordlogic.imgursmostviral.data.models.Comment;
import com.nordlogic.imgursmostviral.data.models.Image;
import com.nordlogic.imgursmostviral.databinding.FragmentPostDetailBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment implements PostDetailContract.View, PostCommentsAdapter.CommentItemClickListener {

    private static final String ARGUMENT_POST_ID = "postId";

    private PostDetailContract.Presenter presenter;
    private FragmentPostDetailBinding binding;
    private PostCommentsAdapter commentsAdapter;
    private PostImagesAdapter imagesAdapter;

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
        presenter = new PostDetailPresenter(Injection.providePostsRepository(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false);
        initRecyclerViews();

        setupToolbar();
        setupFab();

        return binding.getRoot();
    }

    private void initRecyclerViews() {
        imagesAdapter = new PostImagesAdapter(null);
        commentsAdapter = new PostCommentsAdapter(null, this);

        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        llm2.setOrientation(LinearLayoutManager.VERTICAL);

        binding.postImages.setLayoutManager(llm1);
        binding.postComments.setLayoutManager(llm2);

        binding.postImages.setAdapter(imagesAdapter);
        binding.postComments.setAdapter(commentsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        String postId = getArguments().getString(ARGUMENT_POST_ID);
        presenter.getPost(postId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
//        binding.progressIndicator.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPostNotFoundView() {
//        binding.notFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(final String title) {
        binding.title.setText(title);
    }

    @Override
    public void setPostedByUsername(final String username) {
        binding.postedByUsername.setText(username);
    }

    @Override
    public void setPoints(final String points) {
        binding.points.setText(points);
    }

    @Override
    public void setPostedWhen(final String postedWhen) {
        binding.postedWhen.setText(postedWhen);
    }

    @Override
    public void setImages(List<Image> images) {
        imagesAdapter.setImages(images);
    }

    @Override
    public void setComments(List<Comment> comments) {
        commentsAdapter.setComments(comments);
    }

    @Override
    public void startShareActionProvider(String link) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        startActivity(sendIntent);
    }

    private void setupToolbar() {
        PostDetailActivity activity = ((PostDetailActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setupFab() {
        binding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShareButtonClicked();
            }
        });
    }

    @Override
    public void onCommentClicked(int commentId) {
        presenter.onCommentRowClicked(commentId);
    }
}
