package com.nordlogic.imgursmostviral.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.nordlogic.imgursmostviral.data.models.Post;

/**
 * Created by ccsete on 4/22/16.
 */
public class GlideUtils {

    public static void loadImage(Context context, String url, String type, ImageView imageView) {
        DrawableTypeRequest<String> drawableTypeRequest = Glide.with(context).load(url);
        if (isGifType(type)) {
            drawableTypeRequest.asGif().into(imageView);
        } else {
            drawableTypeRequest.asBitmap().into(imageView);
        }
    }

    private static boolean isGifType(final String type) {
        return TextUtils.equals(Post.TYPE_GIF, type);
    }
}
