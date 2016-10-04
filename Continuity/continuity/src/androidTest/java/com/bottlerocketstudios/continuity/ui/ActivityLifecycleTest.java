package com.bottlerocketstudios.continuity.ui;

import android.os.Handler;
import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;

import com.bottlerocketstudios.continuity.ContinuityRepository;
import com.bottlerocketstudios.continuity.ContinuityTest;
import com.bottlerocketstudios.continuity.model.ContinuousTestClass;
import com.bottlerocketstudios.continuity.util.SafeWait;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created on 10/4/16.
 */
@RunWith(AndroidJUnit4.class)
public class ActivityLifecycleTest extends ContinuityTest {

    @Test
    public void testFinishing() {
        final Container<ContinuousTestClass> continuousTestClassContainer = new Container<>();

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                FinishingActivity activity = new FinishingActivity();
                ContinuityRepository continuityRepository = getContinuityRepository();
                continuousTestClassContainer.setValue(continuityRepository.with(activity, ContinuousTestClass.class).build());
                activity.setContinuityRepository(continuityRepository);
                activity.simulateFinish();
            }
        });

        SafeWait.safeWait(ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 2);

        Assert.assertTrue("Object was not notified of destruction", continuousTestClassContainer.getValue().isDestroyed());
        Assert.assertTrue("Object was not notified of discard", continuousTestClassContainer.getValue().isDiscarded());
    }

    private static class Container<T> {
        private T mValue;

        public void setValue(T value) {
            mValue = value;
        }

        public T getValue() {
            return mValue;
        }
    }

}
