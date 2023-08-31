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
package uk.ac.leeds.ccg.data.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.data.core.Data_Object;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.generic.lang.Generic_String;

/**
 * For reading ASCII text files (such as CSV format files). {@link #read} is for
 * reading an entire file into a collection of Strings (one for each line in the
 * file). {@link #readLine()} reads once {@link #st} has been initialised.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_ReadTXT extends Data_Object {

    private static final long serialVersionUID = 1L;

    protected transient StreamTokenizer st;

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
    public boolean testRead(Path f, Path testDir, int syntax) throws IOException {
        Path test = Paths.get(testDir.toString(), "test" + syntax + ".csv");
        PrintWriter pw = Generic_IO.getPrintWriter(f, false);
        if (Files.exists(f)) {
            try ( BufferedReader br = Generic_IO.getBufferedReader(f)) {
                st = new StreamTokenizer(br);
                setStreamTokenizerSyntax(syntax);
                int token = st.nextToken();
                while (!(token == StreamTokenizer.TT_EOF)) {
                    pw.println(readLine());
                    token = st.nextToken();
                }
            }
            pw.close();
        } else {
            de.env.log("Path " + f + " does not exist!");
        }
        long length0 = Files.size(test);
        System.out.println("length of test file = " + length0);
        long length1 = Files.size(f);
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
     * @param br What {@link #st} is set to tokenize.
     * @param syntax The syntax with which {@link #st} is to tokenize.
     */
    public void setStreamTokenizer(BufferedReader br, int syntax) {
        this.st = new StreamTokenizer(br);
        setStreamTokenizerSyntax(syntax);
    }

    /**
     * A convenience method for setting the StreamTokensizer syntax.
     *
     * @param syntax Currently expecting values 1 to 7 inclusive.
     */
    public void setStreamTokenizerSyntax(int syntax) {
        switch (syntax) {
            case 1 -> Generic_IO.setStreamTokenizerSyntax1(st);
            case 2 -> Generic_IO.setStreamTokenizerSyntax2(st);
            case 3 -> Generic_IO.setStreamTokenizerSyntax3(st);
            case 4 -> Generic_IO.setStreamTokenizerSyntax4(st);
            case 5 -> Generic_IO.setStreamTokenizerSyntax5(st);
            case 6 -> Generic_IO.setStreamTokenizerSyntax6(st);
            case 7 -> Generic_IO.setStreamTokenizerSyntax7(st);
            case 8 -> Generic_IO.setStreamTokenizerSyntax8(st);
            case 9 -> Generic_IO.setStreamTokenizerSyntax9(st);
            default -> de.env.log("No Special Syntax set in "
                        + this.getClass().getName()
                        + ".testRead(Path,Path,int)");
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
    public ArrayList<String> read(Path f, Path testDir, int syntax)
            throws IOException {
//        Charset charset = Charset.forName("US-ASCII");
//        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException x) {
//            System.err.format("IOException: %s%n", x);
//        }

//            throw new IOException("testDir " + testDir + " does not exist in "
//                    + this.getClass().getName() + ".read(Path,Path,int)");
        ArrayList<String> r = new ArrayList<>();
        try (BufferedReader br = Generic_IO.getBufferedReader(f)) {
            st = new StreamTokenizer(br);
            setStreamTokenizerSyntax(syntax);
            String line = readLine();
            while (line != null) {
                r.add(line);
                line = readLine();
            }
        }
        if (testDir != null) {
            if (!Files.exists(testDir)) {
                Files.createDirectories(testDir);
            }
            Path outf = Paths.get(testDir.toString(), "test" + syntax + ".csv");
            PrintWriter pw = Generic_IO.getPrintWriter(outf, false);
            for (String line : r) {
                pw.println(line);
            }
        }
        return r;
    }

    /**
     * @return The next line or null.
     * @throws java.io.IOException If there is an IO issue!
     */
    public String readLine() throws IOException {
        int token = st.nextToken();
        String line = "";
        while (!(token == StreamTokenizer.TT_EOF)) {
            switch (token) {
                case StreamTokenizer.TT_EOL -> {
                    return line;
                }
                case StreamTokenizer.TT_WORD -> line += st.sval;
                case StreamTokenizer.TT_NUMBER -> {
                }
                default -> {
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
                }
            }
            token = st.nextToken();
        }
        return null;
    }

}
