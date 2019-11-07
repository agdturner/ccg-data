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

import uk.ac.leeds.ccg.andyt.data.id.Data_RecordID;
import uk.ac.leeds.ccg.andyt.data.id.Data_CollectionID;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.data.core.Data_Strings;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;

/**
 * Represents collections of data.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public abstract class Data_Data {

    /**
     * A reference to a Data_Environment instance. This cannot be final.
     */
    public Data_Environment de;

    /**
     * For convenience.
     */
    public Generic_Environment env;
    public Generic_IO io;

    /**
     * The data stored in a number of collections.
     */
    public final HashMap<Data_CollectionID, Data_Collection> data;

    /**
     * For looking up a collection ID from a record ID.
     */
    public final HashMap<Data_RecordID, Data_CollectionID> rID_2_cID;

    /**
     * The directory containing any cached data.
     */
    protected File dir;

    /**
     * @param e What {@link #de} is set to.
     */
    public Data_Data(Data_Environment e) {
        de = e;
        env = e.env;
        io = env.io;
        data = new HashMap<>();
        rID_2_cID = new HashMap<>();
    }

    /**
     * @return The number of records stored in all the collections.
     */
    public long getN() {
        return data.values().stream().mapToLong(c -> c.getN()).sum();
    }

    /**
     * @return An {@link Data_Record} for the given RecordID.
     * @param rID The {@link Data_RecordID} of the {@link Data_Record} to be
     * returned.
     */
    public Data_Record getDataRecord(Data_RecordID rID) {
        Data_CollectionID cID = rID_2_cID.get(rID);
        Data_Collection c = getCollection(cID);
        return c.data.get(rID);
    }

    /**
     * Prints a random set of {@code >n} {@link Data_Record}s obtained via
     * {@link #getDataRecord(Data_RecordID)}.
     *
     * @param n the number of {@link Data_Record}s to print out.
     * @param random the {@link Random} used for selecting {@link Data_Record}s
     * to print.
     */
    protected void print(int n, Random random) {
        long N = getN();
        for (int i0 = 0; i0 < n; i0++) {
            double double0 = random.nextDouble() * N;
            Data_RecordID rID = new Data_RecordID((long) double0);
            Data_Record rec = getDataRecord(rID);
            env.log(rec.toString());
        }
    }

    /**
     * For getting a collection.
     *
     * @param cID Identifier for the collection to be returned.
     * @return The {@link Data_Collection} in {@link #data}. If this is null,
     * then the collection is loaded from the cache via
     * {@link #loadCollection(Data_CollectionID)}.
     */
    protected Data_Collection getCollection(Data_CollectionID cID) {
        Data_Collection r = data.get(cID);
        if (r == null) {
            r = (Data_Collection) loadCollection(cID);
            data.put(cID, r);
        }
        return r;
    }

    /**
     * For getting a collection.
     *
     * @param cID Identifier for the collection to be returned.
     * @param hoome IFF true then an attempt is made to handle any
     * {@link OutOfMemoryError} encountered.
     * @return The {@link Data_Collection} in {@link #data}. If this is null,
     * then the collection is loaded from the cache via
     * {@link #loadCollection(Data_CollectionID)}. (See
     * {@link #getCollection(Data_CollectionID)}.)
     */
    protected Data_Collection getCollection(Data_CollectionID cID, boolean hoome) {
        try {
            Data_Collection c = getCollection(cID);
            return c;
        } catch (OutOfMemoryError err) {
            if (hoome) {
                de.clearMemoryReserve();
                de.clearSomeData();
                de.checkAndMaybeFreeMemory();
                de.initMemoryReserve();
                return getCollection(cID, hoome);
            } else {
                throw err;
            }
        }
    }

    /**
     * For clearing a collection.
     *
     * @param cID Identifier for the collection to be cleared.
     */
    public void clearCollection(Data_CollectionID cID) {
        String m = "clear" + cID.toString();
        env.logStartTag(m);
        env.log("TotalFreeMemory " + de.getTotalFreeMemory());
        data.put(cID, null);
        env.log("TotalFreeMemory " + de.getTotalFreeMemory());
        env.logEndTag(m);
    }

    /**
     * Caches and clears the first subset collection retrieved from an iterator.
     *
     * @return {@code true} iff a subset collection was cached and cleared.
     */
    public boolean clearSomeData() {
        Iterator<Data_CollectionID> ite = data.keySet().iterator();
        while (ite.hasNext()) {
            Data_CollectionID cID = ite.next();
            Data_Collection c = data.get(cID);
            if (c != null) {
                cacheCollection(cID, c);
                data.put(cID, null);
            }
            return true;
        }
        return false;
    }

    /**
     * Caches and cleared all subset collections.
     *
     * @return The number of subset collections cached and cleared.
     */
    public int clearAllData() {
        int r = 0;
        Iterator<Data_CollectionID> ite = data.keySet().iterator();
        while (ite.hasNext()) {
            Data_CollectionID cID = ite.next();
            Data_Collection c = data.get(cID);
            if (c != null) {
                cacheCollection(cID, c);
                data.put(cID, null);
                r++;
            }
        }
        return r;
    }

    /**
     * For caching a subset collection.
     *
     * @param cID the ID of subset collection to be cached.
     * @param o the subset collection to be cached.
     */
    public void cacheCollection(Data_CollectionID cID, Object o) {
        cache(getCollectionFile(cID), o);
    }

    /**
     * For loading a subset collection.
     *
     * @param cID the ID of subset collection to be loaded.
     * @return the subset collection loaded.
     */
    public Object loadCollection(Data_CollectionID cID) {
        return load(getCollectionFile(cID));
    }

    /**
     * For getting a subset collection file.
     *
     * @param cID the ID of subset collection.
     * @return the subset collection file for cID.
     */
    public File getCollectionFile(Data_CollectionID cID) {
        return new File(de.files.getGeneratedDir(),
                Data_Strings.s_DATA + Data_Strings.symbol_underscore + cID
                + de.files.DOT_DAT);
    }

    /**
     * Loads an Object from a File and reports this to the log.
     *
     * @param f the File to load an object from.
     * @return the object loaded.
     */
    protected Object load(File f) {
        String m = "load object from " + f.toString();
        env.logStartTag(m);
        Object r = io.readObject(f);
        env.logEndTag(m);
        return r;
    }

    /**
     * Caches an Object to a File and reports this to the log.
     *
     * @param f the File to cache to.
     * @param o the Object to cache.
     */
    protected void cache(File f, Object o) {
        String m = "cache object to " + f.toString();
        env.logStartTag(m);
        io.writeObject(o, f);
        env.logEndTag(m);
    }
}
