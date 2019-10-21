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
 * For reading ASCII text files (such as CSV format files) into a collection of
 * Strings (one for each line in the file).
 *
 * @author geoagdt
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
            BufferedReader br = env.env.io.getBufferedReader(f);
            if (br != null) {
                StreamTokenizer st = new StreamTokenizer(br);
                setStreamTokenizerSyntax(st, syntax);
                int token = st.nextToken();
                while (!(token == StreamTokenizer.TT_EOF)) {
                    readLine(st, pw);
                    token = st.nextToken();
                }
                br.close();
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
     * example, Generic_IO.setStreamTokenizerSyntax1(st))
     * @return A list of Strings, one for each line of {@code f}.
     * @throws java.io.IOException If there is an IO issue!
     */
    public ArrayList<String> read(File f, File testDir, int syntax)
            throws IOException {
        ArrayList<String> result = null;
        PrintWriter pw = null;
        // Initialise pw
        if (testDir != null) {
            testDir.mkdirs();
            File test = new File(testDir, "test" + syntax + ".csv");
            pw = new PrintWriter(test);
        }

        if (f.exists()) {
            BufferedReader br = env.env.io.getBufferedReader(f);
            if (br != null) {
                result = new ArrayList<>();
                StreamTokenizer st = new StreamTokenizer(br);
                setStreamTokenizerSyntax(st, syntax);
                int token = st.nextToken();
//                    long RecordID = 0;
                if (pw == null) {
                    while (!(token == StreamTokenizer.TT_EOF)) {
                        result.add(readLine(st, pw));
                        token = st.nextToken();
                    }
                } else {
                    while (!(token == StreamTokenizer.TT_EOF)) {
                        result.add(readLine(st));
                        token = st.nextToken();
                    }
                }
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
        } else {
            System.out.println("File " + f + " does not exist!");
        }
        return result;
    }

    /**
     * Read and return a line of the text file or null if the file is all read.
     *
     * @param st The StreamTokenizer for parsing the stream.
     * @param pw If this is not null then the line returned is printed to it.
     * @return The next line or null.
     * @throws java.io.IOException If there is an IO issue!
     */
    public String readLine(StreamTokenizer st, PrintWriter pw)
            throws IOException {
        int token = st.nextToken();
        String line = "";
        while (!(token == StreamTokenizer.TT_EOF)) {
            switch (token) {
                case StreamTokenizer.TT_EOL:
                    pw.println(line);
                    return line;
                case StreamTokenizer.TT_WORD:
                    line += st.sval;
                    break;
                case StreamTokenizer.TT_NUMBER:
                    break;
                default:
                    line = doDefault(token, line);
                    break;
            }
            token = st.nextToken();
        }
        return null;
    }

    private String doDefault(int token, String line) {
        if (token == 26 || token == 160) {
            /**
             * A type of space " ". It is unusual as st probably already set to
             * parse space as words.
             */
            line += (char) token;
        }
        if (token == 13) {
            /**
             * These are returns or tabs or something...
             */
            //line += (char) token;
        }
        //System.out.println("line so far " + line);
        //System.out.println("Odd token " + token + " \"" + (char) token + "\" encountered.");
        return line;
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
                    line = doDefault(token, line);
                    break;
            }
            token = st.nextToken();
        }
        return null;
    }
}
