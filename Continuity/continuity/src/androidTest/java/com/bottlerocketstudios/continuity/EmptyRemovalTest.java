package com.bottlerocketstudios.continuity;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created on 9/15/16.
 */
@RunWith(AndroidJUnit4.class)
public class EmptyRemovalTest extends ContinuityTest {
    @Test
    public void testDestructionOfUntrackedObject() {
        getContinuityRepository().onDestroy(this);
    }

    @Test
    public void testImproperUseOfNull() {
        getContinuityRepository().onDestroy(null);
    }
}
