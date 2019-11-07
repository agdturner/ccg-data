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
package uk.ac.leeds.ccg.andyt.data.id;

/**
 * An general identifier based on a short. There are at most 2^16 unique ones of
 * these.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_ID_short extends Data_ID implements Comparable<Data_ID_short> {

    public final short ID;

    public Data_ID_short(short s) {
        this.ID = s;
    }

    public Data_ID_short(Data_ID_short s) {
        this.ID = s.ID;
    }

    /**
     * @return the ID
     */
    public short getID() {
        return ID;
    }

    @Override
    public String toString() {
        return getSimpleName() + "(ID=" + ID + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o != this) {
                if (o instanceof Data_ID_short) {
                    Data_ID_short o2 = (Data_ID_short) o;
                    if (ID == o2.ID) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.ID;
        return hash;
    }

    @Override
    public int compareTo(Data_ID_short id) {
        if (id != null) {
            if (id != this) {
                if (ID < id.ID) {
                    return -1;
                } else {
                    if (ID > id.ID) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
            return 0;
        }
        return -2;
    }
}
