package com.bottlerocketstudios.continuitysample.core.application;

import android.app.Application;
import android.databinding.DataBindingUtil;

import com.bottlerocketstudios.continuitysample.core.databinding.PicassoDataBindingComponent;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInitializer;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;

/**
 * Created on 9/13/16.
 */
public class ContinuitySampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ServiceInitializer.initializeContext(this);
        DataBindingUtil.setDefaultComponent(new PicassoDataBindingComponent());
        GroundControl.disableCache();
    }
}
