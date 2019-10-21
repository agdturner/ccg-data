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
package uk.ac.leeds.ccg.andyt.data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Random;
import java.util.logging.Level;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.data.core.Data_Object;

/**
 * Abstract class for handling {@link Data_AbstractRecord}'s.
 *
 * @version 1.0.0
 */
public abstract class Data_AbstractHandler extends Data_Object {

//	private static final long serialVersionUID = 1L;
    /**
     * For storing the length of an {@link Data_AbstractRecord} that this
     * Handler handles in measured in <code>byte</code> units and stored as a
     * <code>long</code>.
     */
    protected long recordLength;
    
    /**
     * @param e What {@link #env} is set to.
     */
    public Data_AbstractHandler(Data_Environment e){
        super(e);
    }

    /**
     * Returns a copy of {@link #recordLength}.
     *
     * @return A copy of {@link #recordLength}.
     */
    public long getRecordLength() {
        return this.recordLength;
    }

    /**
     * Formatted {@link File} for storing {@link Data_AbstractRecord}'s.
     */
    protected File file;

    /**
     * The workspace directory.
     */
    protected File dir;

    /**
     * @return {@link #dir}
     */
    public File getDir() {
        return dir;
    }

    public final void init(Level l, File dir) {
        this.dir = dir;
    }

    public final void init(File dir) {
        init(Level.FINE, dir);
    }

    /**
     * A {@link RandomAccessFile} of {@link #file}.
     */
    protected transient RandomAccessFile rAF;

    /**
     * Sets: {@link #file} to {@code f}; {@link #rAF} to
     * {@code new RandomAccessFile(file,"r" )}.
     *
     * @param f Formatted {@link File} containing {@link Data_AbstractRecord}'s.
     * @throws java.io.IOException if there is a problem accessing {@code f}.
     */
    protected void load(File f) throws IOException {
        this.file = f;
        if (f.exists()) {
            this.rAF = new RandomAccessFile(this.file, "r");
        } else {
            f.createNewFile();
            this.rAF = new RandomAccessFile(this.file, "rw");
        }
    }

    /**
     * @return The number of {@link Data_AbstractRecord}s stored in
     * {@link #rAF}.
     * @throws java.io.IOException if there is a problem accessing {@link #rAF}.
     */
    public long getNDataRecords() throws IOException {
        BigInteger abi = new BigInteger(Long.toString(this.rAF.length()));
        BigInteger bbi = new BigInteger(Long.toString(this.recordLength));
        return abi.divide(bbi).longValue();
    }

    /**
     * @return An {@link Data_AbstractRecord} for the given RecordID.
     * @param RecordID The RecordID of the {@link Data_AbstractRecord} to be
     * returned.
     */
    public abstract Data_AbstractRecord getDataRecord(long RecordID);

    /**
     * Prints a random set of {@code >n} {@link Data_AbstractRecord}s obtained
     * via {@link #getDataRecord(long)}.
     *
     * @param n the number of {@link Data_AbstractRecord}s to print out.
     * @param random the {@link Random} used for selecting
     * {@link Data_AbstractRecord}s to print.
     * @throws java.io.IOException if there is a problem accessing {@link #rAF}.
     */
    protected void print(int n, Random random) throws IOException {
        long nDataRecords = getNDataRecords();
        for (int i0 = 0; i0 < n; i0++) {
            double double0 = random.nextDouble() * nDataRecords;
            Data_AbstractRecord rec = getDataRecord((long) double0);
            env.env.log(rec.toString());
        }
    }
}
