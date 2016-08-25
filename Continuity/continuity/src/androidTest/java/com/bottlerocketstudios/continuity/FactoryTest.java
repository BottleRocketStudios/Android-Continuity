package com.bottlerocketstudios.continuity;

import android.support.test.runner.AndroidJUnit4;

import com.bottlerocketstudios.continuity.model.TestAnchor;
import com.bottlerocketstudios.continuity.util.SequentialNumberGenerator;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created on 8/24/16.
 */
@RunWith(AndroidJUnit4.class)
public class FactoryTest extends ContinuityTest {

    @Test
    public void testCreationUsingFactory() {
        int taskId = SequentialNumberGenerator.generateNumber();
        TestAnchor testAnchor = new TestAnchor();

        final String testString = "My test string";

        SimpleTestClass simpleTestClass = getContinuityRepository().with(testAnchor, SimpleTestClass.class)
                .using(new ContinuityFactory<SimpleTestClass>() {
                    @Override
                    public SimpleTestClass create() {
                        return new SimpleTestClass(testString);
                    }
                })
                .task(taskId)
                .build();

        Assert.assertEquals("Values were not equal.", simpleTestClass.getValue(), testString);
    }

    public static class SimpleTestClass {
        private final String mValue;

        public SimpleTestClass(String value) {
            mValue = value;
        }

        public String getValue() {
            return mValue;
        }
    }

}
