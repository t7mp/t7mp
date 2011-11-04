package com.googlecode.t7mp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


public final class CatalinaOutPrintStream extends PrintStream {
	
	private PrintStream originalSystemErr;

	public CatalinaOutPrintStream(PrintStream originalSystemErr, OutputStream outputStream) throws IOException {
		super(outputStream, true);
		this.originalSystemErr = originalSystemErr;
	}
	
	public PrintStream getOriginalSystemErr(){
		return this.originalSystemErr;
	}
	
	@Override
    public boolean checkError() {
        return originalSystemErr.checkError() || super.checkError();
    }

	@Override
    public void write(int x) {
        originalSystemErr.write(x); // "write once;
        super.write(x); // write somewhere else."
    }

	@Override
    public void write(byte[] x, int o, int l) {
        originalSystemErr.write(x, o, l); // "write once;
        super.write(x, o, l); // write somewhere else."
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
