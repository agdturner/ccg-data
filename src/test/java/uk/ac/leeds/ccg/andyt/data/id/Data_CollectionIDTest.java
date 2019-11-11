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
package uk.ac.leeds.ccg.andyt.data.id;

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
public class Data_CollectionIDTest {
    
    public Data_CollectionIDTest() {
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
     * Test of toString method, of class Data_ID_int.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        int i = 0;
        Data_CollectionID instance = new Data_CollectionID(i);
        String expResult = "Data_CollectionID(ID=0)";
        String result = instance.toString();
        assertEquals(expResult, result);
        // Test 2
        i = 123456;
        instance = new Data_CollectionID(i);
        expResult = "Data_CollectionID(ID=" + Integer.toString(i) + ")";
        result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
