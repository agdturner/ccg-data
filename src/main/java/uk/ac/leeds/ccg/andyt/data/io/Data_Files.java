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
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.data.core.Data_Strings;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Files;

/**
 *
 * @author geoagdt
 */
public class Data_Files extends Generic_Files {
    
    public final String DOT_DAT = Data_Strings.symbol_dot + Data_Strings.s_dat;
    
    /**
     * @param dir The directory.
     */
    public Data_Files(File dir) throws IOException {
        super(dir);
    }
    
    public File getEnvDataFile() {
        return new File(getGeneratedDir(), "Env.dat");
    }
}
