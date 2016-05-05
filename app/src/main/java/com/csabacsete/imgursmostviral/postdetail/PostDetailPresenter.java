package com.csabacsete.imgursmostviral.postdetail;

import android.support.annotation.NonNull;

import com.csabacsete.imgursmostviral.data.models.Comment;
import com.csabacsete.imgursmostviral.data.models.Image;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepository;
import com.csabacsete.imgursmostviral.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public class PostDetailPresenter implements PostDetailContract.Presenter, PostsRepository.GetPostCallback, PostsRepository.GetCommentsCallback {
    private final PostsRepository postsRepository;
    private final PostDetailContract.View view;
    private boolean postInfoReceived;
    private boolean commentsReceived;
    private String shareLink;

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
            // TODO: 5/2/16 notify view
            return;
        }

        postInfoReceived = false;
        view.setProgressIndicator(true);
        postsRepository.getPost(postId, this);
    }

    @Override
    public void getComments(String postId, String sort) {
        if (null == postId || postId.isEmpty()) {
            // TODO: 5/2/16 notify view
            return;
        }

        commentsReceived = false;
        view.clearCommentSection();
        view.setProgressIndicator(true);
        view.setCommentProgress(true);
        postsRepository.getComments(postId, sort, this);
    }

    @Override
    public void onGalleryItemClicked(Image image) {
        view.startZoomingImageViewer(image.getLink());
    }

    @Override
    public void onSortTypeClicked() {
        view.showSelectSortTypeDialog();
    }

    @Override
    public void onShareButtonClicked() {
        view.sharePost(shareLink);
    }

    @Override
    public void onPostLoaded(Post post) {
        if (post != null) {
            this.shareLink = post.getLink();
            view.setTitle(post.getTitle());
            view.setPostedByUsername(post.getAccountUrl());
            view.setPoints(post.getPoints());
            view.setPostedWhen(DateTimeUtils.getReadableTimeElapsed(post.getDatetime()));

            setPostImages(post);
        } else {
            // TODO: 5/2/16 notify view of error
        }
        postInfoReceived = true;
        view.setProgressIndicator(!commentsReceived);
    }

    private void setPostImages(Post post) {
        if (post.isAlbum()) {
            view.setImages(post.getImages());
        } else {
            ArrayList<Image> imageArray = createImageArrayFromPost(post);
            view.setImages(imageArray);
        }
    }

    @NonNull
    private ArrayList<Image> createImageArrayFromPost(Post post) {
        ArrayList<Image> imageArray = new ArrayList<>();
        imageArray.add(new Image(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getLink(),
                post.getType(),
                post.getGifv(),
                post.getDatetime()
        ));
        return imageArray;
    }

    @Override
    public void onCommentsLoaded(List<Comment> comments) {
        if (comments != null) {
            view.setComments(comments);
        } else {
            // TODO: 5/3/16 notify view
        }

        commentsReceived = true;
        view.setProgressIndicator(!postInfoReceived);
    }

}
