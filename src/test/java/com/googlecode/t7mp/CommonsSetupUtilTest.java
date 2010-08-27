package com.googlecode.t7mp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class CommonsSetupUtilTest {
	
	@Test
	public void testCopy() throws IOException{
		
		String message = "TEST";
		
		File source = File.createTempFile("source", ".tmp");
		source.deleteOnExit();
		File target = File.createTempFile("target", ".tmp");
		target.deleteOnExit();
		FileWriter sourceWriter = new FileWriter(source);
		sourceWriter.write(message);
		sourceWriter.close();
		SetupUtil setupUtil = new CommonsSetupUtil();
		setupUtil.copy(new FileInputStream(source), new FileOutputStream(target));
		FileReader reader = new FileReader(target);
		char[] buffer = new char[10];
		reader.read(buffer);
		reader.close();
		String result = new String(buffer);
		Assert.assertEquals(message, result.trim());
	}

}
