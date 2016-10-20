package com.bottlerocketstudios.continuitysample.core.databinding;

/**
 * DataBindingComponent that will provide an ImageViewDataBindingAdapter that uses Picasso.
 */
public class PicassoDataBindingComponent implements android.databinding.DataBindingComponent {
    @Override
    public ImageViewBindingAdapter getImageViewBindingAdapter() {
        return new PicassoImageViewBindingAdapter();
    }
}
