package com.csabacsete.imgursmostviral.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.models.Post;

/**
 * Created by ccsete on 4/22/16.
 */
public class GlideUtils {

    public static void loadImage(Context context, String url, String type, ImageView imageView) {
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(context).load(url);
        if (isGifType(type)) {
            drawableTypeRequest
                    .asGif()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_placeholder))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(imageView);
        } else {
            drawableTypeRequest
                    .asBitmap()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_placeholder))
                    .into(imageView);
        }
    }

    private static boolean isGifType(final String type) {
        return TextUtils.equals(Post.TYPE_GIF, type);
    }
}
