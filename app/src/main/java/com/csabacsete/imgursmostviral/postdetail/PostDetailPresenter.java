package com.csabacsete.imgursmostviral.postdetail;

import android.support.annotation.NonNull;

import com.csabacsete.imgursmostviral.data.models.Comment;
import com.csabacsete.imgursmostviral.data.models.Image;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepository;
import com.csabacsete.imgursmostviral.util.DateTimeUtils;
import com.csabacsete.imgursmostviral.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public class PostDetailPresenter implements PostDetailContract.Presenter, PostsRepository.GetPostCallback, PostsRepository.GetCommentsCallback {
    private final PostsRepository postsRepository;
    private final PostDetailContract.View view;
    private String sort;

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

        EspressoIdlingResource.increment();
        postsRepository.getPost(postId, this);
    }

    @Override
    public void getComments(String postId, String sort) {
        if (null == postId || postId.isEmpty()) {
            // TODO: 5/2/16 notify view
            return;
        }

        EspressoIdlingResource.increment();
        postsRepository.getComments(postId, sort, this);
    }

    @Override
    public void onShareButtonClicked() {
        view.startShareActionProvider("https://imgur.com/");
    }

    @Override
    public void onGalleryItemClicked() {

    }

    @Override
    public void onSortTypeClicked() {
        view.showSelectSortTypeDialog();
    }

    @Override
    public void onPostLoaded(Post post) {
        if (post != null) {
            view.setTitle(post.getTitle());
            view.setPostedByUsername(post.getAccountUrl());
            view.setPoints(String.valueOf(post.getPoints()) + " Points");
            view.setPostedWhen(DateTimeUtils.getReadableTimeElapsed(post.getDatetime()));

            setPostImages(post);
        } else {
            // TODO: 5/2/16 notify view of error
        }
        EspressoIdlingResource.decrement();
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
        view.setComments(comments);
    }

}
