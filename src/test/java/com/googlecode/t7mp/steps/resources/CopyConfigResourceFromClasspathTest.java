package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.steps.directory.CreateTomcatDirectoriesSequence;
import com.googlecode.t7mp.steps.resources.CopyConfigResourceFromClasspath;

@Ignore
public class CopyConfigResourceFromClasspathTest {

    
    private File catalinaBaseDir;
    private Context context = Mockito.mock(Context.class);
    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
    private Log log = new SysoutLog();
    
    
    @Before
    public void setUp(){
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        catalinaBaseDir = new File(tempDir, UUID.randomUUID().toString());
        Mockito.when(context.getMojo()).thenReturn(mojo);
        Mockito.when(context.getLog()).thenReturn(log);
        Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
        
        Step createTomcatDirectories = new CreateTomcatDirectoriesSequence();
        createTomcatDirectories.execute(context);
        
        Assert.assertTrue(new File(catalinaBaseDir, "conf").exists());
        Assert.assertTrue(new File(catalinaBaseDir, "conf").isDirectory());
    }
    
    @After
    public void tearDown() throws IOException{
        FileUtils.deleteDirectory(catalinaBaseDir);
    }
    
    @Test
    public void testCreateDirectoryStep(){
        Step step = new CopyConfigResourceFromClasspath("context.xml");
        step.execute(context);
        File[] files = new File(catalinaBaseDir, "/conf/").listFiles();
        Assert.assertTrue(files.length == 1);
        Assert.assertTrue(files[0].getName().equals("context.xml"));
    }
}
