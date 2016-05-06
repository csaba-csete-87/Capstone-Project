package com.csabacsete.imgursmostviral.postdetail;

import com.csabacsete.imgursmostviral.data.models.Comment;
import com.csabacsete.imgursmostviral.data.models.Image;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public interface PostDetailContract {

    interface View {

        void setProgressIndicator(boolean active);

        void setCommentProgress(boolean active);

        void setTitle(String title);

        void setPostedByUsername(String username);

        void setPoints(int points);

        void setPostedWhen(String postedWhen);

        void setImages(List<Image> images);

        void setComments(List<Comment> comments);

        void showSelectSortTypeDialog();

        void startZoomingImageViewer(String path);

        void sharePost(String link);

        void clearCommentSection();

        void showPostNotFound();

        void showErrorLoadingPost();

        void showErrorLoadingComments();

        void showNoNetworkAvailable();
    }

    interface Presenter {

        void getData(String postId, String sort);

        void getPost(String postId);

        void getComments(String postId, String sort);

        void onGalleryItemClicked(Image image);

        void onSortTypeClicked();

        void onShareButtonClicked();
    }
}
