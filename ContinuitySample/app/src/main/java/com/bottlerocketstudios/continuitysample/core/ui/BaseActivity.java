package com.bottlerocketstudios.continuitysample.core.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bottlerocketstudios.continuity.ContinuityRepository;
import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.injection.Injectable;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;
import com.bottlerocketstudios.continuitysample.databinding.BaseActivityBinding;

/**
 * Created on 9/13/16.
 */
public class BaseActivity extends AppCompatActivity {

    private ContinuityRepository mPresenterRepository;
    protected AppBarController mAppBarController;
    protected Toolbar mToolbar;

    public BaseActivity() {
        ServiceInjector.injectWithType(ContinuityRepository.class, new Injectable<ContinuityRepository>() {
            @Override
            public void receiveInjection(ContinuityRepository injection) {
                mPresenterRepository = injection;
            }
        });
    }

    protected ContinuityRepository getPresenterRepository() {
        return mPresenterRepository;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindActivity();
        initializeAppBarController(getToolbar());
    }

    /**
     * Override this method to replace the base_activity layout and databinding.
     */
    protected void bindActivity() {
        BaseActivityBinding baseActivityBinding = DataBindingUtil.setContentView(this, R.layout.base_activity);
        mToolbar = baseActivityBinding.baseToolbar;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    private void initializeAppBarController(Toolbar toolbar) {
        mAppBarController = new AppBarController(this, toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterRepository.onDestroy(this);
    }
}
