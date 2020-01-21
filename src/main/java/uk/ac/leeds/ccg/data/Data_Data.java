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
package uk.ac.leeds.ccg.data;

import uk.ac.leeds.ccg.data.id.Data_RecordID;
import uk.ac.leeds.ccg.data.id.Data_CollectionID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.data.core.Data_Object;
import uk.ac.leeds.ccg.data.core.Data_Strings;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_IO;

/**
 * Represents collections of data.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public abstract class Data_Data extends Data_Object {

    /**
     * For convenience. Not declared to be final as when a cached version of
     * this is loaded back in it might need initialising.
     */
    public transient Generic_Environment env;
    public transient Generic_IO io;

    /**
     * The data stored in a number of collections. This is protected as
     * typically it needs casting for use and should be found via a getData()
     * method.
     */
    protected final HashMap<Data_CollectionID, Data_Collection> data;

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
        super(e);
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
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public Data_Record getDataRecord(Data_RecordID rID) throws IOException,
            ClassNotFoundException {
        Data_CollectionID cID = rID_2_cID.get(rID);
        Data_Collection c = getCollection(cID);
        Data_Record r = (Data_Record) c.data.get(rID);
//        Data_Record r = c.data.get(rID);
        return r;
    }

    /**
     * Prints a random set of {@code >n} {@link Data_Record}s obtained via
     * {@link #getDataRecord(Data_RecordID)}.
     *
     * @param n the number of {@link Data_Record}s to print out.
     * @param random the {@link Random} used for selecting {@link Data_Record}s
     * to print.
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    protected void print(int n, Random random) throws IOException,
            ClassNotFoundException {
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
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    protected Data_Collection getCollection(Data_CollectionID cID)
            throws IOException, ClassNotFoundException {
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
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    protected Data_Collection getCollection(Data_CollectionID cID,
            boolean hoome) throws IOException, ClassNotFoundException {
        try {
            Data_Collection c = getCollection(cID);
            return c;
        } catch (OutOfMemoryError err) {
            if (hoome) {
                de.clearMemoryReserve(env);
                de.clearSomeData();
                de.checkAndMaybeFreeMemory();
                de.initMemoryReserve(env);
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
        String m = "clearCollection" + cID.toString();
        de.logStartTagMem(m);
        data.put(cID, null);
        de.logEndTagMem(m);
    }

    /**
     * Caches and clears the first subset collection retrieved from an iterator.
     *
     * @return {@code true} iff a subset collection was cached and cleared.
     * @throws java.io.IOException If encountered.
     */
    public boolean clearSomeData() throws IOException {
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
        * @throws java.io.IOException If encountered.
  */
    public int clearAllData() throws IOException {
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
       * @throws java.io.IOException If encountered.
   */
    public void cacheCollection(Data_CollectionID cID, Object o)
            throws IOException {
        cache(getCollectionFile(cID), o);
    }

    /**
     * For loading a subset collection.
     *
     * @param cID the ID of subset collection to be loaded.
     * @return the subset collection loaded.
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public Object loadCollection(Data_CollectionID cID) throws IOException,
            ClassNotFoundException {
        return load(getCollectionFile(cID));
    }

    /**
     * For getting a subset collection file.
     *
     * @param cID the ID of subset collection.
     * @return the subset collection file for cID.
       * @throws java.io.IOException If encountered.
   */
    public Path getCollectionFile(Data_CollectionID cID) throws IOException {
        return Paths.get(de.files.getGeneratedDir().toString(),
                Data_Strings.s_DATA + Data_Strings.symbol_underscore + cID
                + de.files.DOT_DAT);
    }

    /**
     * Loads an Object from a File and reports this to the log.
     *
     * @param f the File to load an object from.
     * @return the object loaded.
       * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
   */
    protected Object load(Path f) throws IOException, ClassNotFoundException {
        String m = "load object from " + f.toString();
        env.logStartTag(m);
        Object r = Generic_IO.readObject(f);
        env.logEndTag(m);
        return r;
    }

    /**
     * Caches an Object to a File and reports this to the log.
     *
     * @param f the File to cache to.
     * @param o the Object to cache.
       * @throws java.io.IOException If encountered.
   */
    protected void cache(Path f, Object o) throws IOException {
        String m = "cache object to " + f.toString();
        env.logStartTag(m);
        Generic_IO.writeObject(o, f);
        env.logEndTag(m);
    }
}
