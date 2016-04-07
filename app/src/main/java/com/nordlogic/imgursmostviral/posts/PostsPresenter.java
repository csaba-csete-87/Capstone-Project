package com.nordlogic.imgursmostviral.posts;

import android.support.annotation.NonNull;

import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsPresenter implements PostsContract.UserActionsListener, PostsRepository.LoadPostsCallback {

    private final PostsRepository mPostsRepository;
    private final PostsContract.View mPostsView;

    public PostsPresenter(
        @NonNull PostsRepository postsRepository,
        @NonNull PostsContract.View postsView) {
        mPostsRepository = checkNotNull(postsRepository, "postsRepository cannot be null");
        mPostsView = checkNotNull(postsView, "postsView cannot be null");
    }

    @Override
    public void loadPosts(boolean forceUpdate) {
        mPostsView.setProgressIndicator(true);
        if (forceUpdate)
            mPostsRepository.refreshData();

        EspressoIdlingResource.increment();
        mPostsRepository.getPosts(this);
    }

    @Override
    public void openPostDetails(@NonNull Post requestedPost) {
        checkNotNull(requestedPost, "requestedPost cannot be null!");
        mPostsView.showPostDetailUi(requestedPost.getId());
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        mPostsView.setProgressIndicator(false);
        mPostsView.showPosts(posts);
        EspressoIdlingResource.decrement();
    }
}
