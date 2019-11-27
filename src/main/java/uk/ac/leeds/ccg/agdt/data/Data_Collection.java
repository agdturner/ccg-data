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
package uk.ac.leeds.ccg.agdt.data;

import java.io.Serializable;
import uk.ac.leeds.ccg.agdt.data.id.Data_RecordID;
import uk.ac.leeds.ccg.agdt.data.id.Data_CollectionID;
import java.util.HashMap;

/**
 * For a {@link Data_Record} collection.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_Collection implements Serializable {

    public final Data_CollectionID ID;
    public final HashMap<? super Data_RecordID, ? super Data_Record> data;

    public Data_Collection(Data_CollectionID ID) {
        this.ID = ID;
        data = new HashMap<>();
    }

    public Data_Collection(Data_CollectionID ID,
            HashMap<Data_RecordID, Data_Record> data) {
        this.ID = ID;
        this.data = data;
    }

    /**
     * For getting the number of records in the collection.
     *
     * @return the size of {@link data}
     */
    public int getN() {
        return data.size();
    }

}
