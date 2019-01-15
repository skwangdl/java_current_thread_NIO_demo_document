package com.kepler.bowl;

import junit.framework.*;
import org.junit.Test;

public class TestFrame extends TestCase {
    public TestFrame(String name){
        super((name));
    }

    @Test
    public void testScoreNoThrows() {
        Frame f = new Frame();
        assertEquals(0, f.getScore());
    }

}