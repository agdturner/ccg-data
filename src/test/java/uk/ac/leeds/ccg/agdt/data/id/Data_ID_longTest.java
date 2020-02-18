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
package uk.ac.leeds.ccg.agdt.data.id;

import uk.ac.leeds.ccg.data.id.Data_ID_long;
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
public class Data_ID_longTest {

    public Data_ID_longTest() {
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
     * Test of getID method, of class Data_ID_long.
     */
    @Test
    public void testGetID() {
        System.out.println("getId");
        long l = 0L;
        Data_ID_long instance = new Data_ID_long(l);
        Number expResult = l;
        Number result = instance.getId();
        assertEquals(expResult, result);
        // Test 2
        l = 123456L;
        instance = new Data_ID_long(l);
        expResult = l;
        result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Data_ID_long.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        long l = 0L;
        Data_ID_long instance = new Data_ID_long(l);
        String expResult = "Data_ID_long(id=0)";
        String result = instance.toString();
        assertEquals(expResult, result);
        // Test 2
        l = 123456;
        instance = new Data_ID_long(l);
        expResult = "Data_ID_long(id=" + Long.toString(l) + ")";
        result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Data_ID_long.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        long l = 0L;
        Object o = new Data_ID_long(l);
        Data_ID_long instance = new Data_ID_long(l);
        boolean expResult = true;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // Test 2
        l = 123456L;
        instance = new Data_ID_long(l);
        expResult = false;
        result = instance.equals(o);
        assertEquals(expResult, result);
        // Test 3
        o = null;
        expResult = false;
        result = instance.equals(o);
        assertEquals(expResult, result);
        // Test 4
        o = new Data_ID_long(l);
        expResult = true;
        result = instance.equals(o);
        assertEquals(expResult, result);

    }

    /**
     * Test of compareTo method, of class Data_ID_long.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        long l = 0L;
        Data_ID_long instance = new Data_ID_long(l);
        Data_ID_long id = new Data_ID_long(l);
        long expResult = 0;
        long result = instance.compareTo(id);
        assertEquals(expResult, result);
        // Test 2
        l = 1L;
        id = new Data_ID_long(l);        
        expResult = -1;
        result = instance.compareTo(id);
        assertEquals(expResult, result);
        // Test 3
        l = -1L;
        id = new Data_ID_long(l);        
        expResult = 1;
        result = instance.compareTo(id);
        assertEquals(expResult, result);
        // Test 4
        id = null;        
        expResult = -2;
        result = instance.compareTo(id);
        assertEquals(expResult, result);
        
    }

}
