package com.csabacsete.imgursmostviral.postdetail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.util.DateTimeUtils;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by ccsete on 4/29/16.
 */
public class CommentViewHolder extends TreeNode.BaseNodeViewHolder<CommentViewHolder.Comment> {

    Context context;
    TextView comment;
    TextView owner;
    TextView points;
    TextView postedWhen;
    TextView replyCount;

    public CommentViewHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View createNodeView(TreeNode node, Comment c) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View itemView = inflater.inflate(R.layout.item_comment, null, false);

        comment = (TextView) itemView.findViewById(R.id.comment);
        owner = (TextView) itemView.findViewById(R.id.comment_owner);
        points = (TextView) itemView.findViewById(R.id.comment_points);
        postedWhen = (TextView) itemView.findViewById(R.id.comment_posted_when);
        replyCount = (TextView) itemView.findViewById(R.id.comment_reply_count);

        comment.setText(c.getComment());
        owner.setText(c.getAuthor());
        points.setText(String.valueOf(c.getPoints()));
        postedWhen.setText(DateTimeUtils.getReadableTimeElapsedShort(c.getDatetime()));
        if (commentHasChildren(c)) {
            replyCount.setText(getReplyText(c.getChildrenSize()));
        } else {
            replyCount.setVisibility(View.INVISIBLE);
        }
        return itemView;
    }

    @Override
    public void toggle(boolean active) {
        replyCount.setCompoundDrawablesWithIntrinsicBounds(
                active ? ContextCompat.getDrawable(context, R.drawable.ic_expand_less) : ContextCompat.getDrawable(context, R.drawable.ic_expand_more),
                null,
                null,
                null
        );
    }

    private String getReplyText(final int replyCount) {
        if (replyCount > 1) {
            return String.format(context.getString(R.string.replies_plural), replyCount);
        } else {
            return String.format(context.getString(R.string.replies_singular), replyCount);
        }
    }

    private boolean commentHasChildren(final Comment c) {
        return (c.getChildrenSize() > 0);
    }

    public static class Comment {
        private String comment;
        private String author;
        private int points;
        private long datetime;
        private int childrenSize;

        public Comment(String comment, String author, int points, long datetime, int childrenSize) {
            this.comment = comment;
            this.author = author;
            this.points = points;
            this.datetime = datetime;
            this.childrenSize = childrenSize;
        }

        public String getComment() {
            return comment;
        }

        public String getAuthor() {
            return author;
        }

        public int getPoints() {
            return points;
        }

        public long getDatetime() {
            return datetime;
        }

        public int getChildrenSize() {
            return childrenSize;
        }
    }

}
