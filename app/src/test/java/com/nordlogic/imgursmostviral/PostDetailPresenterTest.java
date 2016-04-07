package com.nordlogic.imgursmostviral;

import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.postdetail.PostDetailContract;
import com.nordlogic.imgursmostviral.postdetail.PostDetailPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by csaba.csete on 2016-02-25.
 */
public class PostDetailPresenterTest {

    private static final String TEST_ID = "testId";
    private static final String TEST_TITLE = "testTitle";
    private static final String UNKNOWN_POST_ID = "unknownPostId";

    private PostDetailPresenter mPostDetailPresenter;

    @Mock
    private PostsRepository mPostsRepository;

    @Mock
    private PostDetailContract.View mPostsDetailView;

    @Captor
    private ArgumentCaptor<PostsRepository.GetPostCallback> mGetPostCallbackCaptor;

    @Before
    public void setupPostDetailPresenter() {
        MockitoAnnotations.initMocks(this);

        mPostDetailPresenter = new PostDetailPresenter(mPostsRepository, mPostsDetailView);
    }

    @Test
    public void getPostFromRepository_loadsPostIntoView() {
        // Given an initialized PostDetailPresenter with stubbed post
        Post post = new Post(TEST_ID, TEST_TITLE);

        mPostDetailPresenter.getPost(post.getId());
        verify(mPostsDetailView).setProgressIndicator(true);
        verify(mPostsRepository).getPost(eq(post.getId()), mGetPostCallbackCaptor.capture());

        mGetPostCallbackCaptor.getValue().onPostLoaded(post); //trigger callback
        verify(mPostsDetailView).setProgressIndicator(false);
        verify(mPostsDetailView).showPost(post);
    }

    @Test
    public void getUnknownPostFromRepository_showsNotFoundView() throws Exception {
        mPostDetailPresenter.getPost(UNKNOWN_POST_ID);
        verify(mPostsDetailView).setProgressIndicator(true);
        verify(mPostsRepository).getPost(eq(UNKNOWN_POST_ID), mGetPostCallbackCaptor.capture());

        mGetPostCallbackCaptor.getValue().onPostLoaded(null); //trigger callback
        verify(mPostsDetailView).setProgressIndicator(false);
        verify(mPostsDetailView).showPostNotFoundView();
    }

    @Test
    public void openingScreenWithNullPostId_showsNotFoundView() throws Exception {
        mPostDetailPresenter.getPost(null);

        verify(mPostsDetailView).showPostNotFoundView();
    }

    @Test
    public void openingScreenWithEmptyPostId_showsNotFoundView() throws Exception {
        mPostDetailPresenter.getPost("");

        verify(mPostsDetailView).showPostNotFoundView();
    }
}
