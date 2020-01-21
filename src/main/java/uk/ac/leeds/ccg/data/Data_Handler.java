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
package uk.ac.leeds.ccg.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.data.core.Data_Object;
import uk.ac.leeds.ccg.generic.io.Generic_IO;

/**
 * A basic data handler class. This class contains a method
 * {@link #getNLines(java.nio.file.Path, java.lang.String)} which is for getting
 * the number of lines in a file given a charSetName.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_Handler extends Data_Object {

    public Data_Handler(Data_Environment e) {
        super(e);
    }

    public long getNLines(Path f, String charSetName)
            throws IOException {
        long lineCount = 0;
        int bufferSize = 4096;
        try (BufferedReader br = Generic_IO.getBufferedReader(f, charSetName)) {
            char[] buffer = new char[bufferSize];
            int prevChar = -1;
            int readCount = br.read(buffer);
            while (readCount != -1) {
                for (int i = 0; i < readCount; i++) {
                    int nextChar = buffer[i];
                    switch (nextChar) {
                        case '\r': {
                            /**
                             * The current line is terminated by a carriage
                             * return or by a carriage return immediately
                             * followed by a line feed.
                             */
                            lineCount++;
                            break;
                        }
                        case '\n': {
                            if (prevChar == '\r') {
                                /**
                                 * The current line is terminated by a carriage
                                 * return immediately followed by a line feed.
                                 * The line has already been counted.
                                 */
                            } else {
                                /**
                                 * The current line is terminated by a line
                                 * feed.
                                 */
                                lineCount++;
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
                        /**
                         * The last line is terminated by a line terminator. The
                         * last line has already been counted.
                         */
                        break;
                    }
                    default: {
                        /**
                         * The last line is terminated by end-of-file.
                         */
                        lineCount++;
                    }
                }
            }
        }
        return lineCount;
    }
}
