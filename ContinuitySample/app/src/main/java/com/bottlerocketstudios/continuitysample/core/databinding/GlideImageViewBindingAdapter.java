package com.bottlerocketstudios.continuitysample.core.databinding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created on 10/3/16.
 */

public class GlideImageViewBindingAdapter implements ImageViewBindingAdapter {

    @Override
    public void loadImageUrl(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
    }

    @Override
    public void loadImageUrl(ImageView imageView, String imageUrl, Drawable error) {
        Glide.with(imageView.getContext()).load(imageUrl).error(error).into(imageView);
    }
}
