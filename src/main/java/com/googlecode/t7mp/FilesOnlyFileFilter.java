package com.googlecode.t7mp;

import java.io.File;
import java.io.FileFilter;

/**
 * Removes directories.
 * 
 *
 */
public class FilesOnlyFileFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		return file.isFile();
	}

}