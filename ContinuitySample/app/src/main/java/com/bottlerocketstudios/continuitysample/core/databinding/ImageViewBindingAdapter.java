package com.bottlerocketstudios.continuitysample.core.databinding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created on 10/3/16.
 */

public interface ImageViewBindingAdapter {
    @BindingAdapter("bind:imageUrl")
    void loadImageUrl(ImageView imageView, String imageUrl);

    @BindingAdapter({"bind:imageUrl", "bind:error"})
    void loadImageUrl(ImageView imageView, String imageUrl, Drawable error);
}
