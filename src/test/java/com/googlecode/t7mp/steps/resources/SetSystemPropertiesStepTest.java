package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.SystemProperty;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.Step;

public class SetSystemPropertiesStepTest {
	
	private static final String PROPERTY_NAME = "PROPERTY_NAME_";
	private static final String PROPERTY_VALUE = "PROPERTY_VALUE";
	
	private static final String TEST_NULL_VALUE = "______";
	
    private File catalinaBaseDir;
    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
    private Log log = new SysoutLog();
    
	private ArrayList<SystemProperty> propertiesList;
	
    @Before
    public void setUp(){
    	File tempDir = new File(System.getProperty("java.io.tmpdir"));
    	catalinaBaseDir = new File(tempDir, UUID.randomUUID().toString());
    	boolean created = catalinaBaseDir.mkdirs();
    	Assert.assertTrue(created);
    	Assert.assertNotNull(catalinaBaseDir);
    	Assert.assertTrue(catalinaBaseDir.exists());
    	
    	propertiesList = getSystemPropertiesToSet();
		for(SystemProperty sp : propertiesList){
			String value = System.getProperty(sp.getKey());
			Assert.assertTrue(value == null || value.equals(TEST_NULL_VALUE));
		}
    }
    
    @After
    public void tearDown(){
    	for(SystemProperty sp : propertiesList){
    		System.setProperty(sp.getKey(), "______");
    	}
    }
    
	@Test
	public void testSetSystemProperties(){
		Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
		Mockito.when(mojo.getSystemProperties()).thenReturn(propertiesList);
		Mockito.when(mojo.getLog()).thenReturn(log);
		Context context = new DefaultContext(mojo);
		Step step = new SetSystemPropertiesStep();
		step.execute(context);
		// validate result
		for(SystemProperty sp : propertiesList){
			Assert.assertNotNull(System.getProperty(sp.getKey()));
		}
	}
	
	@Test
	public void testSetSystemPropertiesWithReplacement(){
		Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
		Mockito.when(mojo.getLog()).thenReturn(log);
		// 
		SystemProperty catalinaHomeReplacement = new SystemProperty();
		catalinaHomeReplacement.setKey("XXX");
		catalinaHomeReplacement.setValue("${catalina.home}");
		propertiesList.add(catalinaHomeReplacement);
		SystemProperty catalinaBaseReplacement = new SystemProperty();
		catalinaBaseReplacement.setKey("YYY");
		catalinaBaseReplacement.setValue("${catalina.base}");
		propertiesList.add(catalinaBaseReplacement);
		
		Mockito.when(mojo.getSystemProperties()).thenReturn(propertiesList);

		Context context = new DefaultContext(mojo);
		Step step = new SetSystemPropertiesStep();
		step.execute(context);
		// validate result
		for(SystemProperty sp : propertiesList){
			Assert.assertNotNull(System.getProperty(sp.getKey()));
		}
	}
	
	protected ArrayList<SystemProperty> getSystemPropertiesToSet(){
		ArrayList<SystemProperty> propertiesList = new ArrayList<SystemProperty>();
		for(int i = 0; i < 10; i++){
			SystemProperty sp = new SystemProperty();
			sp.setKey(PROPERTY_NAME + i);
			sp.setValue(PROPERTY_VALUE + i);
			propertiesList.add(sp);
		}
		return propertiesList;
	}

}
