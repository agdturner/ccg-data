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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

/**
 * Abstract class for a data record.
 */
public abstract class Data_AbstractRecord implements Serializable, Comparable {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.RecordID ^ (this.RecordID >>> 32));
        return hash;
    }

    /**
     * @return 8 (assumed to be the number of bits in a byte).
     */
    public static long getNumberOfBitsInByte() {
        return 8L;
    }
    /**
     * An individual sequential identifier.
     */
    protected long RecordID;

    /**
     * @return A copy of <code>this.RecordID</code>
     */
    public long getRecordID() {
        return this.RecordID;
    }

    /**
     * Initialise from rec.
     *
     * @param rec The {@link Data_AbstractRecord} to initialise from.
     */
    protected void init(Data_AbstractRecord rec) {
        this.RecordID = rec.RecordID;
    }

    /**
     * Initialise.
     */
    protected void init() {
        this.RecordID = Long.MIN_VALUE;
    }

    /**
     * @return A <code>String</code> description of this.
     */
    @Override
    public String toString() {
        String result = "RecordID " + this.RecordID;
        return result;
    }

    /**
     * @return A Comma Separated Version (CSV) <code>String</code> of the values
     * of the <code>Fields</code> of <code>this</code>.
     */
    public String toCSVString() {
        String result = "" + this.RecordID;
        return result;
    }

    /**
     * @return A Comma Separated Version (CSV) <code>String</code> of the names
     * of the Variables as returned in <code>toString()</code>.
     */
    public String toCSVStringFields() {
        // // Becasue this.getClass().getFields() is not guaranteed to return an
        // array in a particular order this is not used.
        // String result = new String();
        // Field[] tFields = this.getClass().getFields();
        // // Add name of first Field in tFields
        // result += tFields[ 0 ].getName();
        // // Add names of all but the first Field in tFields preceeded by a
        // comma
        // for ( int i0 = 1; i0 < tFields.length; i0 ++ ) {
        // result += "," + tFields[ i0 ];
        // }
        String result = "RecordID";
        return result;
        // String result = new String();
        // String[] toStringSplit = toString().split( "," );
        // String[] stringVariableNameAndValue;
        // stringVariableNameAndValue = toStringSplit[ 0 ].split( " " );
        // result += stringVariableNameAndValue[ 0 ];
        // for ( int i = 1; i < toStringSplit.length; i ++ ) {
        // stringVariableNameAndValue = toStringSplit[ i ].split( " " );
        // if ( stringVariableNameAndValue.length != 0 ) {
        // result += "," + stringVariableNameAndValue[ 0 ];
        // }
        // }
        // return result;
    }

    /**
     * Writes out:
     * <ul>
     * <li>this.RecordID as a <code>long</code></li>
     * <li>this.hashCode as a <code>int</code></li>
     * </ul>
     * to aRandomAccessFile.
     *
     * @param raf The <code>RandomAccessFile</code> written to.
     * @throws java.io.IOException If encountered when writing to {@code raf}.
     */
    public void write(RandomAccessFile raf) throws IOException {
        raf.writeLong(this.RecordID);
    }

    /**
     * @param object The Object to test if it is equal to this.
     * @return true iff object is equal to this.
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }
        Data_AbstractRecord rec = (Data_AbstractRecord) object;
        return (this.RecordID == rec.RecordID);
    }

    /**
     * Method required by Comparable
     *
     * @param obj The object to be compared with this.
     * @see java.lang.Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(Object obj) {
        if (obj instanceof Data_AbstractRecord) {
            Data_AbstractRecord rec = (Data_AbstractRecord) obj;
            if (rec.RecordID == this.RecordID) {
                return 0;
            }
            if (rec.RecordID > this.RecordID) {
                return 1;
            }
        }
        return -1;
    }

    /**
     * @return The size (in bytes) of this as a <code>long</code>.
     *
     */
    public long getSizeInBytes() {
        return ((long) Long.SIZE) / getNumberOfBitsInByte();
    }
}
