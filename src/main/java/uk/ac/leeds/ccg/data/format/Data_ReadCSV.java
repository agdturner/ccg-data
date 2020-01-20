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
package uk.ac.leeds.ccg.data.format;

import java.io.IOException;
import java.util.ArrayList;
import uk.ac.leeds.ccg.data.core.Data_Environment;

/**
 *
 * @author Andy Turner
 */
public class Data_ReadCSV extends Data_ReadTXT {

    public Data_ReadCSV(Data_Environment e) {
        super(e);
    }

    public ArrayList<String> parseLine() throws IOException {
        String line = super.readLine();
        if (line == null) {
            return null;
        } else {
            return parseLine(line);
        }
    }

    /**
     * Parses a line. Considering that text fields may be additionally delimited
     * by double quotes and they may contain double quotes and line feeds and
     * commas.
     *
     * @param line The line to split.
     * @return The split line with fields in order.
     */
    public ArrayList<String> parseLine(String line) {
        ArrayList<String> r = new ArrayList<>();
        char comma = ',';
        char quote = '"';
        if (line.isBlank()) {
            return r;
        }
        StringBuffer sb = new StringBuffer();
        boolean quoted = false;
        boolean start = false;
        boolean isQuote = false;
        char[] chars = line.toCharArray();
        for (char c : chars) {
            if (quoted) {
                start = true;
                if (c == quote) {
                    quoted = false;
                    isQuote = false;
                } else {
                    if (c == '\"') {
                        if (!isQuote) {
                            sb.append(c);
                            isQuote = true;
                        }
                    } else {
                        sb.append(c);
                    }
                }
            } else {
                if (c == quote) {
                    quoted = true;
                    if (chars[0] != '"' && quote == '\"') {
                        sb.append('"');
                    }
                    if (start) {
                        sb.append('"');
                    }
                } else if (c == comma) {
                    r.add(sb.toString());
                    sb = new StringBuffer();
                    start = false;
                } else if (c == '\r') {
                    // Ignore
                } else if (c == '\n') {
                    // Done
                    break;
                } else {
                    sb.append(c);
                }
            }
        }
        r.add(sb.toString());
        return r;
    }
}
