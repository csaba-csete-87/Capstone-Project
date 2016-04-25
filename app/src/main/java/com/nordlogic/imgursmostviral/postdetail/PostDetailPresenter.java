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
    private List<Comment> commentsWithReplies;
    private List<Comment> displayedComments;

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

    private Comment searchCommentRecursive(int needle, List<Comment> haystack) {
        for (Comment c : haystack) {
            if (c.getId() == needle) {
                return c;
            } else {
                searchCommentRecursive(needle, c.getChildren());
            }
        }
        throw new RuntimeException("Did not find the comment I was looking for");
    }

    private void replaceCommentRecursive(int needle, List<Comment> haystack, Comment commentWithReplies) {
        if (haystack == null) {
            return;
        }
        for (int i = 0; i < haystack.size(); i++) {
            Comment c = haystack.get(i);
            if (c.getId() == needle) {
                haystack.set(i, commentWithReplies);
            } else {
                replaceCommentRecursive(needle, c.getChildren(), commentWithReplies);
            }
        }
    }

    @Override
    public void onCommentRowClicked(int commentId) {
        Comment c = searchCommentRecursive(commentId, commentsWithReplies);
        replaceCommentRecursive(commentId, displayedComments, c);

        view.setComments(displayedComments);
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
        this.commentsWithReplies = comments;
        setStrippedComments();

        view.setComments(displayedComments);
        view.setProgressIndicator(false);
    }

    private void setStrippedComments() {
        displayedComments = new ArrayList<>();
        for (Comment comment : commentsWithReplies) {
            comment.setChildrenSize(comment.getChildren().size());
            comment.setChildren(null);
            displayedComments.add(comment);
        }
    }
}
