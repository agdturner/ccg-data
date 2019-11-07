/*
 * Copyright (C) 2018 geoagdt.
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
package uk.ac.leeds.ccg.andyt.data.interval;

import java.io.Serializable;

/**
 * Represents an interval for two longs [l, u) where a long value v is in the
 * interval iff {@code l <= v < u}.
 *
 * @author geoagdt
 */
public class Data_IntervalLong1 implements Serializable,
        Comparable<Data_IntervalLong1> {

    /**
     * Stores the lower bound.
     */
    public final long l;

    /**
     * Stores the upper bound.
     */
    public final long u;

    /**
     * Creates a new instance. (N.B. There is no test to ensure {@code l <= u},
     * but this should be the case.)
     *
     * @param l The lower bound for the interval. What {@link #l} is set to.
     * @param u The lower bound for the interval. What {@link #u} is set to.
     */
    public Data_IntervalLong1(long l, long u) {
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
    public boolean isInInterval(long x) {
        return x >= l && x < u;
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
    public int compareTo(Data_IntervalLong1 i) {
        if (i != null) {
            if (i != this) {
                if (this.l < i.l) {
                    return -1;
                } else {
                    if (this.l > i.l) {
                        return 1;
                    }
                    if (this.u < i.u) {
                        return -1;
                    } else {
                        if (this.u > i.u) {
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
