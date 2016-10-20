package com.bottlerocketstudios.continuitysample.core.databinding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Adapter that will handle imageUrl and imageUrl, error attribute combinations for ImageViews.
 */
public interface ImageViewBindingAdapter {
    @BindingAdapter("imageUrl")
    void loadImageUrl(ImageView imageView, String imageUrl);

    @BindingAdapter({"imageUrl", "error"})
    void loadImageUrl(ImageView imageView, String imageUrl, Drawable error);
}
