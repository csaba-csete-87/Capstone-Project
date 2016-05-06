package com.csabacsete.imgursmostviral.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.db.ImgurDbHelper;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ccsete on 5/4/16.
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final ImgurDbHelper dbHelper;
    private Context context = null;
    private int appWidgetId;
    private List<Post> posts;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        dbHelper = new ImgurDbHelper(context);
    }

    private void updateWidgetListView() {
        this.posts = dbHelper.getAllPosts();
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        Post p = posts.get(position);

        remoteView.setTextViewText(R.id.title, p.getTitle());

        Bitmap b = ImageLoader.getInstance().loadImageSync(p.getThumbnail());
        remoteView.setImageViewBitmap(R.id.icon, b);

//        Intent notificationIntent = new Intent(context, PostDetailActivity.class);
//        notificationIntent.putExtra(PostDetailActivity.EXTRA_POST_ID, p.getId());
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        remoteView.setOnClickPendingIntent(remoteView.getLayoutId(), pendingIntent);

        return remoteView;
    }

//    protected PendingIntent getPendingSelfIntent(Context context, String postId) {
//        Intent intent = new Intent(context, getClass());
//        intent.putExtra("postId", postId);
//        return PendingIntent.getBroadcast(context, 0, intent, 0);
//    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onCreate() {
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged() {
        updateWidgetListView();
    }

    @Override
    public void onDestroy() {
        posts.clear();
    }
}