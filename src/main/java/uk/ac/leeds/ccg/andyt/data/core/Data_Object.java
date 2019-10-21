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
package uk.ac.leeds.ccg.andyt.data.core;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class Data_Object implements Serializable {
    
    public transient Data_Environment env;

    /**
     * @throws IOException if there is a problem initialising {@link #env}.
     */
    public Data_Object() throws IOException {
        this(new Data_Environment());
    }
    
    /**
     * @param e What {@link #env} is set to.
     */
    public Data_Object(Data_Environment e) {
        this.env = e;
    }
    
}
