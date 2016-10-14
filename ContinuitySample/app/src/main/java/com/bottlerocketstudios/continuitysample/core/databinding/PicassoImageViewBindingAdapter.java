package com.bottlerocketstudios.continuitysample.core.databinding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created on 10/3/16.
 */

public class PicassoImageViewBindingAdapter implements ImageViewBindingAdapter {
    @Override
    public void loadImageUrl(ImageView imageView, String imageUrl) {
        Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    @Override
    public void loadImageUrl(ImageView imageView, String imageUrl, Drawable error) {
        Picasso.with(imageView.getContext()).load(imageUrl).error(error).into(imageView);
    }
}
