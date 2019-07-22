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
package uk.ac.leeds.ccg.andyt.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.data.core.Data_Handler;
import uk.ac.leeds.ccg.andyt.data.core.Data_Object;
import uk.ac.leeds.ccg.andyt.data.core.Data_Strings;
import uk.ac.leeds.ccg.andyt.math.Math_BigDecimal;
import uk.ac.leeds.ccg.andyt.math.Math_BigInteger;
import uk.ac.leeds.ccg.andyt.math.Math_Byte;
import uk.ac.leeds.ccg.andyt.math.Math_Double;
import uk.ac.leeds.ccg.andyt.math.Math_Float;
import uk.ac.leeds.ccg.andyt.math.Math_Integer;
import uk.ac.leeds.ccg.andyt.math.Math_Long;
import uk.ac.leeds.ccg.andyt.math.Math_Short;

/**
 * This class contains methods for parsing rectangular data organised into
 * columns of variables and rows of records. The
 * {@link #getFieldTypes(java.io.File[], int)} method attempts to determine what
 * type of numbers to store each variable in for an array of files all assumed
 * to contain the same variables. For each variable, first the data are
 * attempted to be stored as bytes, then if this does not work as some value of
 * that variable encountered cannot be stored that way, it attempts to store it
 * as a short, then as an int, then as a long and then as a BigInteger. If the
 * value of the variable is discovered not to be an integer, then an attempt is
 * made to parse it as a float. This succeeds if (and only if) the float used to
 * represent the string is precise enough. If the string cannot be stored as a
 * float, then a double is tried, then failing that a BigDecimal is used. If all
 * values of a variable cannot be stored as a BigDecimal, then the variable type
 * inference defaults to a String.
 *
 * @author geoagdt
 */
public class Data_VariableType extends Data_Object {

    /**
     * The string used to separate fields in the data.
     */
    protected String delimiter;

    /**
     * Creates a new instance.
     *
     * @param env
     */
    public Data_VariableType(Data_Environment env) {
        super(env);
    }

    /**
     * Pass through the data in fs and work out what numeric type is best to
     * store each field in the data. If the data are clean, then currently, this
     * will do a good job, if there is at least one record with an erroneous
     * value for a variable, then this could screw things up. So, if you know
     * what type the variable should be, probably the best way forward is to
     * declare that type and then either filter records with erroneous values,
     * or check the type and clean the data.
     *
     * @param n The maximum number of lines of data used to determine type. Set
     * n to Integer.MaxValue() to read all of data files with fewer lines than
     * Integer.MaxValue() and to use all lines to determine type.
     * @param fs The files containing the data.
     * @param dp The number of decimal places a value has to be correct to if it
     * is a floating point type.
     * @return .
     */
    protected Object[] getFieldTypes(int n, File[] fs, int dp) {
        String m0 = "getFieldTypes(int,File[],int)";
        env.logStartTag(m0);
        Object[] r = new Object[2];
        HashMap<String, Integer>[] allFieldTypes = new HashMap[fs.length];
        String[][] headers = new String[fs.length][];
        for (int j = 0; j < fs.length; j++) {
            allFieldTypes[j] = getFieldTypes(n, fs[j], dp);
        }
        HashMap<String, Integer> consolidatedFieldTypes = new HashMap<>();
        consolidatedFieldTypes.putAll(allFieldTypes[0]);
        for (int j = 0; j < fs.length; j++) {
            HashMap<String, Integer> fieldTypes = allFieldTypes[j];
            Iterator<String> ite = fieldTypes.keySet().iterator();
            while (ite.hasNext()) {
                String field = ite.next();
                int fieldType = fieldTypes.get(field);
                if (consolidatedFieldTypes.containsKey(field)) {
                    int consolidatedFieldType = consolidatedFieldTypes.get(field);
                    if (fieldType != consolidatedFieldType) {
                        consolidatedFieldTypes.put(field,
                                Math.min(fieldType, consolidatedFieldType));
                        env.log("");
                    }
                } else {
                    consolidatedFieldTypes.put(field, fieldType);
                }
            }
        }
        r[0] = consolidatedFieldTypes;
        r[1] = headers;
        env.logEndTag(m0);
        return r;
    }

