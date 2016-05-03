package com.csabacsete.imgursmostviral.posts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.util.GlideUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ccsete on 4/8/16.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final PostItemListener mItemListener;
    private List<Post> mPosts;
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

        GlideUtils.loadImage(mContext, post.getThumbnail(), null, holder.thumbnail);
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

    public interface PostItemListener {

        void onPostClick(Post clickedPost);
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
}
