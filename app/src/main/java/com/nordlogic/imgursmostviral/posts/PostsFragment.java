package com.nordlogic.imgursmostviral.posts;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nordlogic.imgursmostviral.Injection;
import com.nordlogic.imgursmostviral.R;
import com.nordlogic.imgursmostviral.data.models.Post;
import com.nordlogic.imgursmostviral.databinding.FragmentPostsBinding;
import com.nordlogic.imgursmostviral.postdetail.GridSpacingItemDecoration;
import com.nordlogic.imgursmostviral.postdetail.PostDetailActivity;
import com.nordlogic.imgursmostviral.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
        binding.postsList.addItemDecoration(new GridSpacingItemDecoration(columnCount, 0, false));
        binding.postsList.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
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

    public static class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

        private List<Post> mPosts;
        private final PostItemListener mItemListener;
        private Context mContext;

        public PostsAdapter(List<Post> posts, PostItemListener itemListener) {
            setList(posts);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View postView = inflater.inflate(R.layout.item_post, parent, false);

            return new ViewHolder(postView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Post post = mPosts.get(position);

            holder.title.setText(post.getTitle());

            EspressoIdlingResource.increment();
            Glide.with(mContext)
                    .load(post.getThumbnail())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new GlideDrawableImageViewTarget(holder.thumbnail) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            EspressoIdlingResource.decrement();
                        }
                    });
        }

        public void replaceData(List<Post> posts) {
            setList(posts);
            notifyDataSetChanged();
        }

        private void setList(List<Post> posts) {
            mPosts = checkNotNull(posts);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        public Post getItem(int position) {
            return mPosts.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView title;
            ImageView thumbnail;

            private PostItemListener mItemListener;

            public ViewHolder(View itemView, PostItemListener listener) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.post_title);
                thumbnail = (ImageView) itemView.findViewById(R.id.post_thumbnail);

                mItemListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mItemListener.onPostClick(getItem(getAdapterPosition()));
            }
        }

        public interface PostItemListener {

            void onPostClick(Post clickedPost);
        }
    }
}
