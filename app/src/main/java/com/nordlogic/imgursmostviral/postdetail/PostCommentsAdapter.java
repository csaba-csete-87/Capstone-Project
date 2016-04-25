package com.nordlogic.imgursmostviral.postdetail;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordlogic.imgursmostviral.R;
import com.nordlogic.imgursmostviral.data.models.Comment;
import com.nordlogic.imgursmostviral.util.DateTimeUtils;

import java.util.List;

/**
 * Created by ccsete on 4/8/16.
 */
public class PostCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CommentItemClickListener commentClickListener;
    private List<Comment> comments;
    private Context context;

    public PostCommentsAdapter(List<Comment> comments, CommentItemClickListener commentClickListener) {
        setComments(comments);
        this.commentClickListener = commentClickListener;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.item_comment, parent, false);

        return new CommentRowHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setupCommentRowItem((CommentRowHolder) holder, position);
    }

    private void setupCommentRowItem(CommentRowHolder holder, int position) {
        Comment c = comments.get(position);
        holder.comment.setText(c.getComment());
        holder.owner.setText(c.getAuthor());
        holder.points.setText(String.valueOf(c.getPoints()));
        holder.postedWhen.setText(DateTimeUtils.getReadableTimeElapsedSHort(c.getDatetime()));
        if (commentHasChildren(c)) {
            holder.replyCount.setText(getReplyText(c.getChildrenSize()));

            PostCommentsAdapter adapter = new PostCommentsAdapter(c.getChildren(), commentClickListener);
            holder.replies.setAdapter(adapter);
            holder.replies.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        } else {
            holder.replyCount.setVisibility(View.INVISIBLE);
        }
    }

    private String getReplyText(final int replyCount) {
        if (replyCount > 1) {
            return String.format(context.getString(R.string.replies_singular), replyCount);
        } else {
            return String.format(context.getString(R.string.replies_plural), replyCount);
        }
    }

    private boolean commentHasChildren(final Comment c) {
        return (c.getChildrenSize() > 0);
    }

    private Comment getItem(int position) {
        return comments.get(position);
    }

    public class CommentRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView comment;
        TextView owner;
        TextView points;
        TextView postedWhen;
        TextView replyCount;
        RecyclerView replies;

        public CommentRowHolder(View itemView) {
            super(itemView);

            comment = (TextView) itemView.findViewById(R.id.comment);
            owner = (TextView) itemView.findViewById(R.id.comment_owner);
            points = (TextView) itemView.findViewById(R.id.comment_points);
            postedWhen = (TextView) itemView.findViewById(R.id.comment_posted_when);
            replyCount = (TextView) itemView.findViewById(R.id.comment_reply_count);
            replies = (RecyclerView) itemView.findViewById(R.id.replies);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            commentClickListener.onCommentClicked(getItem(getAdapterPosition()).getId());
        }
    }

    public interface CommentItemClickListener {

        void onCommentClicked(int commentId);
    }
}
