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
package uk.ac.leeds.ccg.data.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.leeds.ccg.data.core.Data_Strings;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Files;

/**
 * 
 * @author Andy Turner
 */
public class Data_Files extends Generic_Files {

    private static final long serialVersionUID = 1L;
    
    public final String DOT_DAT = Data_Strings.symbol_dot + Data_Strings.s_dat;
    
    /**
     * @param dir The directory.
     * @throws java.io.IOException If an IOException is encountered.
     */
    public Data_Files(Path dir) throws IOException {
        super(new Generic_Defaults(dir));
    }
    
    /**
     * @return {@code return new File(getDir(), Data_Strings.s_data)}
     */
    public Path getDataDir() {
        return Paths.get(getDir().toString(), Data_Strings.s_data);
    }
    
    public Path getEnvDataFile() throws IOException {
        return Paths.get(getGeneratedDir().toString(), "Env.dat");
    }
}
