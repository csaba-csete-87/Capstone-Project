package com.csabacsete.imgursmostviral.posts;

import android.support.annotation.NonNull;

import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsPresenter implements PostsContract.UserActionsListener, PostsRepository.GetPostsCallback {

    private final PostsRepository mPostsRepository;
    private final PostsContract.View mPostsView;

    public PostsPresenter(
            @NonNull PostsRepository postsRepository,
            @NonNull PostsContract.View postsView) {
        mPostsRepository = checkNotNull(postsRepository);
        mPostsView = checkNotNull(postsView);
    }

    @Override
    public void loadPosts(boolean forceUpdate) {
        mPostsView.setProgressIndicator(true);
        if (forceUpdate)
            mPostsRepository.refreshData();

        mPostsRepository.getPosts(this);
    }

    @Override
    public void openPostDetails(@NonNull Post requestedPost) {
        checkNotNull(requestedPost);
        mPostsView.showPostDetailUi(requestedPost.getId());
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        mPostsView.setProgressIndicator(false);
        mPostsView.showPosts(posts);
    }
}
