/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.t7mp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 
 * @author jbellmann
 * 
 */
public final class CatalinaOutPrintStream extends PrintStream {

    private final PrintStream originalSystemErr;
    private final boolean suspendConsoleOutput;

    public CatalinaOutPrintStream(PrintStream originalSystemErr, OutputStream outputStream, boolean suspendConsoleOutput)
            throws IOException {
        super(outputStream, true);
        this.originalSystemErr = originalSystemErr;
        this.suspendConsoleOutput = suspendConsoleOutput;
    }

    public PrintStream getOriginalSystemErr() {
        return this.originalSystemErr;
    }

    @Override
    public boolean checkError() {
        return originalSystemErr.checkError() || super.checkError();
    }

    @Override
    public void write(int x) {
        if (!suspendConsoleOutput) {
            originalSystemErr.write(x);
        }
        super.write(x);
    }

    @Override
    public void write(byte[] x, int o, int l) {
        if (!suspendConsoleOutput) {
            originalSystemErr.write(x, o, l);
        }
        super.write(x, o, l);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void flush() {
        originalSystemErr.flush();
        super.flush();
    }

}
