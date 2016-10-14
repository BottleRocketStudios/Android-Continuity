package com.bottlerocketstudios.continuitysample.util;

import android.os.Handler;
import android.os.Looper;

import com.bottlerocketstudios.groundcontrol.looper.LooperController;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created on 9/15/16.
 */
public class LooperTest {
    @Test
    public void testLooper() {
        Looper looper = LooperController.getLooper("Whatever");
        Handler handler = new Handler(looper);
        final ResultContainer<Boolean> testResultContainer = new ResultContainer<>();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testResultContainer.setResult(true);
            }
        }, 1000);

        int waitCount = 0;
        while (testResultContainer.getResult() == null && waitCount < 11) {
            SafeWait.safeWait(100);
            waitCount++;
        }

        Assert.assertNotNull("Looper did not work properly", testResultContainer.getResult());
        Assert.assertTrue("Looper did not work properly", testResultContainer.getResult());
    }
}
