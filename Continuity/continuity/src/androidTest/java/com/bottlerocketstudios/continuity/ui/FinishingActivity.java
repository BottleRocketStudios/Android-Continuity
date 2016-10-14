package com.bottlerocketstudios.continuity.ui;

import android.app.Activity;

import com.bottlerocketstudios.continuity.ContinuityRepository;

/**
 * Created on 10/4/16.
 */

public class FinishingActivity extends Activity {

    private ContinuityRepository mContinuityRepository;

    public void setContinuityRepository(ContinuityRepository continuityRepository) {
        mContinuityRepository = continuityRepository;
    }

    public void simulateFinish() {
        finish();
        mContinuityRepository.onDestroy(this);
    }

}
