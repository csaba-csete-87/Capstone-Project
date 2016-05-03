package com.csabacsete.imgursmostviral.posts;

import android.support.annotation.NonNull;

import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepository;
import com.csabacsete.imgursmostviral.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsPresenter implements ImgurContract.UserActionsListener, PostsRepository.GetPostsCallback {

    private final PostsRepository mPostsRepository;
    private final ImgurContract.View mPostsView;

    public PostsPresenter(
        @NonNull PostsRepository postsRepository,
        @NonNull ImgurContract.View postsView) {
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
