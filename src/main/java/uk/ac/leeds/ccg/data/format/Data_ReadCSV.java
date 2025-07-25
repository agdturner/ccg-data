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
import uk.ac.leeds.ccg.generic.lang.Generic_String;

/**
 *
 * @author Andy Turner
 */
public class Data_ReadCSV extends Data_ReadTXT {

    private static final long serialVersionUID = 1L;

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
     * by double quotes as they may contain commas.
     *
     * @param line The line to split.
     * @return The split line with fields in order.
     */
    public static ArrayList<String> parseLine(String line) {
        return parseLine(line, ',');
    }

    public static final char quoteD = '"';
    public static final char quoteS = '\'';

    /**
     * Parses a line. Considering that fields may be additionally delimited by
     * double quotes or single quotes which may contain commas or the other type
     * of quote.
     *
     * @param line The line to split.
     * @param delimiter The main delimiter.
     * @return The split line with fields in order.
     */
    public static ArrayList<String> parseLine(String line, char delimiter) {
        ArrayList<String> r = new ArrayList<>();
        if (line.isBlank()) {
            return r;
        }
        StringBuffer sb = new StringBuffer();
        boolean quotes = false;
        boolean quoted = false;
        boolean starts = false;
        boolean startd = false;
        boolean isQuotes = false;
        boolean isQuoted = false;

        char[] chars = line.toCharArray();
        for (char c : chars) {
            if (quoted) {
                startd = true;
                if (c == quoteD) {
                    quoted = false;
                    isQuoted = false;
                    sb.append(quoteD);
                } else {
                    if (c == quoteD) {
                        if (!isQuoted) {
                            sb.append(c);
                            isQuoted = true;
                        }
                    } else {
                        sb.append(c);
                    }
                }
            } else {
                if (quotes) {
                    startd = true;
                    if (c == quoteS) {
                        quotes = false;
                        isQuotes = false;
                        sb.append(quoteS);
                    } else {
                        if (c == quoteS) {
                            if (!isQuotes) {
                                sb.append(c);
                                isQuotes = true;
                            }
                        } else {
                            sb.append(c);
                        }
                    }
                } else {
                    if (c == quoteD) {
                        quoted = true;
                        if (chars[0] != quoteD) {
                            sb.append(quoteD);
                        }
                        if (startd) {
                            sb.append(quoteD);
                        }
                    } else if (c == delimiter) {
                        r.add(sb.toString());
                        sb = new StringBuffer();
                        startd = false;
                    } else if (c == '\r') {
                        // Ignore
                    } else if (c == '\n') {
                        // Done
                        break;
                    } else {
                        if (c == quoteS) {
                            quotes = true;
                            if (chars[0] != quoteS) {
                                sb.append(quoteS);
                            }
                            if (starts) {
                                sb.append(quoteS);
                            }
                        } else if (c == delimiter) {
                            r.add(sb.toString());
                            sb = new StringBuffer();
                            starts = false;
                        } else if (c == '\r') {
                            // Ignore
                        } else if (c == '\n') {
                            // Done
                            break;
                        } else {
                            sb.append(c);
                        }
                        //sb.append(c);
                    }
                }
            }
        }
        r.add(sb.toString());
        return r;
    }

    /**
     * @param s The string to count the fields of.
     * @return a count of fields in s. Text fields may be delimited by double
     * quotes and may contain end f line markers. If this is the case, then the
     * string provided may only be a partial row of data.
     */
    public int countFields(String s) {
        if (s.isBlank()) {
            return 0;
        }
        // Count number of double quotes.
        int nDQ = Generic_String.getCount(s, "\"");
        if (nDQ == 0) {
            return Generic_String.getCount(s, ",");
        }
        int count = 0;
        char comma = ',';
        char quote = '"';
        boolean quoted = false;
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (quoted) {
                if (c == quote) {
                    quoted = false;
                }
            } else {
                if (c == quote) {
                    quoted = true;
                } else {
                    if (c == comma) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Parses a line or part of line. A fields may be additionally delimited by
     * double quotes and they may contain double quotes, commas and end of line
     * markers.
     *
     * @param line The line to split.
     * @param enf The expected number of fields.
     * @return The split line with fields in order.
     */
    public ArrayList<String> parseLine(String line, int enf) {
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

    /**
     * @param nf The number of fields to read.
     * @return Read a row of data.
     * @throws java.io.IOException If there is an IO issue!
     */
    public String readRow(int nf) throws IOException {
        /**
         * There can be fields with line breaks in them. If this is then what is
         * read as a line is not a full row of data. So, keep reading until the
         * end of a row is reached.
         */
        String line = readLine();
        if (line == null) {
            return null;
        }
        int count = countFields(line);
        while (count < nf) {
            line += readLine();
            count = countFields(line);
        }
        // Just in case the last field has line breaks in:
        int dqc = Generic_String.getCount(line, "\"");
        if (dqc % 2 != 0) {
            line += readLine();
        }
        count = countFields(line);
        if (count > nf) {
            throw new RuntimeException("Too many fields exception reading a row of data.");
        }
        return line;
    }
}
