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
package uk.ac.leeds.ccg.data.core;

import java.io.IOException;
import uk.ac.leeds.ccg.data.Data_Data;
import uk.ac.leeds.ccg.data.io.Data_Files;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.memory.Generic_MemoryManager;

/**
 * Data Environment.
 * 
 * @author Andy Turner
 * @version 1.0.0
 */
public class Data_Environment extends Generic_MemoryManager {

    private static final long serialVersionUID = 1L;

    public final transient Generic_Environment env;
    public final transient Data_Files files;
    public transient Data_Data data;
    
    /**
     * Stores the {@link #env} log ID for the log set up.
     */
    public transient int logID;

    public Data_Environment() throws IOException, Exception {
        this(new Generic_Environment(new Generic_Defaults()));
    }
    
    /**
     * @param e What {@link #env} is set to.
     * @throws java.io.IOException IFF there is one.
     */
    public Data_Environment(Generic_Environment e) throws IOException {
        this.env = e;
        files = new Data_Files(e.files.getDir());
    }

    public int initLog(String logname) throws IOException, Exception {
        logID = env.initLog(logname);
        return logID;
    }
    
    public void setDataEnvironment() {
        data.de = this;
    }

    /**
     * Attempts to clear some of {@link #data} using
     * {@link Data_Data#clearSomeData()}.
     *
     * @return {@code true} iff some data was successfully cleared.
     * @throws java.io.IOException If encountered.
     */
    public boolean clearSomeData() throws IOException {
        return data.clearSomeData();
    }

    @Override
    public boolean swapSomeData() throws IOException {
        boolean r = clearSomeData();
        if (r) {
            return r;
        } else {
            String m = "No data to clear. Do some coding to try to arrange to "
                    + "clear something else if needs be. If the program fails "
                    + "then perhaps try running it again, but providing more "
                    + "memory by adjusting the VM option -Xmx...";
            System.err.println();
            env.log(m);
            return r;
        }
    }

    @Override
    public boolean swapSomeData(boolean hoome) throws IOException {
        try {
            boolean r = swapSomeData();
            checkAndMaybeFreeMemory();
            return r;
        } catch (OutOfMemoryError e) {
            if (hoome) {
                clearMemoryReserve(env);
                boolean r = swapSomeData(HOOMEF);
                initMemoryReserve(env);
                return r;
            } else {
                throw e;
            }
        }
    }

    @Override
    public boolean checkAndMaybeFreeMemory() throws IOException {
        System.gc();
        while (getTotalFreeMemory() < Memory_Threshold) {
//            int clear = clearAllData();
//            if (clear == 0) {
//                return false;
//            }
            if (!swapSomeData()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 
     * @param s The tag name.
     */
    public final void logStartTagMem(String s) {
        env.logStartTag(s, logID);
        env.log("TotalFreeMemory " + getTotalFreeMemory());
    }

    public final void logEndTagMem(String s) {
        env.log("TotalFreeMemory " + getTotalFreeMemory());
        env.logEndTag(s, logID);
    }

    
    
}
