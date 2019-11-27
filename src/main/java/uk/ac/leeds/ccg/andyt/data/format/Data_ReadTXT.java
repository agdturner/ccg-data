/*
 * Copyright (C) 2016 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.data.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.data.core.Data_Object;

/**
 * For reading ASCII text files (such as CSV format files). {@link #read} is for
 * reading an entire file into a collection of Strings (one for each line in the
 * file). {@link #readLine(java.io.StreamTokenizer)} reads once a
 * {@link java.io.Reader} and {@link java.io.StreamTokenizer} have ben set up.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_ReadTXT extends Data_Object {

    public Data_ReadTXT(Data_Environment e) {
        super(e);
    }

    /**
     * For testing that {@code f} can be read using the syntax given by
     * {@code syntax}. If the file is successfully read, then a duplicate file
     * of the same length will be written in {@code testDir}.
     *
     * @param f The file to be read as a CSV file.
     * @param testDir The directory where a duplicate test file will be written.
     * @param syntax This controls the syntax for the {@link StreamTokenizer}
     * used to parse the lines of the {@code f}.
     * @return true iff the file was successfully read and the file written in
     * {@code testDir} is the same length as {@code f}.
     * @throws IOException if there is an IO problem.
     */
    public boolean testRead(File f, File testDir, int syntax) throws IOException {
        File test = new File(testDir, "test" + syntax + ".csv");
        PrintWriter pw = env.env.io.getPrintWriter(f, false);
        if (f.exists()) {
            try (BufferedReader br = env.env.io.getBufferedReader(f)) {
                StreamTokenizer st = new StreamTokenizer(br);
                setStreamTokenizerSyntax(st, syntax);
                int token = st.nextToken();
                while (!(token == StreamTokenizer.TT_EOF)) {
                    pw.println(readLine(st));
                    token = st.nextToken();
                }
            }
            pw.close();
        } else {
            env.env.log("File " + f + " does not exist!");
        }
        long length0;
        length0 = test.length();
        System.out.println("length of test file = " + length0);
        long length1;
        length1 = f.length();
        System.out.println("length of original file = " + length1);
        if (length0 == length1) {
            System.out.println("length0 == length1");
            return true;
        } else {
            return false;
        }
        //return result;
    }

    /**
     * A convenience method for setting the StreamTokensizer syntax.
     *
     * @param st The StreamTokensizer that's parsing syntax is set.
     * @param syntax Currently expecting values 1 to 7 inclusive.
     */
    protected void setStreamTokenizerSyntax(StreamTokenizer st, int syntax) {
        switch (syntax) {
            case 1:
                env.env.io.setStreamTokenizerSyntax1(st);
                break;
            case 2:
                env.env.io.setStreamTokenizerSyntax2(st);
                break;
            case 3:
                env.env.io.setStreamTokenizerSyntax3(st);
                break;
            case 4:
                env.env.io.setStreamTokenizerSyntax4(st);
                break;
            case 5:
                env.env.io.setStreamTokenizerSyntax5(st);
                break;
            case 6:
                env.env.io.setStreamTokenizerSyntax6(st);
                break;
            case 7:
                env.env.io.setStreamTokenizerSyntax7(st);
                break;
            default:
                env.env.log("No Special Syntax set in "
                        + this.getClass().getName()
                        + ".testRead(File,File,int)");
        }
    }

    /**
     * Attempts to read and parse a file as a TXT file.
     *
     * @param f The file to be read in and returned as an ArrayList with each
     * element being a line.
     * @param testDir If this is null then a test file is not written out.
     * Otherwise a test file is written out in testDir
     * @param syntax For setting the syntax of the StreamTokenizer (see for
     * example, Generic_IO.setStreamTokenizerSyntax1(st)). Currently expecting
     * values 1 to 7 inclusive.
     * @return A list of Strings, one for each line of {@code f}.
     * @throws java.io.IOException If there is an IO issue!
     */
    public ArrayList<String> read(File f, File testDir, int syntax)
            throws IOException {
        if (testDir.exists()) {
            testDir.mkdirs();
            File outf = new File(testDir, "test" + syntax + ".csv");
            PrintWriter pw = new PrintWriter(outf);
            ArrayList<String> r = new ArrayList<>();
            try (BufferedReader br = env.env.io.getBufferedReader(f)) {
                StreamTokenizer st = new StreamTokenizer(br);
                setStreamTokenizerSyntax(st, syntax);
                String line = readLine(st);
                while (line != null) {
                    r.add(line);
                    pw.println(line);
                    line = readLine(st);
                }
            }
            return r;
        } else {
            throw new IOException("testDir " + testDir + " does not exist in "
                    + this.getClass().getName() + ".read(File,File,int)");
        }
    }

    /**
     * @param st The StreamTokenizer for parsing the stream.
     * @return The next line or null.
     * @throws java.io.IOException If there is an IO issue!
     */
    public String readLine(StreamTokenizer st) throws IOException {
        int token = st.nextToken();
        String line = "";
        while (!(token == StreamTokenizer.TT_EOF)) {
            switch (token) {
                case StreamTokenizer.TT_EOL:
                    return line;
                case StreamTokenizer.TT_WORD:
                    line += st.sval;
                    break;
                case StreamTokenizer.TT_NUMBER:
                    break;
                default:
                    if (token == 26 || token == 160) {
                        /**
                         * A type of white-space.
                         */
                        line += (char) token;
                    }
                    if (token == 13) {
                        /**
                         * A type of white-space.
                         */
                        //line += (char) token;
                    }
                    break;
            }
            token = st.nextToken();
        }
        return null;
    }

}
