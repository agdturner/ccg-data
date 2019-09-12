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
 *
 * @author geoagdt
 */
public class Data_IntervalLong1 implements Serializable, Comparable {

    /**
     * Stores the lower bound.
     */
    private final long L;

    /**
     * Stores the upper bound.
     */
    private final long U;

    public Data_IntervalLong1(long l, long u) {
        L = l;
        U = u;
    }

    @Override
    public String toString() {
        return "[" + L + ", " + U + ")";
    }

    /**
     * x is in the interval if it is greater than or equal to L and less than U.
     *
     * @param x The number to test to see if it is in the interval.
     * @return True iff x is in the interval.
     */
    public boolean isInInterval(long x) {
        return x >= L && x < U;
    }

    /**
     * @return the L
     */
    public long getL() {
        return L;
    }

    /**
     * @return the U
     */
    public long getU() {
        return U;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Data_IntervalLong1) {
            Data_IntervalLong1 obj;
            obj = (Data_IntervalLong1) o;
            if (this.L < obj.L) {
                return -1;
            } else {
                if (this.L > obj.L) {
                    return 1;
                }
                if (this.U < obj.U) {
                    return -1;
                } else {
                    if (this.U > obj.U) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        } else {
            return -1;
        }
    }

}
