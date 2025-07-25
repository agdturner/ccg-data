/*
 * Copyright 2025 Centre for Computational Geography, University of Leeds.
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
package uk.ac.leeds.ccg.data.format;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Andy Turner
 */
public class Data_ReadCSVTest {
    
    public Data_ReadCSVTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of parseLine method, of class Data_ReadCSV.
     */
    @org.junit.jupiter.api.Test
    public void testParseLine_String_char() {
        System.out.println("parseLine");
        String line = "'Structure model' 'Database references'";
        char delimiter = ' ';
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("'Structure model'");
        expResult.add("'Database references'");
        ArrayList<String> result = Data_ReadCSV.parseLine(line, delimiter);
        assertTrue(expResult.size() == result.size());
        for (int i = 0; i < result.size(); i ++) {
            System.out.println(expResult.get(i) + " expResult.get(i)");
            System.out.println(result.get(i) + " result.get(i)");
            assertTrue(expResult.get(i).equalsIgnoreCase(result.get(i)));
        }
        // Test 2
        line = "\"Structure model\" 'Database references'";
        expResult = new ArrayList<>();
        expResult.add("\"Structure model\"");
        expResult.add("'Database references'");
        result = Data_ReadCSV.parseLine(line, delimiter);
        assertTrue(expResult.size() == result.size());
        for (int i = 0; i < result.size(); i ++) {
            System.out.println(expResult.get(i) + " expResult.get(i)");
            System.out.println(result.get(i) + " result.get(i)");
            assertTrue(expResult.get(i).equalsIgnoreCase(result.get(i)));
        }
        // Test 3
        line = "\"A O'DW\" 'Database references'";
        expResult = new ArrayList<>();
        expResult.add("\"A O'DW\"");
        expResult.add("'Database references'");
        result = Data_ReadCSV.parseLine(line, delimiter);
        assertTrue(expResult.size() == result.size());
        for (int i = 0; i < result.size(); i ++) {
            System.out.println(expResult.get(i) + " expResult.get(i)");
            System.out.println(result.get(i) + " result.get(i)");
            assertTrue(expResult.get(i).equalsIgnoreCase(result.get(i)));
        }
    }

    
}
