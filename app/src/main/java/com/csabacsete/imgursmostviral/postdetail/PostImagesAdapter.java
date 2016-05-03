package com.csabacsete.imgursmostviral.postdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csabacsete.imgursmostviral.R;
import com.csabacsete.imgursmostviral.data.models.Image;
import com.csabacsete.imgursmostviral.util.GlideUtils;

import java.util.List;

/**
 * Created by ccsete on 4/8/16.
 */
public class PostImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Image> images;
    private Context context;

    public PostImagesAdapter(List<Image> images) {
        setImages(images);
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.item_album_image, parent, false);
        return new AlbumImageHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setupAlbumImageItem((AlbumImageHolder) holder, position);
    }

    private void setupAlbumImageItem(AlbumImageHolder holder, int position) {
        Image image = images.get(position);
        GlideUtils.loadImage(context, image.getLink(), image.getType(), holder.image);

        if (imageHasDescription(image)) {
            holder.description.setText(image.getDescription());
        } else {
            holder.description.setVisibility(View.GONE);
        }
    }

    private boolean imageHasDescription(Image image) {
        return (image.getDescription() != null && !TextUtils.isEmpty(image.getDescription()));
    }

    public class AlbumImageHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView description;

        public AlbumImageHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            description = (TextView) itemView.findViewById(R.id.image_description);
        }
    }
}
