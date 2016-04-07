package com.nordlogic.imgursmostviral;

import com.google.common.collect.Lists;
import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository.LoadPostsCallback;
import com.nordlogic.imgursmostviral.posts.PostsContract;
import com.nordlogic.imgursmostviral.posts.PostsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsPresenterTest {

    private static List<Post> POSTS = Lists.newArrayList(new Post("id1", "Title1"), new Post("id2", "Title2"));

    private PostsPresenter mPostsPresenter;

    @Mock
    private PostsRepository mPostsRepository;

    @Mock
    private PostsContract.View mPostsView;

    @Captor
    private ArgumentCaptor<LoadPostsCallback> mLoadPostsCallbackCaptor;

    @Before
    public void setupPostsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mPostsPresenter = new PostsPresenter(mPostsRepository, mPostsView);
    }

    @Test
    public void testLoadPostsFromRepository() throws Exception {
        mPostsPresenter.loadPosts(true);
        verify(mPostsView).setProgressIndicator(true);

        verify(mPostsRepository).getPosts(mLoadPostsCallbackCaptor.capture());
        mLoadPostsCallbackCaptor.getValue().onPostsLoaded(POSTS);

        verify(mPostsView).setProgressIndicator(false);
        verify(mPostsView).showPosts(POSTS);
    }

    @Test
    public void testClickOnPost_ShowsPostDetailsUi() throws Exception {
        Post requestedPost = new Post("postId", "Requested Post");

        mPostsPresenter.openPostDetails(requestedPost);

        verify(mPostsView).showPostDetailUi(any(String.class));
    }
}
