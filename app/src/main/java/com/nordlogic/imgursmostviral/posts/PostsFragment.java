package com.nordlogic.imgursmostviral.posts;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordlogic.imgursmostviral.Injection;
import com.nordlogic.imgursmostviral.R;
import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.databinding.FragmentPostsBinding;
import com.nordlogic.imgursmostviral.postdetail.GridSpacingItemDecoration;
import com.nordlogic.imgursmostviral.postdetail.PostDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment implements PostsContract.View {

    private PostsAdapter mPostsAdapter;

    private PostsContract.UserActionsListener mPostsPresenter;

    PostsAdapter.PostItemListener mItemListener = new PostsAdapter.PostItemListener() {
        @Override
        public void onPostClick(Post clickedPost) {
            mPostsPresenter.openPostDetails(clickedPost);
        }
    };
    private FragmentPostsBinding binding;

    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostsAdapter = new PostsAdapter(new ArrayList<Post>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPostsPresenter.loadPosts(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        mPostsPresenter = new PostsPresenter(Injection.providePostsRepository(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posts, container, false);

        initRecyclerView();
        initRefreshLayout();

        return binding.getRoot();
    }

    private void initRecyclerView() {
        binding.postsList.setAdapter(mPostsAdapter);

        int columnCount = getContext().getResources().getInteger(R.integer.column_count);

        binding.postsList.setHasFixedSize(true);
        binding.postsList.addItemDecoration(new GridSpacingItemDecoration(columnCount, getResources().getDimensionPixelSize(R.dimen.recycler_view_default_margin), false));
        binding.postsList.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
    }

    private void initRefreshLayout() {
        binding.refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPostsPresenter.loadPosts(true);
            }
        });
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showPosts(List<Post> posts) {
        mPostsAdapter.replaceData(posts);
    }

    @Override
    public void showPostDetailUi(String postId) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, postId);
        startActivity(intent);
    }
}
