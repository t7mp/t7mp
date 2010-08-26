package com.googlecode.t7mp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * 
 * 
 *
 */
public class CommonsSetupUtil implements SetupUtil {

	@Override
	public void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		IOUtils.copy(inputStream, outputStream);
	}

}