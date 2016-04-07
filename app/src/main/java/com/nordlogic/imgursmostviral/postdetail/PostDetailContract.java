package com.nordlogic.imgursmostviral.postdetail;

import com.nordlogic.imgursmostviral.data.models.Post;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public interface PostDetailContract {

    interface View {
        void setProgressIndicator(boolean active);

        void showPost(Post post);

        void showPostNotFoundView();
    }

    interface UserActionsListener {

        void getPost(String postId);
    }
}