    /**
     *
     * @return Map coding up types:
     * <ul>
     * <li>0, {@link #env#strings#s_String}</li>
     * <li>1, {@link #env#strings#s_BigDecimal}</li>
     * <li>2, {@link #env#strings#s_Double}</li>
     * <li>3, {@link #env#strings#s_Float</li>
     * <li>4, {@link #env#strings#s_BigInteger</li>
     * <li>5, {@link #env#strings#s_Long</li>
     * <li>6, {@link #env#strings#s_Integer</li>
     * <li>7, {@link #env#strings#s_Short</li>
     * <li>8, {@link #env#strings#s_Byte</li>
     * </ul>
     */
    public HashMap<Integer, String> getTypeNameLookup() {
        HashMap<Integer, String> r = new HashMap<>();
        r.put(0, Data_Strings.s_String);
        r.put(1, Data_Strings.s_BigDecimal);
        r.put(2, Data_Strings.s_Double);
        r.put(3, Data_Strings.s_Float);
        r.put(4, Data_Strings.s_BigInteger);
        r.put(5, Data_Strings.s_Long);
        r.put(6, Data_Strings.s_Integer);
        r.put(7, Data_Strings.s_Short);
        r.put(8, Data_Strings.s_Byte);
        return r;
    }

    /**
     * Pass through the data in fs and work out what numeric type is best to
     * store each field in the data.If the data are clean, then currently, this
     * will do a good job, if there is at least one record with an erroneous
     * value for a variable, then this could screw things up. So, if you know
     * what type the variable should be, probably the best way forward is to
     * declare that type and then either filter records with erroneous values,
     * or check the type and clean the data.
     *
     * @param n The maximum number of lines of data used to determine type. Set
     * n to Integer.MaxValue() to read all of data files with fewer lines than
     * Integer.MaxValue() and to use all lines to determine type.
     * @param f The file containing the data.
     * @param dp The number of decimal places a value has to be correct to if it
     * is a floating point type.
     * @return .
     */
    protected HashMap<String, Integer> getFieldTypes(int n, File f, int dp) {
        String m0 = "getFieldTypes(int,File,int)";
        env.logStartTag(m0);
        Object[] t = loadTest(n, f, dp);
        HashMap<String, Integer> r = new HashMap<>();
        String[] fields = (String[]) t[0];
        /**
         * True indicates that a value of a field can be stored as a string, but
         * not a BigDecimal.
         */
        boolean[] strings2 = (boolean[]) t[1];
        /**
         * True indicates that a value of a field can be stored as a BigDecimal.
         */
        boolean[] bigDecimals = (boolean[]) t[2];
        /**
         * True indicates that a value of a field can be stored as a double.
         */
        boolean[] doubles = (boolean[]) t[3];
        /**
         * True indicates that a value of a field can be stored as a float.
         */
        boolean[] floats = (boolean[]) t[4];
        /**
         * True indicates that a value of a field can be stored as a BigInteger.
         */
        boolean[] bigIntegers = (boolean[]) t[5];
        /**
         * True indicates that a value of a field can be stored as a long.
         */
        boolean[] longs = (boolean[]) t[6];
        /**
         * True indicates that a value of a field can be stored as a int.
         */
        boolean[] ints = (boolean[]) t[7];
        /**
         * True indicates that a value of a field can be stored as a short.
         */
        boolean[] shorts = (boolean[]) t[8];
        /**
         * True indicates that a value of a field can be stored as a byte.
         */
        boolean[] bytes = (boolean[]) t[9];
        for (int i = 0; i < strings2.length; i++) {
            String field = fields[i];
            String m = field + " " + i + " ";
            if (strings2[i]) {
                m += Data_Strings.s_String;
                r.put(field, 0);
            } else {
                if (bigDecimals[i]) {
                    m += Data_Strings.s_BigDecimal;
                    r.put(field, 1);
                } else {
                    if (doubles[i]) {
                        m += Data_Strings.s_double;
                        r.put(field, 2);
                    } else {
                        if (floats[i]) {
                            m += Data_Strings.s_float;
                            r.put(field, 3);
                        } else {

                            if (bigIntegers[i]) {
                                m += Data_Strings.s_BigInteger;
                                r.put(field, 4);
                            } else {
                                if (longs[i]) {
                                    m += Data_Strings.s_long;
                                    r.put(field, 5);
                                } else {
                                    if (ints[i]) {
                                        m += Data_Strings.s_int;
                                        r.put(field, 6);
                                    } else {
                                        if (shorts[i]) {
                                            m += Data_Strings.s_short;
                                            r.put(field, 7);
                                        } else {
                                            if (bytes[i]) {
                                                m += Data_Strings.s_byte;
                                                r.put(field, 8);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            env.log(m);
        }
        env.logEndTag(m0);
        return r;
    }

    /**
     * Tests the loading of f and determines a good numerical type to store each
     * field variable. Essentially this reads through the file and works out
     * what might be most appropriate numerical type for each field variable. If
     * no numerical value is appropriate these will initially default to a
     * String type which is likely to include alphanumeric data including things
     * like dates.
     *
     * @param n The maximum number of lines of data used to determine type. Set
     * n to Integer.MaxValue() to read all of data files with fewer lines than
     * Integer.MaxValue() and to use all lines to determine type.
     * @param f The input file containing rectangular data with a one line
     * header and field variables separated with a delimiter.
     * @param dp The number of decimal places to be used to check if a variable
     * can be stored using a floating point number.
     * @return
     */
    public Object[] loadTest(int n, File f, int dp) {
        String m0 = "loadTest(n, File,int)";
        env.logStartTag(m0);
        env.log("n " + n);
        env.log("File " + f);
        env.log("int " + dp);
        Object[] r = new Object[10];
        BufferedReader br = env.io.getBufferedReader(f);
        String line = br.lines().findFirst().get();
        String[] fields = parseHeader(line);
        int nf = fields.length;
        /**
         * True indicates that a value of a field can be stored as a string, but
         * not a BigDecimal.
         */
        boolean[] strings = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a BigDecimal.
         */
        boolean[] bigDecimals = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a double.
         */
        boolean[] doubles = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a float.
         */
        boolean[] floats = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a BigInteger.
         */
        boolean[] bigIntegers = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a long.
         */
        boolean[] longs = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a int.
         */
        boolean[] ints = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a short.
         */
        boolean[] shorts = new boolean[nf];
        /**
         * True indicates that a value of a field can be stored as a byte.
         */
        boolean[] bytes = new boolean[nf];
        // Initialise arrays;        
        for (int i = 0; i < nf; i++) {
            strings[i] = false;
            bigDecimals[i] = false;
            doubles[i] = false;
            floats[i] = false;
            bigIntegers[i] = false;
            longs[i] = false;
            ints[i] = false;
            shorts[i] = false;
            bytes[i] = true;
        }
        /**
         * Check data is rectangular and if not log a "Field Length Warning".
         */
        boolean fieldLengthWarning = br.lines().parallel().anyMatch(l
                -> l.split(",").length != n);
        if (fieldLengthWarning) {
            env.log("Field Length Warning");
        }
        /**
         * Read through all or at least the first n lines of data and determine
         * type. This needs extra code to be parallelised as the data in the
         * expression will change if not all data can be represented as bytes.
         */
        long nlines = 0;
        try {
            Data_Handler dh = new Data_Handler(env);
            nlines = Math.min(n, dh.getNLines(f, "UTF-8"));
        } catch (IOException ex) {
            Logger.getLogger(Data_VariableType.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int j = 0; j < nlines; j++) {
            try {
                String l = br.readLine();
                String[] split = l.split(delimiter);
                for (int i = 0; i < split.length; i++) {
                    parse(split[i], i, dp, strings, bigDecimals, doubles, floats,
                            bigIntegers, longs, ints, shorts, bytes);
                }
            } catch (IOException ex) {
                Logger.getLogger(Data_VariableType.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /**
         * The following commented out code did the work going through all the
         * data, but sometimes it is better (as the data may contain errors) to
         * just go through the first n lines of data to determine type and then
         * to catch and deal with exceptions when trying to read what is more
         * likely to be erroneous data.
         */
        //        br.lines().skip(1).forEach(l -> {
        //            String[] split = l.split(delimiter);
        //            for (int i = 0; i < split.length; i++) {
        //                parse(split[i], i, dp, strings, bigDecimals, doubles, floats,
        //                        bigIntegers, longs, ints, shorts, bytes);
        //            }
        //        });
        r[0] = fields;
        r[1] = strings;
        r[2] = bigDecimals;
        r[3] = doubles;
        r[4] = floats;
        r[5] = bigIntegers;
        r[6] = longs;
        r[7] = ints;
        r[8] = shorts;
        r[9] = bytes;
        env.logEndTag(m0);
        return r;
    }

    /**
     * Converts variable names to upper case and returns them as
     * {@code String[]}.
     *
     * @param header The line as a string.
     * @return The header changed to upper case and split by {@link #delimiter}.
     */
    public String[] parseHeader(String header) {
        String[] r;
        String h1;
        h1 = header.toUpperCase();
        r = h1.split(delimiter);
        return r;
    }

    /**
     * For parsing the value s of the variable given by index to determine what
     * type of number can store it or whether it must be stored as a String. If
     * another value of this variable already has had to be stored as a String,
     * then so be it.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     * @param floats Indicates any variables that can be stored as floats, but
     * could not be stored as integers generally.
     * @param bigIntegers Indicates any variables that can be stored as
     * BigIntegers, but not as more restricted integer types.
     * @param longs Indicates any variables that can be stored as longs, but not
     * as more restricted integer types.
     * @param ints Indicates any variables that can be stored as ints, but not
     * as more restricted integer types.
     * @param shorts Indicates any variables that can be stored as shorts, but
     * not as more restricted integer types.
     * @param bytes Indicates any variables that can be stored as bytes, but not
     * as more restricted integer types.
     */
    public void parse(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints,
            boolean[] shorts, boolean[] bytes) {
        if (!s.trim().isEmpty()) {
            if (!strings[index]) {
                // Deal with decimals
                if (bigDecimals[index]) {
                    doBigDecimal(s, index, dp, strings, bigDecimals, doubles);
                } else {
                    if (doubles[index]) {
                        doDouble(s, index, dp, strings, bigDecimals, doubles);
                    } else {
                        if (floats[index]) {
                            doFloat(s, index, dp, strings, bigDecimals, doubles,
                                    floats);
                        } else {
                            // Deal with integers.
                            parseIntegers(s, index, dp, strings, bigDecimals,
                                    doubles, floats, bigIntegers, longs, ints,
                                    shorts, bytes);
                        }
                    }
                }
            }
        }
    }

    private void parseIntegers(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints,
            boolean[] shorts, boolean[] bytes) {
        if (bigIntegers[index]) {
            doBigInteger(s, index, dp, strings, bigDecimals, doubles, floats,
                    bigIntegers);
        } else {
            if (longs[index]) {
                doLong(s, index, dp, strings, bigDecimals, doubles, floats,
                        bigIntegers, longs);
            } else {
                if (ints[index]) {
                    doInt(s, index, dp, strings, bigDecimals, doubles, floats,
                            bigIntegers, longs, ints);
                } else {
                    if (shorts[index]) {
                        doShort(s, index, dp, strings, bigDecimals, doubles,
                                floats, bigIntegers, longs, ints, shorts);
                    } else {
                        if (!Math_Byte.isByte(s)) {
                            bytes[index] = false;
                            shorts[index] = true;
                            doShort(s, index, dp, strings, bigDecimals, doubles,
                                    floats, bigIntegers, longs, ints, shorts);
                        }
                    }
                }
            }
        }
    }

    /**
     * This checks if s can be stored as a byte. If it can't it tries next to
     * determine if it can be stored as a short.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     * @param floats Indicates any variables that can be stored as floats, but
     * could not be stored as integers generally.
     * @param bigIntegers Indicates any variables that can be stored as
     * BigIntegers, but not as more restricted integer types.
     * @param longs Indicates any variables that can be stored as longs, but not
     * as more restricted integer types.
     * @param ints Indicates any variables that can be stored as ints, but not
     * as more restricted integer types.
     * @param shorts Indicates any variables that can be stored as shorts, but
     * not as more restricted integer types.
     * @param bytes Indicates any variables that can be stored as bytes, but not
     * as more restricted integer types.
     */
    protected void doByte(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints,
            boolean[] shorts, boolean[] bytes) {
        if (!Math_Byte.isByte(s)) {
            bytes[index] = false;
            shorts[index] = true;
            doShort(s, index, dp, strings, bigDecimals, doubles, floats,
                    bigIntegers, longs, ints, shorts);
        }
    }

    /**
     * This checks if s can be stored as a short. If it can't it tries next to
     * determine if it can be stored as a int.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     * @param floats Indicates any variables that can be stored as floats, but
     * could not be stored as integers generally.
     * @param bigIntegers Indicates any variables that can be stored as
     * BigIntegers, but not as more restricted integer types.
     * @param longs Indicates any variables that can be stored as longs, but not
     * as more restricted integer types.
     * @param ints Indicates any variables that can be stored as ints, but not
     * as more restricted integer types.
     * @param shorts Indicates any variables that can be stored as shorts, but
     * not as more restricted integer types.
     */
    protected void doShort(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints,
            boolean[] shorts) {
        if (!Math_Short.isShort(s)) {
            shorts[index] = false;
            ints[index] = true;
            doInt(s, index, dp, strings, bigDecimals, doubles, floats,
                    bigIntegers, longs, ints);
        }
    }

    /**
     * This checks if s can be stored as a int. If it can't it tries next to
     * determine if it can be stored as a long.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     * @param floats Indicates any variables that can be stored as floats, but
     * could not be stored as integers generally.
     * @param bigIntegers Indicates any variables that can be stored as
     * BigIntegers, but not as more restricted integer types.
     * @param longs Indicates any variables that can be stored as longs, but not
     * as more restricted integer types.
     * @param ints Indicates any variables that can be stored as ints, but not
     * as more restricted integer types.
     */
    protected void doInt(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints) {
        if (!Math_Integer.isInt(s)) {
            ints[index] = false;
            longs[index] = true;
            doLong(s, index, dp, strings, bigDecimals, doubles, floats,
                    bigIntegers, longs);
        }
    }

    /**
     * This checks if s can be stored as a long. If it can't it tries next to
     * determine if it can be stored as a BigInteger.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     * @param floats Indicates any variables that can be stored as floats, but
     * could not be stored as integers generally.
     * @param bigIntegers Indicates any variables that can be stored as
     * BigIntegers, but not as more restricted integer types.
     * @param longs Indicates any variables that can be stored as longs, but not
     * as more restricted integer types.
     */
    protected void doLong(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs) {
        if (!Math_Long.isLong(s)) {
            longs[index] = false;
            bigIntegers[index] = true;
            doBigInteger(s, index, dp, strings, bigDecimals, doubles, floats,
                    bigIntegers);
        }
    }

    /**
     * This checks if s can be stored as a BigInteger. If it can't it tries next
     * to determine if it can be stored as a float.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     * @param floats Indicates any variables that can be stored as floats, but
     * could not be stored as integers generally.
     * @param bigIntegers Indicates any variables that can be stored as
     * BigIntegers, but not as more restricted integer types.
     */
    protected void doBigInteger(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers) {
        if (!Math_BigInteger.isBigInteger(s)) {
            bigIntegers[index] = false;
            floats[index] = true;
            doFloat(s, index, dp, strings, bigDecimals, doubles, floats);
        }
    }

    /**
     * This checks if s can be stored as a float. If it can't it tries next to
     * determine if it can be stored as a double.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     * @param floats Indicates any variables that can be stored as floats, but
     * could not be stored as integers generally.
     */
    protected void doFloat(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats) {
        if (!Math_Float.isFloat(s, dp)) {
            floats[index] = false;
            doubles[index] = true;
            doDouble(s, index, dp, strings, bigDecimals, doubles);
        }
    }

    /**
     * This checks if s can be stored as a float. If it can't it tries next to
     * determine if it can be stored as a double.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     */
    protected void doDouble(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles) {
        if (!Math_Double.isDouble(s, dp)) {
            doubles[index] = false;
            bigDecimals[index] = true;
            doBigDecimal(s, index, dp, strings, bigDecimals, doubles);
        }
    }

    /**
     * This checks if s can be stored as a float. If it can't it tries next to
     * determine if it can be stored as a double.
     *
     * @param s The String to test if it is a byte.
     * @param index The index of the variable for recording it's type.
     * @param dp The number of decimal places to use when testing if it is fine
     * to store a value as a floating point number.
     * @param strings Indicates any variables that have had variable that could
     * only be stored as Strings.
     * @param bigDecimals Indicates any variables that can be stored as
     * BigDecimals, but not as doubles accurately enough given dp.
     * @param doubles Indicates any variables that can be stored as doubles, but
     * not as floats.
     */
    protected void doBigDecimal(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles) {
        if (!Math_BigDecimal.isBigDecimal(s)) {
            bigDecimals[index] = false;
            strings[index] = true;
        }
    }
}
