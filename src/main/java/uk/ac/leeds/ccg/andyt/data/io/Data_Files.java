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
package uk.ac.leeds.ccg.andyt.data.io;

import java.io.File;
import uk.ac.leeds.ccg.andyt.data.core.Data_Strings;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Files;

/**
 *
 * @author geoagdt
 */
public class Data_Files extends Generic_Files {
    
    public final String DOT_DAT = strings.symbol_dot + strings.s_dat;
    
    /**
     *
     * @param strings
     * @param dir
     */
    public Data_Files(Data_Strings strings, File dir) {
        super(strings, dir);
    }
    
    public File getEnvDataFile() {
        return new File(getGeneratedDataDir(), "Env.dat");
    }
}
