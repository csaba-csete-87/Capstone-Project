package com.csabacsete.imgursmostviral.data.db.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ImgurSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static ImgurSyncAdapter sImgurSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sImgurSyncAdapter == null) {
                sImgurSyncAdapter = new ImgurSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sImgurSyncAdapter.getSyncAdapterBinder();
    }
}