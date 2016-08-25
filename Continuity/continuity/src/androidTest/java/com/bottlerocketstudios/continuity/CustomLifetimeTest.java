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
 * Created on 8/24/16.
 */
@RunWith(AndroidJUnit4.class)
public class CustomLifetimeTest extends ContinuityTest {

    private static final long TEST_LIFETIME_MS = ContinuityRepository.DEFAULT_LIFETIME_MS + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 4;

    @Test
    public void testCustomLifetimeDiscard() {
        int taskId = SequentialNumberGenerator.generateNumber();
        ContinuousTestClass beforeRotation = getUnanchoredContinuousTestClass(taskId, TEST_LIFETIME_MS);

        createMemoryPressure();
        System.gc();
        SafeWait.safeWait(TEST_LIFETIME_MS + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 2);

        ContinuousTestClass afterRotation = getUnanchoredContinuousTestClass(taskId);

        Assert.assertNotEquals("Instance was retained after custom expiration", beforeRotation, afterRotation);
        Assert.assertTrue("Original instance was not notified of being discarded", beforeRotation.isDiscarded());
    }

    @Test
    public void testCustomLifetimeRetained() {
        int taskId = SequentialNumberGenerator.generateNumber();
        ContinuousTestClass beforeRotation = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).lifetime(TEST_LIFETIME_MS).task(taskId).build();

        createMemoryPressure();
        System.gc();
        SafeWait.safeWait(ContinuityRepository.DEFAULT_LIFETIME_MS + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 2);

        ContinuousTestClass afterRotation = getUnanchoredContinuousTestClass(taskId);

        Assert.assertEquals("Instance was not retained with custom expiration", beforeRotation, afterRotation);
    }
}
