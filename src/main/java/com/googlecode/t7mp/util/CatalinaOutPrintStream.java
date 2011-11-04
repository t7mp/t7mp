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
