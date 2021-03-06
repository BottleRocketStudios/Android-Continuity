package com.bottlerocketstudios.continuitysample.core.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.bottlerocketstudios.continuity.ContinuityRepository;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;

/**
 * Created on 9/13/16.
 */
public class BaseFragment extends Fragment {

    private ContinuityRepository mPresenterRepository;

    public BaseFragment() {
        mPresenterRepository = ServiceInjector.resolve(ContinuityRepository.class);
    }

    protected ContinuityRepository getPresenterRepository() {
        return mPresenterRepository;
    }

    /**
     * Convenience method to wrap casting an Activity to a listener interface type onAttach.
     */
    public <T> T activityCastOrThrow(Context context, Class<T> castType) {
        if (castType.isInstance(context)) {
            return castType.cast(context);
        }
        throw new ClassCastException("Activity " + context.getClass().getCanonicalName() + " must implement " + castType.getCanonicalName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Let the repository know that this Fragment is being destroyed so that Presenters associated with it are notified.
        mPresenterRepository.onDestroy(this);
    }
}
