package com.bottlerocketstudios.continuity;

import android.support.test.runner.AndroidJUnit4;

import com.bottlerocketstudios.continuity.model.ContinuousTestClass;
import com.bottlerocketstudios.continuity.model.TestAnchor;
import com.bottlerocketstudios.continuity.util.SafeWait;
import com.bottlerocketstudios.continuity.util.SequentialNumberGenerator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created on 8/23/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestShutdownTimeouts extends ContinuityTest {

    @Test
    public void testEmptyTimeout() {
        int taskId = SequentialNumberGenerator.generateNumber();
        ContinuousTestClass oldReference = getUnanchoredContinuousTestClass(taskId);

        int emptyPasses = 0;
        while(!getContinuityRepository().isEmpty() && emptyPasses < 5) {
            createMemoryPressure();
            System.gc();
            SafeWait.safeWait(ContinuityRepository.DEFAULT_LIFETIME_MS + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 2);
            emptyPasses++;
        }
        Assert.assertTrue("Repository was not emptied in time", getContinuityRepository().isEmpty());

        int passes = 0;
        while(getContinuityRepository().isRunning() && passes <= ContinuityRepository.DEFAULT_MAX_EMPTY_ITERATIONS) {
            SafeWait.safeWait(ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS + 100);
            passes++;
        }
        Assert.assertFalse("Repository was still running after being empty for too long.", getContinuityRepository().isRunning());
    }

    @Test
    public void testIdleTimeout() {
        int taskId = SequentialNumberGenerator.generateNumber();
        TestAnchor testAnchor = new TestAnchor();

        long fastTimeoutMs = 5000;
        ContinuityRepository fastTimeoutRepository = new ContinuityRepository(ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS, fastTimeoutMs, ContinuityRepository.DEFAULT_LIFETIME_MS, ContinuityRepository.DEFAULT_MAX_EMPTY_ITERATIONS);

        ContinuousTestClass oldReference = fastTimeoutRepository.with(testAnchor, ContinuousTestClass.class).task(taskId).build();

        int emptyPasses = 0;
        while(fastTimeoutRepository.isRunning() && emptyPasses < 2) {
            SafeWait.safeWait(fastTimeoutMs + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS);
            emptyPasses++;
        }
        Assert.assertFalse("Repository was still running after being idle for too long.", fastTimeoutRepository.isRunning());

        ContinuousTestClass newReference = fastTimeoutRepository.with(testAnchor, ContinuousTestClass.class).task(taskId).build();
        Assert.assertEquals("Object was cleaned while a strong anchor was held", oldReference, newReference);
    }

}
