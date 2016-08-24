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
public class TaskIdTest extends ContinuityTest {

    @Test
    public void testTaskIdDifferent() {
        int taskIdA = SequentialNumberGenerator.generateNumber();
        int taskIdB = SequentialNumberGenerator.generateNumber();
        ContinuousTestClass taskA = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskIdA).build();
        ContinuousTestClass taskB = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskIdB).build();

        Assert.assertNotEquals("Objects from two separate task Ids are equal", taskA, taskB);
    }

    @Test
    public void testTaskIdSame() {
        int taskIdA = SequentialNumberGenerator.generateNumber();
        ContinuousTestClass taskA1 = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskIdA).build();
        ContinuousTestClass taskA2 = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskIdA).build();

        Assert.assertEquals("Objects from same task were not equal", taskA1, taskA2);
    }
}
