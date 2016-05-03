package com.csabacsete.imgursmostviral.postdetail;


import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csabacsete.imgursmostviral.Injection;
import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.models.Comment;
import com.csabacsete.imgursmostviral.data.models.Image;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.databinding.FragmentPostDetailBinding;
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
        presenter.getPost(postId);
        presenter.getComments(postId, getString(R.string.best).toLowerCase());
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
    public void setTitle(final String title) {
        binding.title.setText(title);
    }

    @Override
    public void setPostedByUsername(final String username) {
        binding.postedByUsername.setText(username);
    }

    @Override
    public void setPoints(final String points) {
        binding.points.setText(points);
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
        binding.commentsContainer.removeAllViews();
        TreeNode root = addRepliesToNode(comments, TreeNode.root());

        ImgurTreeView tView = new ImgurTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultViewHolder(CommentViewHolder.class);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);

        binding.commentsContainer.addView(tView.getView());
    }

    @Override
    public void startShareActionProvider(String link) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        startActivity(sendIntent);
    }

    @Override
    public void showSelectSortTypeDialog() {
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(Post.SORT_TYPES, 0, new DialogInterface.OnClickListener() {
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
    public void startLargeImageViewer(String path) {
        // TODO: 5/2/16 start shared element intent
    }

    private void setupViews() {
        setupToolbar();
        setupGallery(); // TODO: 5/2/16 on item click listener for gallery
        setupSort();
        setupFab();
    }

    private void setupToolbar() {
        PostDetailActivity activity = ((PostDetailActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setupGallery() {
        imagesAdapter = new PostImagesAdapter(null);

        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setOrientation(LinearLayoutManager.VERTICAL);

        binding.postImages.setLayoutManager(llm1);
        binding.postImages.setAdapter(imagesAdapter);
    }

    private void setupSort() {
        binding.sortContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


}
