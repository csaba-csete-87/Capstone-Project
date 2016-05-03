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

        void setTitle(String title);

        void setPostedByUsername(String username);

        void setPoints(String points);

        void setPostedWhen(String postedWhen);

        void setImages(List<Image> images);

        void setComments(List<Comment> comments);

        void startShareActionProvider(String link);

        void showSelectSortTypeDialog();

        void startZoomingImageViewer(String path);
    }

    interface Presenter {

        void getPost(String postId);

        void getComments(String postId, String sort);

        void onShareButtonClicked();

        void onGalleryItemClicked(Image image);

        void onSortTypeClicked();
    }
}
