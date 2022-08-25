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

import java.io.Serializable;

/**
 * An abstract class for identifiers.
 *
 * @author Andy Turner
 * @version 1.0
 */
public abstract class Data_ID implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance.
     */
    public Data_ID(){}
    
    /** 
     * @return {@code getClass().getTypeName()}
     */
    public String getTypeName() {
        return getClass().getTypeName();
    }
    
    /** 
     * @return {@code getClass().getSimpleName()}
     */
    public String getSimpleName() {
        return getClass().getSimpleName();
    }
}
