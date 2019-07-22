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
import uk.ac.leeds.ccg.andyt.data.io.Data_Files;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;

/**
 *
 * @author geoagdt
 */
public class Data_Environment {
    
    public final transient Generic_Environment ge;
    public final transient Data_Files files;
    public final transient Data_Strings strings;
    
    public Data_Environment() {
        this(new Generic_Environment());
    }
    public Data_Environment(Generic_Environment ge) {
        this(ge, ge.files.getDataDir());
    }
    
    public Data_Environment(File dataDir) {
        this(new Generic_Environment(), dataDir);
    }
    
    public Data_Environment(Generic_Environment ge, File dataDir) {
        this.ge = ge;
        strings = new Data_Strings();
        files = new Data_Files(strings, dataDir);
    }
    
}
