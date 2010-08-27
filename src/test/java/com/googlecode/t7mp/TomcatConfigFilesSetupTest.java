package com.googlecode.t7mp;

import java.io.File;
import java.io.FileFilter;
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
import org.junit.Ignore;
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
		catalinaBaseDir.deleteOnExit();
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
		TomcatDirectorySetup directorySetup = new TomcatDirectorySetup(catalinaBaseDir);
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
	
	@Test(expected=TomcatSetupException.class)
	public void testUserConfigDirDoesNotExist() throws MojoExecutionException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, Mockito.mock(SetupUtil.class));
		configurator.copyUserConfigs(new File("/"));
	}
	
	@Ignore
	@Test(expected=TomcatSetupException.class)
	public void testUserConfigDirIsNotADirectory() throws MojoExecutionException, IOException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, Mockito.mock(SetupUtil.class));
		configurator.copyUserConfigs(File.createTempFile("test_", "tmp"));
	}
	
	@Test
	public void testNoUserconfigDirConfigured() throws MojoExecutionException{
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBaseDir, log, setupUtil);
		configurator.copyUserConfigs(null);
	}
	

	
	@Test(expected=TomcatSetupException.class)
	public void testCopyConfigResourceWithIOException() throws IOException{
		SetupUtil setupUtil = Mockito.mock(SetupUtil.class);
		Mockito.doThrow(new IOException("TESTEXCEPTION")).when(setupUtil).copy(Mockito.any(InputStream.class), Mockito.any(OutputStream.class));
		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(new File("/klaus"), log, setupUtil);
		configurator.copyConfigResource("test");
	}
	
	public static class ExceptionalSetupUtil implements SetupUtil {

		@Override
		public void copy(InputStream inputStream, OutputStream outputStream)
				throws IOException {
			throw new IOException("TESTIOEXCEPTION");
		}
		
	}
}
