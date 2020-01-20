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

import uk.ac.leeds.ccg.data.id.Data_ID_int;
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
public class Data_ID_intTest {

    public Data_ID_intTest() {
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
     * Test of getID method, of class Data_ID_int.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
        int i = 0;
        Data_ID_int instance = new Data_ID_int(i);
        Number expResult = i;
        Number result = instance.getID();
        assertEquals(expResult, result);
        // Test 2
        i = 123456;
        instance = new Data_ID_int(i);
        expResult = i;
        result = instance.getID();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Data_ID_int.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        int i = 0;
        Data_ID_int instance = new Data_ID_int(i);
        String expResult = "Data_ID_int(ID=0)";
        String result = instance.toString();
        assertEquals(expResult, result);
        // Test 2
        i = 123456;
        instance = new Data_ID_int(i);
        expResult = "Data_ID_int(ID=" + Integer.toString(i) + ")";
        result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Data_ID_int.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        int i = 0;
        Object o = new Data_ID_int(i);
        Data_ID_int instance = new Data_ID_int(i);
        boolean expResult = true;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // Test 2
        i = 123456;
        instance = new Data_ID_int(i);
        expResult = false;
        result = instance.equals(o);
        assertEquals(expResult, result);
        // Test 3
        o = null;
        expResult = false;
        result = instance.equals(o);
        assertEquals(expResult, result);
        // Test 4
        o = new Data_ID_int(i);
        expResult = true;
        result = instance.equals(o);
        assertEquals(expResult, result);

    }

    /**
     * Test of compareTo method, of class Data_ID_int.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        int i = 0;
        Data_ID_int instance = new Data_ID_int(i);
        Data_ID_int id = new Data_ID_int(i);
        int expResult = 0;
        int result = instance.compareTo(id);
        assertEquals(expResult, result);
        // Test 2
        i = 1;
        id = new Data_ID_int(i);        
        expResult = -1;
        result = instance.compareTo(id);
        assertEquals(expResult, result);
        // Test 3
        i = -1;
        id = new Data_ID_int(i);        
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
