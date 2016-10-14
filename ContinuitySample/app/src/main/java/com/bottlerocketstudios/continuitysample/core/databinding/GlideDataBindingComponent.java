package com.bottlerocketstudios.continuitysample.core.databinding;

import android.databinding.DataBindingComponent;

/**
 * Created on 10/3/16.
 */

public class GlideDataBindingComponent implements DataBindingComponent {
    @Override
    public ImageViewBindingAdapter getImageViewBindingAdapter() {
        return new GlideImageViewBindingAdapter();
    }
}
