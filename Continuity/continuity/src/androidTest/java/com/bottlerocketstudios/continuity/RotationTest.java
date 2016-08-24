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
public class RotationTest extends ContinuityTest {
    private static final String TAG = RotationTest.class.getSimpleName();

    @Test
    public void testRotationRetentionUnderLifetime() {
        int taskId = SequentialNumberGenerator.generateNumber();
        ContinuousTestClass beforeRotation = getUnanchoredContinuousTestClass(taskId);

        createMemoryPressure();
        System.gc();
        SafeWait.safeWait(ContinuityRepository.DEFAULT_LIFETIME_MS - 500);

        ContinuousTestClass afterRotation = getUnanchoredContinuousTestClass(taskId);

        Assert.assertEquals("Instance was not retained after rotation", beforeRotation, afterRotation);
    }

    @Test
    public void testRotationRemovalPastLifetime() {
        int taskId = SequentialNumberGenerator.generateNumber();
        ContinuousTestClass beforeRotation = getUnanchoredContinuousTestClass(taskId);

        createMemoryPressure();
        System.gc();
        SafeWait.safeWait(ContinuityRepository.DEFAULT_LIFETIME_MS + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 2);

        ContinuousTestClass afterRotation = getUnanchoredContinuousTestClass(taskId);

        Assert.assertNotEquals("Instance was retained after expiration", beforeRotation, afterRotation);
        Assert.assertTrue("Original instance was not notified of being discarded", beforeRotation.isDiscarded());
    }

    @Test
    public void testRetentionPastLifetime() {
        int taskId = SequentialNumberGenerator.generateNumber();
        TestAnchor testAnchor = new TestAnchor();

        ContinuousTestClass beforeMemoryPressure = getContinuityRepository().with(testAnchor, ContinuousTestClass.class).task(taskId).build();

        createMemoryPressure();
        System.gc();
        SafeWait.safeWait(ContinuityRepository.DEFAULT_LIFETIME_MS + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 2);

        ContinuousTestClass afterMemoryPressure = getContinuityRepository().with(testAnchor, ContinuousTestClass.class).task(taskId).build();

        Assert.assertEquals("Object was removed while a strong reference to anchor was held.", beforeMemoryPressure, afterMemoryPressure);
    }

}
