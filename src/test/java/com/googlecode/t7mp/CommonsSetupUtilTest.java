/**
 * Copyright (C) 2010 Joerg Bellmann <joerg.bellmann@googlemail.com>
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
