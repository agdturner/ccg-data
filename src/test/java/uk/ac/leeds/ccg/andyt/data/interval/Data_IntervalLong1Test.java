/*
 * Copyright 2019 Centre for Computational Geography, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.data.interval;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author geoagdt
 */
public class Data_IntervalLong1Test {
    
    public Data_IntervalLong1Test() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of toString method, of class Data_IntervalLong1.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        long l = 0;
        long u = 1;
        Data_IntervalLong1 instance = new Data_IntervalLong1(l, u);
        String expResult = "Data_IntervalLong1(l=" + Long.toString(l) + ", u=" 
                + Long.toString(u) + ")";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of isInInterval method, of class Data_IntervalLong1.
     */
    @Test
    public void testIsInInterval() {
        System.out.println("isInInterval");
        long x = 0L;
        long l = 0;
        long u = 1;
        Data_IntervalLong1 instance = new Data_IntervalLong1(l, u);
        boolean expResult = true;
        boolean result = instance.isInInterval(x);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Data_IntervalLong1.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        long l = 0;
        long u = 2;
        Data_IntervalLong1 i = new Data_IntervalLong1(l, u);
        Data_IntervalLong1 instance = new Data_IntervalLong1(l, u);
        int expResult = 0;
        int result = instance.compareTo(i);
        assertEquals(expResult, result);
        // Test 2
        l = 0;
        u = 1;
        instance = new Data_IntervalLong1(l, u);
        expResult = -1;
        result = instance.compareTo(i);
        assertEquals(expResult, result);
        // Test 3
        l = 1;
        u = 2;
        instance = new Data_IntervalLong1(l, u);
        expResult = 1;
        result = instance.compareTo(i);
        assertEquals(expResult, result);
        // Test 4
        l = 0;
        u = 3;
        instance = new Data_IntervalLong1(l, u);
        expResult = 1;
        result = instance.compareTo(i);
        assertEquals(expResult, result);
    }
    
}
