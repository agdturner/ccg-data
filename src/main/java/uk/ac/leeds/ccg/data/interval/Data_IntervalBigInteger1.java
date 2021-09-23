/*
 * Copyright 2021 Centre for Computational Geography, University of Leeds.
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
package uk.ac.leeds.ccg.data.interval;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Represents an interval for two BigIntegers [l, u) where a value v is in the
 * interval iff {@code l <= v < u}.
 *
 * @author Andy Turner
 */
public class Data_IntervalBigInteger1 implements Serializable,
        Comparable<Data_IntervalBigInteger1> {

    private static final long serialVersionUID = 1L;

    /**
     * Stores the lower bound.
     */
    public final BigInteger l;

    /**
     * Stores the upper bound.
     */
    public final BigInteger u;

    /**
     * Creates a new instance. (N.B. There is no test to ensure {@code l <= u},
     * but this should be the case.)
     *
     * @param l The lower bound for the interval. What {@link #l} is set to.
     * @param u The lower bound for the interval. What {@link #u} is set to.
     */
    public Data_IntervalBigInteger1(BigInteger l, BigInteger u) {
        this.l = l;
        this.u = u;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(l=" + l + ", u=" + u + ")";
    }

    /**
     * For testing if {@code x} is in this interval.
     *
     * @param x The number to test to see if it is in the interval.
     * @return {@code true} iff x is in the interval.
     */
    public boolean isInInterval(BigInteger x) {
        return x.compareTo(l) != -1 && x.compareTo(u) == -1;
    }

    /**
     * For comparing two instances.
     *
     * @param i The interval to compare.
     * @return {@code -1} if the {@link #l} of this is less than that of
     * {@code i}: {@code 1} if the {@link #l} of this is greater than that of
     * {@code i}: {@code -1} if the {@link #l} of this is equal to that of
     * {@code i} and the {@link #u} of this is less than that of {@code i}:
     * {@code 1} if the {@link #l} of this is equal to that of {@code i} and
     * the {@link #u} of this is greater than that of {@code i}: {@code -2} if
     * {@code i} is {@code null} and {@code 0} otherwise. the upper bound of i
     */
    @Override
    public int compareTo(Data_IntervalBigInteger1 i) {
        if (i != null) {
            if (i != this) {
                if (l.compareTo(i.l) == -1) {
                    return -1;
                } else {
                    if (l.compareTo(i.l) == 1) {
                        return 1;
                    }
                    if (u.compareTo(i.u) == -1) {
                        return -1;
                    } else {
                        if (u.compareTo(i.u) == 1) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            return 0;
        }
        return -2;
    }
}
