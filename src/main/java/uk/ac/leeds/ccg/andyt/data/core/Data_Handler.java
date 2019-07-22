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
package uk.ac.leeds.ccg.andyt.data.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author geoagdt
 */
public class Data_Handler extends Data_Object {

    public Data_Handler(Data_Environment e) {
        super(e);
    }
    
    public long getNLines(File f, String charSetName) 
            throws IOException {
        long linesCount = 0;
        int bufferSize = 4096;
        BufferedReader br = env.ge.io.getBufferedReader(f, charSetName);
        char[] buffer = new char[bufferSize];
        int prevChar = -1;
        int readCount = br.read(buffer);
        while (readCount != -1) {
            for (int i = 0; i < readCount; i++) {
                int nextChar = buffer[i];
                switch (nextChar) {
                    case '\r': {
                        // The current line is terminated by a carriage return or by a carriage return immediately followed by a line feed.
                        linesCount++;
                        break;
                    }
                    case '\n': {
                        if (prevChar == '\r') {
                            // The current line is terminated by a carriage return immediately followed by a line feed.
                            // The line has already been counted.
                        } else {
                            // The current line is terminated by a line feed.
                            linesCount++;
                        }
                        break;
                    }
                }
                prevChar = nextChar;
            }
            readCount = br.read(buffer);
        }
        if (prevChar != -1) {
            switch (prevChar) {
                case '\r':
                case '\n': {
                    // The last line is terminated by a line terminator.
                    // The last line has already been counted.
                    break;
                }
                default: {
                    // The last line is terminated by end-of-file.
                    linesCount++;
                }
            }
        }
        br.close();
        return linesCount;
    }
}
