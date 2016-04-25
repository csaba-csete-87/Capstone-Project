package com.nordlogic.imgursmostviral.postdetail;

import android.support.annotation.NonNull;

import com.nordlogic.imgursmostviral.data.models.Comment;
import com.nordlogic.imgursmostviral.data.models.Image;
import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.util.DateTimeUtils;
import com.nordlogic.imgursmostviral.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public class PostDetailPresenter implements PostDetailContract.Presenter, PostsRepository.GetPostCallback, PostsRepository.GetCommentsCallback {
    private final PostsRepository postsRepository;
    private final PostDetailContract.View view;
    private List<Comment> comments;

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
    public void onShareButtonClicked() {
        view.startShareActionProvider("https://imgur.com/");
    }

    @Override
    public void showRepliesForComment(Comment comment) {
        Comment commentWithReplies = searchAndReplaceCommentRecursive(comment.getId());
        view.setComments(comments);
    }

    private Comment searchAndReplaceCommentRecursive(int id) {
        return null;
    }

    private int getCommentPosition(Comment strippedComment) {
        for (int i = 0; i < comments.size(); i++) {
            Comment c = comments.get(i);
            if (c.getId() == strippedComment.getId()) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException("Stripped comment is not in original array.");
    }

    @Override
    public void hideRepliesForComment(Comment comment) {

    }

    @Override
    public void onPostLoaded(Post post) {
        if (post != null) {
            view.setTitle(post.getTitle());
            view.setPostedByUsername(post.getAccountUrl());
            view.setPoints(String.valueOf(post.getPoints()) + " Points");
            view.setPostedWhen(DateTimeUtils.getReadableTimeElapsed(post.getDatetime()));

            setPostImages(post);

            postsRepository.getComments(post.getId(), this);
        } else {
            view.setProgressIndicator(false);
            view.showPostNotFoundView();
        }
        EspressoIdlingResource.decrement();
    }

    private void setPostImages(Post post) {
        if (post.isAlbum()) {
            view.setImages(post.getImages());
        } else {
            // TODO: 4/24/16 refactor this because it's ugly
            ArrayList<Image> imageArray = new ArrayList<>();
            imageArray.add(new Image(
                    post.getTitle(),
                    post.getDescription(),
                    post.getLink(),
                    post.getType(),
                    post.getDatetime()
            ));
            view.setImages(imageArray);
        }
    }

    @Override
    public void onCommentsLoaded(List<Comment> comments) {
        this.comments = comments;
        view.setProgressIndicator(false);
        view.setComments(stripCommentsOfReplies(comments));
    }

    private List<Comment> stripCommentsOfReplies(List<Comment> comments) {
        List<Comment> strippedComments = new ArrayList<>();
        for (Comment comment : comments) {
            comment.setChildren(null);
            strippedComments.add(comment);
        }
        return strippedComments;
    }
}
