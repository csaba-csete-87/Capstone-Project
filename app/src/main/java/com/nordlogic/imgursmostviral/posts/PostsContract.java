package com.nordlogic.imgursmostviral.posts;

import com.nordlogic.imgursmostviral.data.models.Post;

import java.util.List;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public interface PostsContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showPosts(List<Post> posts);

        void showPostDetailUi(String postId);
    }

    interface UserActionsListener {

        void loadPosts(boolean forceUpdate);

        void openPostDetails(Post requestedPost);
    }

}
