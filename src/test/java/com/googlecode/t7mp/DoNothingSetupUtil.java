package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DoNothingSetupUtil implements SetupUtil {

	@Override
	public void copy(InputStream inputStream, OutputStream outputStream) throws IOException {

	}

	@Override
	public void copyDirectory(File source, File target) throws IOException {
				
	}

}
