package com.bottlerocketstudios.continuitysample.core.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.bottlerocketstudios.continuity.ContinuityRepository;
import com.bottlerocketstudios.continuitysample.core.injection.Injectable;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;

/**
 * Created on 9/13/16.
 */
public class BaseFragment extends Fragment {

    private ContinuityRepository mPresenterRepository;

    public BaseFragment() {
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

    public <T> T activityCastOrThrow(Context context, Class<T> castType) {
        if (castType.isInstance(context)) {
            return castType.cast(context);
        }
        throw new ClassCastException("Activity " + context.getClass().getCanonicalName() + " must implement " + castType.getCanonicalName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterRepository.onDestroy(this);
    }
}
