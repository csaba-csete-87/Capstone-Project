package com.nordlogic.imgursmostviral.postdetail;

import android.support.annotation.NonNull;

import com.nordlogic.imgursmostviral.data.models.Comment;
import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public class PostDetailPresenter implements PostDetailContract.UserActionsListener, PostsRepository.GetPostCallback, PostsRepository.GetCommentsCallback {
    private final PostsRepository postsRepository;
    private final PostDetailContract.View view;
    private Post post;

    public PostDetailPresenter(
        @NonNull PostsRepository postsRepository,
        @NonNull PostDetailContract.View postsDetailView) {
        checkNotNull(postsRepository);
        checkNotNull(postsDetailView);

        this.postsRepository = postsRepository;
        view = postsDetailView;
    }

    @Override
    public void getPost(String postId) {
        if (null == postId || postId.isEmpty()) {
            view.showPostNotFoundView();
            return;
        }
        view.setProgressIndicator(true);

        EspressoIdlingResource.increment();
        postsRepository.getPost(postId, this);
    }

    @Override
    public void onPostLoaded(Post post) {
        if(post != null) {
            this.post = post;
            postsRepository.getComments(post.getId(), this);
        } else {
            view.setProgressIndicator(false);
            view.showPostNotFoundView();
        }
        EspressoIdlingResource.decrement();
    }

    @Override
    public void onCommentsLoaded(List<Comment> comments) {
        view.setProgressIndicator(false);
        post.setComments(comments);
        view.showPost(post);
    }
}
