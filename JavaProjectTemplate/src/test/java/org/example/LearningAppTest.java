package org.example;
import org.junit.Test;
import static org.junit.Assert.*;

import org.example.LearningApp;

public class LearningAppTest {
    @Test public void testSomeLibraryMethod() {
        LearningApp classUnderTest = new LearningApp();
        assertTrue("someMethod should return 'true'", classUnderTest.someMethod());
    }
}
