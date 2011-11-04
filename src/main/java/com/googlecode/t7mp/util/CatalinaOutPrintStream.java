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

    private PrintStream originalSystemErr;

    public CatalinaOutPrintStream(PrintStream originalSystemErr, OutputStream outputStream) throws IOException {
        super(outputStream, true);
        this.originalSystemErr = originalSystemErr;
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
        originalSystemErr.write(x);
        super.write(x);
    }

    @Override
    public void write(byte[] x, int o, int l) {
        originalSystemErr.write(x, o, l);
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
