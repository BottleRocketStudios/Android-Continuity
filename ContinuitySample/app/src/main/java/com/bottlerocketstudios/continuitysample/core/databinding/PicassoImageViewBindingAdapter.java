package com.bottlerocketstudios.continuitysample.core.databinding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Implementation of ImageViewBindingAdapter that will use Picasso to fulfill requests.
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
