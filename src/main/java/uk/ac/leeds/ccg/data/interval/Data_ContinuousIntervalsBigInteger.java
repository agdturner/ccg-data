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

import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * For a set of continuous ranges.
 *
 * @author Andy Turner
 */
public class Data_ContinuousIntervalsBigInteger {

    /**
     * The lowest.
     */
    public BigInteger l;

    /**
     * The highest.
     */
    public BigInteger u;

    public TreeSet<Data_IntervalBigInteger1> intervals;

    public Data_ContinuousIntervalsBigInteger(BigInteger l, BigInteger u) {
        intervals = new TreeSet<>();
        this.l = l;
        this.u = u;
        intervals.add(new Data_IntervalBigInteger1(l, u));
    }

    /**
     * Creates and adds a new interval in the
     *
     * @param l
     * @param u
     */
    public void addInterval(BigInteger l, BigInteger u) {
        if (l.compareTo(u) == 0) {
            intervals.add(new Data_IntervalBigInteger1(l, u));
            this.u = u;
        } else if (u.compareTo(l) == 0) {
            intervals.add(new Data_IntervalBigInteger1(l, u));
            this.l = l;
        } else {
            throw new RuntimeException("Unable to join interval.");
        }
    }

    public Data_IntervalBigInteger1 getInterval(BigInteger v) {
        Iterator<Data_IntervalBigInteger1> ite = intervals.iterator();
        while (ite.hasNext()) {
            Data_IntervalBigInteger1 i = ite.next();
            if (v.compareTo(i.u) == -1) {
                return i;
            }
        }
        return intervals.last();
    }

}
