/**
 * Copyright 2010 Andy Turner, The University of Leeds, UK
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package uk.ac.leeds.ccg.andyt.data;

import java.io.Serializable;
import java.util.Objects;
import uk.ac.leeds.ccg.andyt.data.core.Data_Strings;
import uk.ac.leeds.ccg.andyt.data.id.Data_ID;

/**
 * Abstract class for a data record.
 */
public abstract class Data_Record implements Serializable, Comparable<Data_Record> {

    /**
     * The identifier for this record.
     */
    public final Data_RecordID ID;

    public Data_Record(Data_RecordID ID) {
        this.ID = ID;
    }

    public Data_Record(Data_Record rec) {
        this.ID = rec.ID;
    }

    /**
     * @return {@link ID} cast appropriately.
     */
    public abstract Data_ID getID();

    /**
     * @return A String representation of this.
     */
    @Override
    public String toString() {
        //return getID().getClass().getSimpleName() + ID.toString();
        return ID.toString();
    }

    /**
     * @return A Comma Separated Version (CSV) of this.
     */
    public String toCSV() {
        return ID.toString();
    }

    /**
     * @return A Comma Separated Version (CSV) of the names of the
     * fields/variables.
     */
    public String toCSVHeader() {
        return Data_Strings.s_ID;
    }

    /**
     * @param o The Object to test if it is equal to this.
     * @return true iff object is equal to this.
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o != this) {
                if (o instanceof Data_Record) {
                    Data_Record o2 = (Data_Record) o;
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
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.ID);
        return hash;
    }

    @Override
    public int compareTo(Data_Record o) {
        if (o != null) {
            if (o != this) {
                if (o instanceof Data_Record) {
                    Data_Record o2 = (Data_Record) o;
                    return ID.compareTo(o2.ID);
                }
            }
            return 0;
        }
        return -3;
    }
}
