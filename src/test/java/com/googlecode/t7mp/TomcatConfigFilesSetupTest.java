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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TomcatConfigFilesSetupTest {
	
	private File catalinaBaseDir;
	private static int counter = 1;
	
	private Log log = Mockito.mock(Log.class);
	private SetupUtil setupUtil = Mockito.mock(SetupUtil.class);
	
	private List<String> expectedFileNames = Arrays.asList(new String[]{"context.xml", "server.xml", "catalina.properties","catalina.policy", "logging.properties", "tomcat-users.xml", "web.xml"});
	@Before
	public void setUp(){
		catalinaBaseDir = new File(new File(System.getProperty("java.io.tmpdir")), "catalinaBase_" + (++counter));
		catalinaBaseDir.mkdirs();
		TomcatDirectorySetup directorySetup = new TomcatDirectorySetup(catalinaBaseDir, log);
		directorySetup.createTomcatDirectories();
	}
	
	@After
	public void tearDown() throws IOException{
		FileUtils.deleteDirectory(catalinaBaseDir);
		if(catalinaBaseDir.exists()){
			System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
		}
	}
	
	@Test
	public void testConfiguratorDefaultConfigFiles() throws MojoExecutionException{
		TomcatDirectorySetup directorySetup = new TomcatDirectorySetup(catalinaBaseDir, log);
		directorySetup.createTomcatDirectories();
		TomcatConfigFilesSetup fileSetup = new TomcatConfigFilesSetup(catalinaBaseDir, log, setupUtil);
		fileSetup.copyDefaultConfig();
		File confDir = new File(catalinaBaseDir, "/conf/");
		File[] createdDirectories = confDir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		List<String> fileNames = new ArrayList<String>();
		for(File file : createdDirectories) {
			fileNames.add(file.getName());
		}
		Collections.sort(expectedFileNames);
		Collections.sort(fileNames);
		Assert.assertEquals(expectedFileNames, fileNames);
	}
	
	@Test
	public void testUserConfigDirDoesNotExist() throws MojoExecutionException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, Mockito.mock(SetupUtil.class));
		configurator.copyUserConfigs(new File("/klaus"));
		Mockito.verify(log, Mockito.atLeast(2)).warn(Mockito.anyString());
	}
	
	@Test
	public void testUserConfigDirIsNotADirectory() throws MojoExecutionException, IOException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, Mockito.mock(SetupUtil.class));
		configurator.copyUserConfigs(File.createTempFile("test_", "tmp"));
		Mockito.verify(log, Mockito.atLeast(2)).warn(Mockito.anyString());
	}
	
	@Test
	public void testNoUserconfigDirConfigured() throws MojoExecutionException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, setupUtil);
		configurator.copyUserConfigs(null);
		Mockito.verify(log, Mockito.atLeast(1)).info(Mockito.anyString());
	}
	
	@Test
	public void testCopyUserconfigDir() throws MojoExecutionException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, setupUtil);
		configurator.copyDefaultConfig();
		File configFileDirectory = new File(catalinaBaseDir, "/conf");
		configurator.copyUserConfigs(configFileDirectory);
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testCopyUserconfigDirWithIOException() throws MojoExecutionException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, setupUtil);
		configurator.copyDefaultConfig();
		//
		TomcatConfigFilesSetup configurator2 = new TomcatConfigFilesSetup(catalinaBaseDir, log, new IOExceptionSetupUtil());
		File configFileDirectory = new File(catalinaBaseDir, "/conf");
		configurator2.copyUserConfigs(configFileDirectory);
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testCopyUserconfigDirWithFileNotFoundException() throws MojoExecutionException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, setupUtil);
		configurator.copyDefaultConfig();
		//
		TomcatConfigFilesSetup configurator2 = new TomcatConfigFilesSetup(catalinaBaseDir, log, new FileNotFoundExceptionSetupUtil());
		File configFileDirectory = new File(catalinaBaseDir, "/conf");
		configurator2.copyUserConfigs(configFileDirectory);
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testCopyConfigResourceWithIOException() throws IOException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, new IOExceptionSetupUtil());
		configurator.copyConfigResource("test");
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testCopyConfigResourceWithFileNotFoundException() throws IOException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, new FileNotFoundExceptionSetupUtil());
		configurator.copyConfigResource("test");
	}
	
	public static class IOExceptionSetupUtil implements SetupUtil {
		@Override
		public void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
			throw new IOException("TESTIOEXCEPTION");
		}

		@Override
		public void copyDirectory(File source, File target) throws IOException {
			throw new IOException("TESTIOEXCEPTION");
		}
	}
	
	public static class FileNotFoundExceptionSetupUtil implements SetupUtil {
		@Override
		public void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
			throw new FileNotFoundException("TESTIOEXCEPTION");
		}

		@Override
		public void copyDirectory(File source, File target) throws IOException {
			throw new FileNotFoundException("TESTIOEXCEPTION");			
		}
	}
}
