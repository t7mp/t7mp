package com.googlecode.t7mp;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TomcatConfiguratorTest {
	
	private File catalinaBaseDir;
	private static int counter = 1;
	
	private List<String> expectedDirectoryNames = Arrays.asList(new String[]{"conf","lib", "logs", "temp", "webapps", "work"});
	private List<String> expectedFileNames = Arrays.asList(new String[]{"context.xml", "server.xml", "catalina.properties","catalina.policy", "logging.properties", "tomcat-users.xml", "web.xml"});
	@Before
	public void setUp(){
		catalinaBaseDir = new File(new File(System.getProperty("java.io.tmpdir")), "catalinaBase_" + (++counter));
		catalinaBaseDir.mkdirs();
		catalinaBaseDir.deleteOnExit();
	}
	
	@After
	public void tearDown(){
		boolean deleted = catalinaBaseDir.delete();
		if(!deleted){
			System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
		}
	}
	
	@Test
	public void testConfigurator() throws MojoExecutionException{
		TomcatConfigurator configurator = new TomcatConfigurator(catalinaBaseDir);
		configurator.createTomcatDirectories();
		File[] createdDirectories = catalinaBaseDir.listFiles(new FileFilter(){
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		List<String> directoryNames = new ArrayList<String>();
		for(File directory : createdDirectories) {
			directoryNames.add(directory.getName());
		}
		Collections.sort(expectedDirectoryNames);
		Collections.sort(directoryNames);
		Assert.assertEquals(expectedDirectoryNames, directoryNames);
	}
	
	@Test
	public void testConfiguratorDefaultConfigFiles() throws MojoExecutionException{
		TomcatConfigurator configurator = new TomcatConfigurator(catalinaBaseDir);
		configurator.createTomcatDirectories();
		configurator.copyDefaultConfig();
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

}
