package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.Step;

public class CopySetenvScripStepTest {
	
    private File catalinaBaseDir;
    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
    private Log log = new SysoutLog();
	
    
    @Before
    public void setUp(){
    	File tempDir = new File(System.getProperty("java.io.tmpdir"));
    	catalinaBaseDir = new File(tempDir, UUID.randomUUID().toString());
    	boolean created = catalinaBaseDir.mkdirs();
    	Assert.assertTrue(created);
    	Assert.assertNotNull(catalinaBaseDir);
    	Assert.assertTrue(catalinaBaseDir.exists());
    	
    	Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
    	Mockito.when(mojo.getLog()).thenReturn(log);
    }
    
    @After
    public void tearDown() throws IOException{
    	FileUtils.deleteDirectory(catalinaBaseDir);
    }
    
	@Test
	public void testCopySetenvScriptStepNotExist(){
		Context context = new DefaultContext(mojo);
		Step step = new CopySetenvScriptStep();
		step.execute(context);
	}
	
	@Test
	public void testCopySetenvScriptStep(){
		String setenvScriptPath = getClass().getResource("/com/googlecode/t7mp/bin/setenv.sh").getPath();
		File t7mpDirectory = new File(setenvScriptPath).getParentFile().getParentFile();
		File confDirectory = new File(t7mpDirectory, "/conf/");
		Assert.assertNotNull(confDirectory);
		Assert.assertTrue(confDirectory.exists());
		Mockito.when(mojo.getTomcatConfigDirectory()).thenReturn(new File(t7mpDirectory, "/conf/"));
		Context context = new DefaultContext(mojo);
		Step step = new CopySetenvScriptStep();
		step.execute(context);
		File binDirectory = new File(catalinaBaseDir, "/bin/");
		Assert.assertNotNull(binDirectory);
		Assert.assertTrue(binDirectory.exists());
		File copyiedSetenvScript = new File(binDirectory, "setenv.sh");
		
		Assert.assertNotNull(copyiedSetenvScript);
		Assert.assertTrue(copyiedSetenvScript.exists());
	}

}
