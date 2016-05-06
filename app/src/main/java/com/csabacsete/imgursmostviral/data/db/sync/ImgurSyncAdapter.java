package com.csabacsete.imgursmostviral.data.db.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.csabacsete.imgursmostviral.Injection;
import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.db.ImgurContract;
import com.csabacsete.imgursmostviral.data.models.Post;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepository;
import com.csabacsete.imgursmostviral.postdetail.PostDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Vector;

public class ImgurSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 30;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 10;
    private static final int NOTIFICATION_ID = 100;

    public ImgurSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        ImgurSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        PostsRepository repository = Injection.provideServerRepository();

        repository.getPosts(new PostsRepository.GetPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                parseResponse(posts);
            }
        });
    }

    private void parseResponse(List<Post> posts) {
        Vector<ContentValues> postsVector = new Vector<>(posts.size());

        for (Post p : posts) {
            ContentValues postValues = new ContentValues();

            postValues.put(ImgurContract.PostEntry.COLUMN_POST_ID, p.getId());
            postValues.put(ImgurContract.PostEntry.COLUMN_TITLE, p.getTitle());
            postValues.put(ImgurContract.PostEntry.COLUMN_DESCRIPTION, p.getDescription());
            postValues.put(ImgurContract.PostEntry.COLUMN_LINK, p.getLink());
            postValues.put(ImgurContract.PostEntry.COLUMN_TYPE, p.getType());
            postValues.put(ImgurContract.PostEntry.COLUMN_GIFV, p.getGifv());
            postValues.put(ImgurContract.PostEntry.COLUMN_DATETIME, p.getDatetime());
            postValues.put(ImgurContract.PostEntry.COLUMN_COVER, p.getCover());
            postValues.put(ImgurContract.PostEntry.COLUMN_POINTS, p.getPoints());
            postValues.put(ImgurContract.PostEntry.COLUMN_IMAGES_COUNT, p.getImagesCount());
            postValues.put(ImgurContract.PostEntry.COLUMN_COMMENT_COUNT, p.getCommentCount());
            postValues.put(ImgurContract.PostEntry.COLUMN_IS_ALBUM, p.isAlbum());
            postValues.put(ImgurContract.PostEntry.COLUMN_ACCOUNT_URL, p.getAccountUrl());

            postsVector.add(postValues);
        }

        if (postsVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[postsVector.size()];
            postsVector.toArray(cvArray);

            getContext().getContentResolver().delete(
                    ImgurContract.PostEntry.CONTENT_URI,
                    null,
                    null
            );

            getContext().getContentResolver().bulkInsert(
                    ImgurContract.PostEntry.CONTENT_URI,
                    cvArray
            );
        }

        Post newTopPost = posts.get(0);
        String newTopPostId = newTopPost.getId();
        String currentTopPostId = getCurrentTopPostIdFromSharedPreferences();
        if (!TextUtils.isEmpty(currentTopPostId)) {
            if (!TextUtils.equals(currentTopPostId, newTopPostId)) {
                new IssueNotificationTask().execute(newTopPost);
            }
        } else {
            setCurrentTopPostIdToSharedPreferences(newTopPostId);
        }
    }

    private PendingIntent getPendingIntentForPost(Post post) {
        Intent notificationIntent = new Intent(getContext(), PostDetailActivity.class);
        notificationIntent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.getId());
        return PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private String getCurrentTopPostIdFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences();
        return sharedPref.getString(getContext().getString(R.string.preference_key_top_post), "");
    }

    private void setCurrentTopPostIdToSharedPreferences(final String id) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getContext().getString(R.string.preference_key_top_post), id);
        editor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(getContext().getString(R.string.preference_key_imgur), Context.MODE_PRIVATE);
    }

    class IssueNotificationTask extends AsyncTask<Post, Void, Notification> {

        @Override
        protected Notification doInBackground(Post... posts) {
            Post post = posts[0];
            PendingIntent pendingIntent = getPendingIntentForPost(post);
            Bitmap b = ImageLoader.getInstance().loadImageSync(post.getThumbnail());

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getContext())
                            .setSmallIcon(R.drawable.ic_notification)
                            .setLargeIcon(b)
                            .setContentTitle("A new post is trending!")
                            .setContentText(post.getTitle())
                            .setContentIntent(pendingIntent);
            return builder.build();
        }

        @Override
        protected void onPostExecute(Notification notification) {
            NotificationManager mNotifyMgr = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(NOTIFICATION_ID, notification);
        }
    }
}