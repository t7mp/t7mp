package com.googlecode.t7mp.steps.directory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.steps.directory.CreateTomcatDirectoryStep;

public class CreateTomcatDirectoryStepTest {
    
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
    }
    
    @After
    public void tearDown() throws IOException{
        FileUtils.deleteDirectory(catalinaBaseDir);
    }
    
    @Test
    public void testCreateDirectoryStep(){
        testDirectory("conf");
        testDirectory("lib");
        testDirectory("webapps");
        testDirectory("work");
        testDirectory("temp");
        testDirectory("logs");
    }
    
    private void testDirectory(String directory){
        Step step = new CreateTomcatDirectoryStep(directory);
        step.execute(context);
        Assert.assertTrue(new File(catalinaBaseDir, directory).exists());
        Assert.assertTrue(new File(catalinaBaseDir, directory).isDirectory());
    }

}
