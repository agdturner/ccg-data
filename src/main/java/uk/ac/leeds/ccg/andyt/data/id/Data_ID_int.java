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
 * A general identifier based on a int.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_ID_int extends Data_ID implements Comparable<Data_ID_int> {

    public final int ID;

    public Data_ID_int(int i) {
        this.ID = i;
    }

    /**
     * @return the ID
     */
    public Number getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "ID=" + ID;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o != this) {
                if (o instanceof Data_ID_int) {
                    Data_ID_int o2 = (Data_ID_int) o;
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
        hash = 37 * hash + this.ID;
        return hash;
    }

    @Override
    public int compareTo(Data_ID_int d) {
        if (d != null) {
            if (d != this) {
                if (d instanceof Data_ID_int) {
                    Data_ID_int d2 = (Data_ID_int) d;
                    if (ID < d2.ID) {
                        return -1;
                    } else {
                        if (ID > d2.ID) {
                            return 1;
                        } else {

                        }
                    }
                }
                return -2;
            }
            return 0;
        }
        return -3;
    }

}
