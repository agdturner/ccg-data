/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.data.format;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;

/**
 *
 * @author Andy Turner
 */
public class Data_ReadCSV extends Data_ReadTXT {

    public Data_ReadCSV(Data_Environment e) {
        super(e);
    }

    public ArrayList<String> parseLine(StreamTokenizer st) throws IOException {
        String line = super.readLine(st);
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
