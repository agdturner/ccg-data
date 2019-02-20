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

import uk.ac.leeds.ccg.andyt.math.Math_BigDecimal;
import uk.ac.leeds.ccg.andyt.math.Math_BigInteger;
import uk.ac.leeds.ccg.andyt.math.Math_byte;
import uk.ac.leeds.ccg.andyt.math.Math_double;
import uk.ac.leeds.ccg.andyt.math.Math_float;
import uk.ac.leeds.ccg.andyt.math.Math_int;
import uk.ac.leeds.ccg.andyt.math.Math_long;
import uk.ac.leeds.ccg.andyt.math.Math_short;

/**
 *
 * @author geoagdt
 */
public class Data_VariableType {

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
    protected static void doByte(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints,
            boolean[] shorts, boolean[] bytes) {
        if (!Math_byte.isByte(s)) {
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
    protected static void doShort(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints,
            boolean[] shorts) {
        if (!Math_short.isShort(s)) {
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
    protected static void doInt(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs, boolean[] ints) {
        if (!Math_int.isInt(s)) {
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
    protected static void doLong(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats,
            boolean[] bigIntegers, boolean[] longs) {
        if (!Math_long.isLong(s)) {
            longs[index] = false;
            bigIntegers[index] = true;
            doBigInteger(s, index, dp, strings, bigDecimals, doubles, floats,
                    bigIntegers);
        }
    }
    
    /**
     * This checks if s can be stored as a BigInteger. If it can't it tries next to
     * determine if it can be stored as a float.
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
    protected static void doBigInteger(String s, int index, int dp, boolean[] strings,
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
    protected static void doFloat(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles, boolean[] floats) {
        if (!Math_float.isFloat(s, dp)) {
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
    protected static void doDouble(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles) {
        if (!Math_double.isDouble(s, dp)) {
            doubles[index] = false;
            bigDecimals[index] = true;
            doBigDecimals(s, index, dp, strings, bigDecimals, doubles);
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
    protected static void doBigDecimals(String s, int index, int dp, boolean[] strings,
            boolean[] bigDecimals, boolean[] doubles) {
        if (!Math_BigDecimal.isBigDecimal(s)) {
            bigDecimals[index] = false;
            strings[index] = true;
        }
    }
}
