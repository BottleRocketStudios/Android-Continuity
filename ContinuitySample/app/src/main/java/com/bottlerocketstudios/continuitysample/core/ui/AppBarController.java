package com.bottlerocketstudios.continuitysample.core.ui;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Helper class to coordinate operations on the Toolbar.
 */
public final class AppBarController {
    private final ActionBar mActionBar;
    private final Toolbar mToolbar;

    public AppBarController(AppCompatActivity appCompatActivity, Toolbar toolbar) {
        mToolbar = toolbar;
        appCompatActivity.setSupportActionBar(toolbar);
        mActionBar = appCompatActivity.getSupportActionBar();
    }

    public AppBarController setAppBarTitle(@StringRes int resId) {
        mActionBar.setTitle(resId);
        return this;
    }

    public AppBarController setAppBarTitle(String title) {
        mActionBar.setTitle(title);
        return this;
    }

    public AppBarController setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
        return this;
    }

    public AppBarController setHomeAsUpIndicator(@DrawableRes int resId) {
        mActionBar.setHomeAsUpIndicator(resId);
        return this;
    }

    public AppBarController setNavigationIcon(@DrawableRes int resId) {
        mToolbar.setNavigationIcon(resId);
        return this;
    }
}