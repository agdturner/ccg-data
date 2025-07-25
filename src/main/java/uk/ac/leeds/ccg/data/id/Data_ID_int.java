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
 * A general identifier based on a int. There are at most 2^32 unique ones of
 * these.
 *
 * @author Andy Turner
 * @version 1.1
 */
public class Data_ID_int extends Data_ID implements Comparable<Data_ID_int> {

    private static final long serialVersionUID = 1L;

    public final int id;

    /**
     * Creates a new instance.
     * 
     * @param i What {@link #id} is set to.
     */
    public Data_ID_int(int i) {
        this.id = i;
    }

    /**
     * For creating from a {@link Data_ID_short}.
     * 
     * @param s The {@link Data_ID_short} this is created from.
     */
     public Data_ID_int(Data_ID_short s) {
        this.id = s.id;
    }

     /**
     * For creating from a {@link Data_ID_byte}.
     * 
     * @param b The {@link Data_ID_byte} this is created from.
     */
    public Data_ID_int(Data_ID_byte b) {
        this.id = b.id;
    }
    
    /**
     * @return the id
     */
    public int getId() {
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
                if (o instanceof Data_ID_int o2) {
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
        int hash = 7;
        hash = 47 * hash + this.id;
        return hash;
    }

    @Override
    public int compareTo(Data_ID_int i) {
        if (i != null) {
            if (i != this) {
                if (this.id < i.id) {
                    return -1;
                } else {
                    if (this.id > i.id) {
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
