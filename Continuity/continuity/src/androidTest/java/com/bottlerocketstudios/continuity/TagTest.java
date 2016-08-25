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
public class TagTest extends ContinuityTest {

    @Test
    public void testTagDifferent() {
        int taskId = SequentialNumberGenerator.generateNumber();
        String tagA = "tagA";
        String tagB = "tagB";

        ContinuousTestClass tagObjectA = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskId).tag(tagA).build();
        ContinuousTestClass tagObjectB = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskId).tag(tagB).build();

        Assert.assertNotEquals("Objects from two separate tags are equal", tagObjectA, tagObjectB);
    }

    @Test
    public void testTagSame() {
        int taskId = SequentialNumberGenerator.generateNumber();
        String tagA = "tagA";

        ContinuousTestClass tagObjectA1 = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskId).tag(tagA).build();
        ContinuousTestClass tagObjectA2 = getContinuityRepository().with(new TestAnchor(), ContinuousTestClass.class).task(taskId).tag(tagA).build();

        Assert.assertEquals("Objects from same tag were not equal", tagObjectA1, tagObjectA2);
    }
}
