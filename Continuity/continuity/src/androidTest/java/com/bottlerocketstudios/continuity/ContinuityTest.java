package com.bottlerocketstudios.continuity;

import android.util.Log;

import com.bottlerocketstudios.continuity.model.ContinuousTestClass;
import com.bottlerocketstudios.continuity.model.TestAnchor;

import java.util.ArrayList;

/**
 * Created on 8/23/16.
 */
public class ContinuityTest {
    private ContinuityRepository mContinuityRepository;

    public synchronized ContinuityRepository getContinuityRepository() {
        if (mContinuityRepository == null) {
            mContinuityRepository = new ContinuityRepository();
            mContinuityRepository.setMinLoggingLevel(Log.VERBOSE);
        }
        return mContinuityRepository;
    }

    protected ContinuousTestClass getUnanchoredContinuousTestClass(int taskId) {
        return getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskId).build();
    }

    protected ContinuousTestClass getUnanchoredContinuousTestClass(int taskId, long customLifetimeMs) {
        return getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskId).lifetime(customLifetimeMs).build();
    }

    protected void createMemoryPressure() {
        ArrayList<byte[]> byteArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            byteArrayList.add(new byte[4096000]);
        }
    }
}
