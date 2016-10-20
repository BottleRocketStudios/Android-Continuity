package com.bottlerocketstudios.continuitysample.core.databinding;

import android.databinding.DataBindingComponent;

/**
 * DataBindingComponent that will provide an ImageViewDataBindingAdapter that uses Glide.
 */
public class GlideDataBindingComponent implements DataBindingComponent {
    @Override
    public ImageViewBindingAdapter getImageViewBindingAdapter() {
        return new GlideImageViewBindingAdapter();
    }
}
