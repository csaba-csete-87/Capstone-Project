package com.csabacsete.imgursmostviral.posts;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csabacsete.imgursmostviral.ImgurApplication;
import com.csabacsete.imgursmostviral.Injection;
import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.databinding.FragmentPostsBinding;
import com.csabacsete.imgursmostviral.postdetail.PostDetailActivity;
import com.csabacsete.imgursmostviral.util.AnalyticsUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment implements PostsContract.View {

    private PostsAdapter postsAdapter;

    private PostsContract.UserActionsListener postsPresenter;
    private FragmentPostsBinding binding;
    private Tracker tracker;
    PostsAdapter.PostItemListener postItemClickedListener = new PostsAdapter.PostItemListener() {
        @Override
        public void onPostClick(Post clickedPost) {
            tracker.send(AnalyticsUtil.getEvent(
                    getString(R.string.action),
                    getString(R.string.selected_post)
            ));
            postsPresenter.openPostDetails(clickedPost);
        }
    };

    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postsAdapter = new PostsAdapter(new ArrayList<Post>(0), postItemClickedListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        postsPresenter.loadPosts(false);
        trackScreen();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        postsPresenter = new PostsPresenter(Injection.provideLocalRepository(this), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posts, container, false);

        initRecyclerView();
        initRefreshLayout();
        initAd();

        return binding.getRoot();
    }

    private void initRecyclerView() {
        binding.postsList.setAdapter(postsAdapter);

        int columnCount = getContext().getResources().getInteger(R.integer.column_count);

        binding.postsList.setHasFixedSize(true);
        binding.postsList.addItemDecoration(new GridSpacingItemDecoration(columnCount, getResources().getDimensionPixelSize(R.dimen.recycler_view_default_margin), false));
        binding.postsList.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
    }

    private void initRefreshLayout() {
        binding.refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postsPresenter.loadPosts(true);
            }
        });
    }

    private void initAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        binding.ad.loadAd(adRequest);
        binding.ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker.send(AnalyticsUtil.getEvent(
                        getString(R.string.action),
                        getString(R.string.clicked_on_ad)
                ));
            }
        });
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        binding.refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.refreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showPosts(List<Post> posts) {
        postsAdapter.replaceData(posts);
    }

    @Override
    public void showPostDetailUi(String postId) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, postId);
        startActivity(intent);
    }

    private void trackScreen() {
        ImgurApplication application = (ImgurApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName(getString(R.string.posts_grid));
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
