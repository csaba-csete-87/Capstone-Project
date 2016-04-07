package com.nordlogic.imgursmostviral.postdetail;

import android.support.annotation.NonNull;

import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.util.EspressoIdlingResource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public class PostDetailPresenter implements PostDetailContract.UserActionsListener, PostsRepository.GetPostCallback {
    private final PostsRepository mPostsRepository;
    private final PostDetailContract.View mPostDetailView;

    public PostDetailPresenter(
        @NonNull PostsRepository postsRepository,
        @NonNull PostDetailContract.View postsDetailView) {
        checkNotNull(postsRepository);
        checkNotNull(postsDetailView);

        mPostsRepository = postsRepository;
        mPostDetailView = postsDetailView;
    }

    @Override
    public void getPost(String postId) {
        if (null == postId || postId.isEmpty()) {
            mPostDetailView.showPostNotFoundView();
            return;
        }
        mPostDetailView.setProgressIndicator(true);

        EspressoIdlingResource.increment();
        mPostsRepository.getPost(postId, this);
    }

    @Override
    public void onPostLoaded(Post post) {
        mPostDetailView.setProgressIndicator(false);

        if(post != null) {
            mPostDetailView.showPost(post);
        } else {
            mPostDetailView.showPostNotFoundView();
        }
        EspressoIdlingResource.decrement();
    }
}
