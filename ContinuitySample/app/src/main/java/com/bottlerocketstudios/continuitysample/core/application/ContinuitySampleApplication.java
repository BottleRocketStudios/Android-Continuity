package com.bottlerocketstudios.continuitysample.core.application;

import android.app.Application;
import android.databinding.DataBindingUtil;

import com.bottlerocketstudios.continuitysample.core.databinding.PicassoDataBindingComponent;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInitializer;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;

public class ContinuitySampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Avoid adding a whole DI framework and all of that just to keep a few objects around.
        ServiceInitializer.initializeContext(this);

        /**
         * Set the default component for DataBinding to use Picasso. This could also be GlideDataBindingComponent();
         * If there is an actual need to decide at launch which DataBindingComponent to use, it can be done here.
         * Avoid repeatedly calling setDefaultComponent to switch on the fly. It will cause your application
         * to behave unpredictably and is not thread safe. If a different DataBindingComponent is needed for a
         * specific layout, you can specify that DataBindingComponent when you inflate the layout/setContentView.
         */
        DataBindingUtil.setDefaultComponent(new PicassoDataBindingComponent());

        /**
         * GroundControl's default UI policy will use a built in cache. It isn't necessary and actually
         * causes confusion as the Presenters serve this role.
         */
        GroundControl.disableCache();
    }
}
