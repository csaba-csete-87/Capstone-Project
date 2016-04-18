package com.nordlogic.imgursmostviral.postdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nordlogic.imgursmostviral.R;
import com.nordlogic.imgursmostviral.data.models.Comment;
import com.nordlogic.imgursmostviral.data.models.Post;

/**
 * Created by ccsete on 4/8/16.
 */
public class PostDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_ALBUM_IMAGE = 0;
    private static final int ITEM_TYPE_COMMENT_HEADER = 1;
    private static final int ITEM_TYPE_COMMENT = 2;

    private Post post;
    private Context mContext;

    public PostDetailsAdapter(Post post) {
        setPost(post);
    }

    private void setPost(Post post) {
        this.post = post;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType;
        if (post.isAlbum()) {
            itemViewType = getAlbumPostItemViewType(position);
        } else {
            itemViewType = getImagePostItemViewType(position);
        }

        return itemViewType;
    }

    @Override
    public int getItemCount() {
        if (post.isAlbum()) {
            return getAlbumPhotoSectionCount() + 1 + getCommentSectionCount(); // album images + comment header + comments
        } else {
            return 1 + getCommentSectionCount(); //header + comment header + comments
        }
    }

    private int getAlbumPhotoSectionCount() {
        return Math.min(post.getImagesCount(), Post.PAGE_COUNT);
    }

    private int getCommentSectionCount() {
        return Math.min(post.getCommentCount(), Post.PAGE_COUNT);
    }

    private int getAlbumPostItemViewType(int position) {
        return getItemViewTypeByImageCount(position, getAlbumPhotoSectionCount());
    }

    private int getItemViewTypeByImageCount(int position, int imageCount) {
        int itemViewType;
        if (position < imageCount) {
            itemViewType = ITEM_TYPE_ALBUM_IMAGE;
        } else if (position == imageCount) {
            itemViewType = ITEM_TYPE_COMMENT_HEADER;
        } else {
            itemViewType = ITEM_TYPE_COMMENT;
        }
        return itemViewType;
    }

    private int getImagePostItemViewType(int position) {
        int itemViewType;
        if (position == 0) {
            itemViewType = ITEM_TYPE_COMMENT_HEADER;
        } else {
            itemViewType = ITEM_TYPE_COMMENT;
        }
        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View rowView;
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case ITEM_TYPE_ALBUM_IMAGE:
                rowView = inflater.inflate(R.layout.item_album_image, parent, false);
                viewHolder = new AlbumImageHolder(rowView);
                break;
            case ITEM_TYPE_COMMENT_HEADER:
                rowView = inflater.inflate(R.layout.item_comment_header, parent, false);
                viewHolder = new CommentHeaderHolder(rowView);
                break;
            case ITEM_TYPE_COMMENT:
                rowView = inflater.inflate(R.layout.item_comment, parent, false);
                viewHolder = new CommentRowHolder(rowView);
                break;
            default:
                throw new RuntimeException(mContext.getResources().getString(R.string.invalid_item_type));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_ALBUM_IMAGE:
                setupAlbumImageItem((AlbumImageHolder) holder, position);
                break;
            case ITEM_TYPE_COMMENT_HEADER:
                setupCommentHeaderItem((CommentHeaderHolder) holder, position);
                break;
            case ITEM_TYPE_COMMENT:
                setupCommentRowItem((CommentRowHolder) holder, position);
                break;
            default:
                throw new RuntimeException(mContext.getResources().getString(R.string.invalid_item_type));
        }
    }

    private void setupAlbumImageItem(AlbumImageHolder holder, int position) {

    }

    private void setupCommentHeaderItem(CommentHeaderHolder holder, int position) {

    }

    private void setupCommentRowItem(CommentRowHolder holder, int position) {
        Comment c = getCommentByPosition(position);
        holder.comment.setText(c.getComment());
    }

    private Comment getCommentByPosition(int position) {
        return post.getComments().get(position - getCommentSectionOffset());
    }

    private int getCommentSectionOffset() {
        return (post.isAlbum()) ? getAlbumPhotoSectionCount() + 1 : 1;
    }

    public class AlbumImageHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView thumbnail;

        public AlbumImageHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            thumbnail = (ImageView) itemView.findViewById(R.id.post_thumbnail);
        }
    }

    public class CommentHeaderHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView thumbnail;

        public CommentHeaderHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            thumbnail = (ImageView) itemView.findViewById(R.id.post_thumbnail);
        }
    }

    public class CommentRowHolder extends RecyclerView.ViewHolder {

        TextView comment;

        public CommentRowHolder(View itemView) {
            super(itemView);

            comment = (TextView) itemView.findViewById(R.id.comment);
        }
    }
}
