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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
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
 * {@link #getFieldTypes(int, java.io.File[], int)} method attempts to determine
 * what type of numbers to store each variable in for an array of files all
 * assumed to contain the same variables. For each variable, first the data are
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
     * For storing a type2TypeName Lookup.
     * <ul>
     * <li>0, "String"</li>
     * <li>1, "BigDecimal"</li>
     * <li>2, "Double"</li>
     * <li>3, "Float"</li>
     * <li>4, "BigInteger"</li>
     * <li>5, "Long"</li>
     * <li>6, "Integer"</li>
     * <li>7, "Short"</li>
     * <li>8, "Byte"</li>
     * </ul>
     */
    protected HashMap<Integer, String> type2TypeName;

    /**
     * Creates a new instance.
     *
     * @param env The {@link Data_Environment} for creating an instance.
     */
    public Data_VariableType(Data_Environment env) {
        super(env);
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
     * @param fs The files containing the data.
     * @param dp The number of decimal places a value has to be correct to if it
     * is a floating point type.
     * @return Data_VariableNamesAndTypes
     * @throws java.io.FileNotFoundException If a data file is not found.
     */
    protected Data_VariableNamesAndTypes getFieldTypes(int n, File[] fs, int dp)
            throws FileNotFoundException, IOException {
        String m0 = "getFieldTypes(int,File[],int)";
        env.env.logStartTag(m0);
        Data_VariableNamesAndTypes r = getFieldTypes(n, fs[0], dp);
        for (int j = 1; j < fs.length; j++) {
            Data_VariableNamesAndTypes vnt = getFieldTypes(n, fs[j], dp);
            integrateVariableNamesAndTypes(r, vnt);
        }
        env.env.logEndTag(m0);
        return r;
    }

    /**
     * Compares r and vnt and updates r if there are Variable names and types
     * that are different in vnt.If there are new names and types, these are
     * added to the end of r.If there are some with the same names, but
     * different types, then the lowest type is assigned to the name.
     *
     * @param r The Data_VariableNamesAndTypes to be modified.
     * @param vnt The Data_VariableNamesAndTypes to be compared with r. Any new
     * variable names and types are added to r. Any that have the same name, but
     * a different type, the lowest type is set in r.
     */
    public void integrateVariableNamesAndTypes(Data_VariableNamesAndTypes r,
            Data_VariableNamesAndTypes vnt) {
        String m = "integrateVariableNamesAndTypes(Data_VariableNamesAndTypes,"
                + "Data_VariableNamesAndTypes)";
        env.env.logStartTag(m);
        int vfl = vnt.fieldNames2Order.size();
        int rfl = r.fieldNames2Order.size();
        if (vfl == rfl) {
            env.env.log("");
        }
        env.env.logEndTag(m);
    }

    /**
     *
     * @return Map coding up types:
     * <ul>
     * <li>0, "String"</li>
     * <li>1, "BigDecimal"</li>
     * <li>2, "Double"</li>
     * <li>3, "Float"</li>
     * <li>4, "BigInteger"</li>
     * <li>5, "Long"</li>
     * <li>6, "Integer"</li>
     * <li>7, "Short"</li>
     * <li>8, "Byte"</li>
     * </ul>
     */
    public HashMap<Integer, String> getType2TypeName() {
        if (type2TypeName == null) {
            type2TypeName = new HashMap<>();
            type2TypeName.put(0, Data_Strings.s_String);
            type2TypeName.put(1, Data_Strings.s_BigDecimal);
            type2TypeName.put(2, Data_Strings.s_Double);
            type2TypeName.put(3, Data_Strings.s_Float);
            type2TypeName.put(4, Data_Strings.s_BigInteger);
            type2TypeName.put(5, Data_Strings.s_Long);
            type2TypeName.put(6, Data_Strings.s_Integer);
            type2TypeName.put(7, Data_Strings.s_Short);
            type2TypeName.put(8, Data_Strings.s_Byte);
        }
        return type2TypeName;
    }

    /**
     * Pass through the data in {@code f} and work out what numeric type is best
     * to store each field. If the data are clean, then currently, this will do
     * a good job, if there is at least one record with an erroneous value for a
     * variable, then this could screw things up. So, if you know what type the
     * variable should be, probably the best way forward is to declare that type
     * and then either filter records with erroneous values, or check the type
     * and clean the data.
     *
     * @param n The maximum number of lines of data used to determine type. Set
     * n to Integer.MaxValue() to read all of data files with fewer lines than
     * Integer.MaxValue() and to use all lines to determine type.
     * @param f The file containing the data.
     * @param dp The number of decimal places a value has to be correct to if it
     * is a floating point type.
     * @return A map with keys as field names and values as numbers representing
     * types.
     * @throws java.io.FileNotFoundException If a data file is not found.
     */
    protected Data_VariableNamesAndTypes getFieldTypes(int n, File f, int dp)
            throws FileNotFoundException, IOException {
        String m0 = "getVariableNamesAndTypes(int,File,int)";
        env.env.logStartTag(m0);
        Data_VariableNamesAndTypes r = getVariableNamesAndTypes(n, f, dp);
        env.env.logEndTag(m0);
        return r;
    }

    /**
     * This loads the first n lines of f and determines a type to store each
     * field variable. Integer value types (short, integer, long, BigInteger)
     * types are preferred before decimals (float, double BigDecimal). If no
     * numerical value is appropriate the type is set to String type (this may
     * include things like dates).
     *
     * @param n The maximum number of lines of data used to determine type. Set
     * n to Integer.MaxValue() to read all of data files with fewer lines than
     * Integer.MaxValue() and to use all lines to determine type.
     * @param f The input file containing rectangular data with a one line
     * header and field variables separated with a delimiter.
     * @param dp The number of decimal places to be used to check if a variable
     * can be stored using a floating point number.
     * @return Data_VariableNamesAndTypes r:
     * @throws java.io.FileNotFoundException If a data file is not found.
     */
    public Data_VariableNamesAndTypes getVariableNamesAndTypes(int n, File f,
            int dp) throws FileNotFoundException, IOException {
        String m0 = "getVariableNamesAndTypes(n, File,int)";
        env.env.logStartTag(m0);
        env.env.log("n " + n);
        env.env.log("File " + f);
        env.env.log("int " + dp);
        BufferedReader br = env.env.io.getBufferedReader(f);
        String line = br.readLine();
        env.env.log(line); // Log header.
        String[] fields = parseHeader(line);
        int nf = fields.length;
        Data_VariableNamesAndTypes r = new Data_VariableNamesAndTypes(nf, fields);
        /**
         * Check data is rectangular and if not log a "Field Length Warning".
         */
        boolean fieldLengthWarning = br.lines().parallel().anyMatch(l
                -> l.split(",").length != nf);
        if (fieldLengthWarning) {
            env.env.log("Field Length Warning");
        }
        /**
         * Read through all or at least the first n lines of data and determine
         * type. This needs extra code to be parallelised as the data in the
         * expression will change if not all data can be represented as bytes.
         */
        long nlines = 0;
        Data_Handler dh = new Data_Handler(env);
        nlines = Math.min(n, dh.getNLines(f, "UTF-8"));
        for (int j = 0; j < nlines; j++) {
            line = br.readLine();
            String[] split = line.split(delimiter);
            for (int i = 0; i < split.length; i++) {
                parse(split[i], i, dp, r.strings, r.bigDecimals, r.doubles, r.floats,
                        r.bigIntegers, r.longs, r.ints, r.shorts, r.bytes);
            }
        }
        /**
         * Also get the last line and parse this.
         */
        line = br.readLine();
        if (line != null) {
            String line2 = br.readLine();;
            while (line2 != null) {
                line = line2;
                line2 = br.readLine();
            }
            String[] split = line.split(delimiter);
            for (int i = 0; i < split.length; i++) {
                parse(split[i], i, dp, r.strings, r.bigDecimals, r.doubles, r.floats,
                        r.bigIntegers, r.longs, r.ints, r.shorts, r.bytes);
            }
        }
        /**
         * The following commented out code did the work going through all the
         * data, but sometimes it is better (as the data may contain errors) to
         * just go through the first n lines of data and the last line to
         * determine type and then to catch and deal with exceptions when trying
         * to read the rest what may contain erroneous data.
         */
        //        br.lines().skip(1).forEach(l -> {
        //            String[] split = l.split(delimiter);
        //            for (int i = 0; i < split.length; i++) {
        //                parse(split[i], i, dp, strings, bigDecimals, doubles, floats,
        //                        bigIntegers, longs, ints, shorts, bytes);
        //            }
        //        });
        for (int j = 0; j < nf; j++) {
            Iterator<Integer> ite = r.order2FieldNames.keySet().iterator();
            while (ite.hasNext()) {
                int i = ite.next();
                if (r.strings[i]) {
                    r.order2Type.put(i, 0);
                } else if (r.bigDecimals[i]) {
                    r.order2Type.put(i, 1);
                } else if (r.doubles[i]) {
                    r.order2Type.put(i, 2);
                } else if (r.floats[i]) {
                    r.order2Type.put(i, 3);
                } else if (r.bigIntegers[i]) {
                    r.order2Type.put(i, 4);
                } else if (r.longs[i]) {
                    r.order2Type.put(i, 5);
                } else if (r.ints[i]) {
                    r.order2Type.put(i, 6);
                } else if (r.shorts[i]) {
                    r.order2Type.put(i, 7);
                } else if (r.bytes[i]) {
                    r.order2Type.put(i, 8);
                } else {
                    env.env.log("Undetermined type!!!");
                }
            }
        }
        env.env.logEndTag(m0);
        return r;
    }

    public class Data_VariableNamesAndTypes {

        /**
         * Keys are field names, values are the index position where field names
         * appear in fields.
         */
        public HashMap<String, Integer> fieldNames2Order;
        /**
         * Keys are the index position where field names appear in fields,
         * values are field names.
         */
        public TreeMap<Integer, String> order2FieldNames;
        /**
         * Keys are the order of the fieldname, values are the type coded as an
         * integer where:
         * <ul>
         * <li>0 is a "String"</li>
         * <li>1 is a "BigDecimal"</li>
         * <li>2 is a "Double"</li>
         * <li>3 is a "Float"</li>
         * <li>4 is a "BigInteger"</li>
         * <li>5 is a "Long"</li>
         * <li>6 is a "Integer"</li>
         * <li>7 is a "Short"</li>
         * <li>8 is a "Byte"</li>
         * </ul>
         */
        public HashMap<Integer, Integer> order2Type;
        /**
         * True indicates that a value of a field can be stored as a string, but
         * not a BigDecimal.
         */
        public boolean[] strings;
        /**
         * True indicates that a value of a field can be stored as a BigDecimal.
         */
        public boolean[] bigDecimals;
        /**
         * True indicates that a value of a field can be stored as a double.
         */
        public boolean[] doubles;
        /**
         * True indicates that a value of a field can be stored as a float.
         */
        public boolean[] floats;
        /**
         * True indicates that a value of a field can be stored as a BigInteger.
         */
        public boolean[] bigIntegers;
        /**
         * True indicates that a value of a field can be stored as a long.
         */
        public boolean[] longs;
        /**
         * True indicates that a value of a field can be stored as a int.
         */
        public boolean[] ints;
        /**
         * True indicates that a value of a field can be stored as a short.
         */
        public boolean[] shorts;
        /**
         * True indicates that a value of a field can be stored as a byte.
         */
        public boolean[] bytes;

        public Data_VariableNamesAndTypes(int nf, String[] fields) {
            type2TypeName = getType2TypeName();
            fieldNames2Order = new HashMap<>();
            order2FieldNames = new TreeMap<>();
            order2Type = new HashMap<>();
            for (int i = 0; i < nf; i++) {
                String field = parseFieldName(fields[i]);
                fieldNames2Order.put(field, i);
                order2FieldNames.put(i, field);
            }
            // Initialise arrays;        
            strings = new boolean[nf];
            bigDecimals = new boolean[nf];
            doubles = new boolean[nf];
            floats = new boolean[nf];
            bigIntegers = new boolean[nf];
            longs = new boolean[nf];
            ints = new boolean[nf];
            shorts = new boolean[nf];
            bytes = new boolean[nf];
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
        }
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
     * Replaces all characters that are not alphanumeric or underscores with
     * underscores. Replaces all instances of consecutive underscores with a
     * single underscore. Removes any underscore at the start or end of the
     * result.
     *
     * @param name The name to parse.
     * @return A copy of name where has been modified as above.
     */
    public String parseFieldName(String name) {
        int length = name.length();
        String r = name.replaceAll("[^A-Z^0-9_]", "_");
        r = r.replaceAll("[__]", "_");
        if (r.startsWith("_")) {
            r = r.substring(1, r.length());
        }
        if (r.endsWith("_")) {
            r = r.substring(0, r.length() - 1);
        }
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
