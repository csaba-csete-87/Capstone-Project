package com.csabacsete.imgursmostviral.postdetail;


import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csabacsete.imgursmostviral.ImgurApplication;
import com.csabacsete.imgursmostviral.Injection;
import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.models.Comment;
import com.csabacsete.imgursmostviral.data.models.Image;
import com.csabacsete.imgursmostviral.databinding.FragmentPostDetailBinding;
import com.csabacsete.imgursmostviral.util.AnalyticsUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.unnamed.b.atv.model.TreeNode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment implements PostDetailContract.View {

    private static final String ARGUMENT_POST_ID = "postId";

    private PostDetailContract.Presenter presenter;
    private FragmentPostDetailBinding binding;
    private PostImagesAdapter imagesAdapter;
    private String postId;
    private Tracker tracker;

    public static PostDetailFragment newInstance(String postId) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_POST_ID, postId);

        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new PostDetailPresenter(Injection.provideServerRepository(), this);

        presenter.getData(postId, getString(R.string.best).toLowerCase());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false);

        setupViews();

        postId = getArguments().getString(ARGUMENT_POST_ID);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        trackScreen();
    }

    @Override
    public void setProgressIndicator(boolean active) {
        boolean showRefresh = false;
        if (active) {
            if (!binding.refreshLayout.isRefreshing()) {
                showRefresh = true;
                binding.refreshLayout.setRefreshing(true);
            } else {
                return;
            }
        }

        binding.refreshLayout.setRefreshing(showRefresh);
    }

    @Override
    public void setCommentProgress(boolean active) {
        binding.progressComments.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTitle(final String title) {
        binding.title.setText(title);
    }

    @Override
    public void setPostedByUsername(final String username) {
        if (!TextUtils.isEmpty(username)) {
            binding.postedByUsername.setText(username);
        } else {
            binding.postedBy.setVisibility(View.GONE);
            binding.postedByUsername.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPoints(int points) {
        String unformatted = getString(R.string.points_plural);
        if (points == 1) {
            unformatted = getString(R.string.points_singular);
        }
        binding.points.setText(String.format(unformatted, points));
    }

    @Override
    public void setPostedWhen(final String postedWhen) {
        binding.postedWhen.setText(postedWhen);
    }

    @Override
    public void setImages(List<Image> images) {
        imagesAdapter.setImages(images);
    }

    @Override
    public void setComments(List<Comment> comments) {
        //noinspection unchecked
        new AddCommentSectionViewToLayoutTask().execute(comments);
    }

    @Override
    public void showSelectSortTypeDialog() {
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(getResources().getStringArray(R.array.sort_types), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String bindingText = getString(R.string.best);
                        if (i == 1) {
                            bindingText = getString(R.string.top);
                        } else if (i == 2) {
                            bindingText = getString(R.string.newest);

                        }
                        binding.sort.setText(bindingText);
                        presenter.getComments(postId, bindingText.toLowerCase());
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void startZoomingImageViewer(String path) {
        // TODO: 5/2/16 maybe in a future release this will be implemented
    }

    @Override
    public void sharePost(String link) {
        tracker.send(AnalyticsUtils.getEvent(
                getString(R.string.action),
                getString(R.string.clicked_share)
        ));
        startActivity(getShareIntent(link));
    }

    @Override
    public void clearCommentSection() {
        binding.commentsContainer.removeAllViews();
    }

    @Override
    public void showPostNotFound() {
        Snackbar.make(binding.getRoot(), R.string.post_not_found, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorLoadingPost() {
        Snackbar.make(binding.getRoot(), R.string.error_load_post, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorLoadingComments() {
        Snackbar.make(binding.getRoot(), R.string.error_load_comments, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoNetworkAvailable() {
        binding.containerNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoNetworkAvailable() {
        binding.containerNoInternet.setVisibility(View.GONE);
    }

    private void setupViews() {
        setupRefreshLayout();
        setupToolbar();
        setupGallery();
        setupSort();
        setupFab();
    }

    private void setupRefreshLayout() {
        binding.refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent));
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getComments(postId, getCurrentSort());
            }
        });
    }

    private String getCurrentSort() {
        return binding.sort.getText().toString().toLowerCase();
    }

    private void setupToolbar() {
        PostDetailActivity activity = ((PostDetailActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setupGallery() {
        imagesAdapter = new PostImagesAdapter(null);

        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setOrientation(LinearLayoutManager.VERTICAL);

        binding.postImages.setLayoutManager(llm1);
        binding.postImages.setAdapter(imagesAdapter);
        binding.postImages.setNestedScrollingEnabled(false);
    }

    private void setupSort() {
        binding.sortContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker.send(AnalyticsUtils.getEvent(
                        getString(R.string.action),
                        getString(R.string.clicked_sort)
                ));
                presenter.onSortTypeClicked();
            }
        });
    }

    private void setupFab() {
        binding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShareButtonClicked();
            }
        });
    }

    private TreeNode addRepliesToNode(List<Comment> comments, TreeNode parentNode) {
        if (comments == null) {
            return parentNode;
        }
        for (Comment comment : comments) {
            TreeNode childNode = new TreeNode(new CommentViewHolder.Comment(
                    comment.getComment(),
                    comment.getAuthor(),
                    comment.getPoints(),
                    comment.getDatetime(),
                    comment.getChildren().size()
            ));
            childNode = addRepliesToNode(comment.getChildren(), childNode);
            parentNode.addChild(childNode);
        }
        return parentNode;
    }

    private void trackScreen() {
        ImgurApplication application = (ImgurApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName(getString(R.string.post_details));
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private Intent getShareIntent(String link) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
        shareIntent.setType(getString(R.string.type_text_plain));
        return shareIntent;
    }

    private class AddCommentSectionViewToLayoutTask extends AsyncTask<List<Comment>, Void, View> {

        @SafeVarargs
        @Override
        protected final View doInBackground(List<Comment>... args) {
            List<Comment> comments = args[0];

            TreeNode root = addRepliesToNode(comments, TreeNode.root());

            ImgurTreeView tView = new ImgurTreeView(getActivity(), root);
            tView.setDefaultAnimation(true);
            tView.setDefaultViewHolder(CommentViewHolder.class);
            tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
            tView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, Object value) {
                    tracker.send(AnalyticsUtils.getEvent(
                            getString(R.string.action),
                            getString(R.string.clicked_on_comment)
                    ));
                }
            });
            return tView.getView();
        }

        @Override
        protected void onPostExecute(View view) {
            binding.commentsContainer.addView(view);
            binding.sortContainer.setVisibility(View.VISIBLE);
            setCommentProgress(false);
        }
    }
}
