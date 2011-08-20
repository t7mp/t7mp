package com.googlecode.t7mp.steps.resources;

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.Step;

public class OverwriteWebXmlStepTest {
	
    private File catalinaBaseDir;
    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
    private Log log = new SysoutLog();
    
    @Before
    public void setUp(){
    	Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
    	Mockito.when(mojo.getLog()).thenReturn(log);
    }
	
	@Test
	public void testOverwriteWebXmlIsNull(){
    	Mockito.when(mojo.getOverwriteWebXML()).thenReturn(null);
		Context context = new DefaultContext(mojo);
		Step step = new OverwriteWebXmlStep();
		step.execute(context);
	}
	
	@Test
	public void testOverwriteWebXmlDoesNotExist(){
		File notExistentFile = new File("/klaus/peter");
    	Mockito.when(mojo.getOverwriteWebXML()).thenReturn(notExistentFile);
		Context context = new DefaultContext(mojo);
		Step step = new OverwriteWebXmlStep();
		step.execute(context);
	}

}
