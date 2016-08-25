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
 * Created on 8/25/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestOneAnchorToManyContinuous extends ContinuityTest {

    @Test
    public void testOneToMany() {
        int taskId = SequentialNumberGenerator.generateNumber();
        TestAnchor testAnchor = new TestAnchor();

        ContinuousTestClass typeOneA = getContinuityRepository().with(testAnchor, ContinuousTestClass.class).task(taskId).build();

        SimpleTestClass typeTwo = getContinuityRepository().with(testAnchor, SimpleTestClass.class).task(taskId).build();

        SafeWait.safeWait(ContinuityRepository.DEFAULT_LIFETIME_MS + ContinuityRepository.DEFAULT_CHECK_INTERVAL_MS * 2);

        ContinuousTestClass typeOneB = getContinuityRepository().with(testAnchor, ContinuousTestClass.class).task(taskId).build();

        Assert.assertEquals("First object should not have been released while a strong reference to the anchor was held and a second object associated to the anchor.", typeOneA, typeOneB);
    }

    public static class SimpleTestClass {

        public SimpleTestClass() {
            //NOOP
        }
    }

}
