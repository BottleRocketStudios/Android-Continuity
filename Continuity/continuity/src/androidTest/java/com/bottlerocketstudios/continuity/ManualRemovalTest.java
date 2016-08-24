package com.bottlerocketstudios.continuity;

import android.support.test.runner.AndroidJUnit4;

import com.bottlerocketstudios.continuity.model.ContinuousTestClass;
import com.bottlerocketstudios.continuity.model.TestAnchor;
import com.bottlerocketstudios.continuity.util.SequentialNumberGenerator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created on 8/23/16.
 */
@RunWith(AndroidJUnit4.class)
public class ManualRemovalTest extends ContinuityTest {

    @Test
    public void testManualRemoval() {
        int taskId = SequentialNumberGenerator.generateNumber();
        TestAnchor testAnchor = new TestAnchor();

        ContinuousTestClass beforeRemoval = getContinuityRepository().with(testAnchor, ContinuousTestClass.class).task(taskId).build();

        getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskId).remove();

        ContinuousTestClass afterRemoval = getContinuityRepository().with(testAnchor, ContinuousTestClass.class).task(taskId).build();

        Assert.assertNotEquals("Instance was retained after removal", beforeRemoval, afterRemoval);
    }
}
