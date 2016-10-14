package com.bottlerocketstudios.continuitysample.core.databinding;

/**
 * Created on 10/3/16.
 */

public class PicassoDataBindingComponent implements android.databinding.DataBindingComponent {
    @Override
    public ImageViewBindingAdapter getImageViewBindingAdapter() {
        return new PicassoImageViewBindingAdapter();
    }
}
