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

import java.io.File;
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.data.io.Data_Files;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Defaults;

/**
 *
 * @author geoagdt
 */
public class Data_Environment  {
    
    public transient Generic_Environment env;
    public transient Data_Files files;
    
    public Data_Environment() throws IOException {
        this(new Generic_Environment());
    }
    
    /**
     * Defaults dir to: {@link Generic_Defaults#getDefaultDir()}.
     * {@link #Data_Environment(Generic_Environment, File)}
     * @param env The default.
     */
    public Data_Environment(Generic_Environment env) {
        this(env, Generic_Defaults.getDefaultDir());
    }

    /**
     * 
     * @param env What {@link #env} is set to. 
     * @param dir Directory used to initialise {@link #files}. 
     */
    public Data_Environment(Generic_Environment env, File dir) {
        this.env = env;
        files = new Data_Files(dir);
    }
    
}
