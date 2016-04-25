package com.nordlogic.imgursmostviral.postdetail;

import com.nordlogic.imgursmostviral.data.models.Comment;
import com.nordlogic.imgursmostviral.data.models.Image;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public interface PostDetailContract {

    interface View {
        void setProgressIndicator(boolean active);

        void showPostNotFoundView();

        void setTitle(String title);

        void setPostedByUsername(String username);

        void setPoints(String points);

        void setPostedWhen(String postedWhen);

        void setImages(List<Image> images);

        void setComments(List<Comment> comments);

        void startShareActionProvider(String link);
    }

    interface Presenter {

        void getPost(String postId);

        void onShareButtonClicked();

        void onCommentRowClicked(int commentId);
    }
}
