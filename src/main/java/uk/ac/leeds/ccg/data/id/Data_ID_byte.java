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
package uk.ac.leeds.ccg.data.id;

/**
 * An general identifier based on a byte. There are at most 2^8 unique ones of
 * these.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_ID_byte extends Data_ID implements Comparable<Data_ID_byte> {

    private static final long serialVersionUID = 1L;

    public final byte id;

    public Data_ID_byte(byte b) {
        this.id = b;
    }

    /**
     * @return the {@link #id}
     */
    public byte getId() {
        return id;
    }

    @Override
    public String toString() {
        return getSimpleName() + "(id=" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o != this) {
                if (o instanceof Data_ID_byte) {
                    Data_ID_byte o2 = (Data_ID_byte) o;
                    if (id == o2.id) {
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
        hash = 67 * hash + this.id;
        return hash;
    }

    @Override
    public int compareTo(Data_ID_byte i) {
        if (i != null) {
            if (i != this) {
                if (id < i.id) {
                    return -1;
                } else {
                    if (id > i.id) {
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