package com.csabacsete.imgursmostviral.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by ccsete on 5/4/16.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }

}