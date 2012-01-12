/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
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
package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7BaseMojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.Step;

public class SetSystemPropertiesStepTest {

    private static final String PROPERTY_NAME = "PROPERTY_NAME_";
    private static final String PROPERTY_VALUE = "PROPERTY_VALUE_";

    private File catalinaBaseDir;
    private AbstractT7BaseMojo mojo = Mockito.mock(AbstractT7BaseMojo.class);
    private Log log = new SysoutLog();

    private Map<String, String> properties;

    @Before
    public void setUp() {
	File tempDir = new File(System.getProperty("java.io.tmpdir"));
	catalinaBaseDir = new File(tempDir, UUID.randomUUID().toString());
	boolean created = catalinaBaseDir.mkdirs();
	Assert.assertTrue(created);
	Assert.assertNotNull(catalinaBaseDir);
	Assert.assertTrue(catalinaBaseDir.exists());

	properties = getSystemPropertiesToSet();
    }
    
    @After
    public void clearProperties() {
	for (String key : properties.keySet()) {
	    System.clearProperty(key);
	}
    }

    @Test
    public void testSetSystemProperties() {
	Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
	Mockito.when(mojo.getSystemProperties()).thenReturn(properties);
	Mockito.when(mojo.getLog()).thenReturn(log);
	Context context = new DefaultContext(mojo);
	Step step = new SetSystemPropertiesStep();
	step.execute(context);
	// validate result
	for (String key : properties.keySet()) {
	    Assert.assertNotNull(System.getProperty(key));
	}
    }

    @Test
    public void testSetSystemPropertiesWithReplacement() {
	Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
	Mockito.when(mojo.getLog()).thenReturn(log);
	//
	properties.put("XXX", "${catalina.home}");
	properties.put("YYY", "${catalina.base}");

	Mockito.when(mojo.getSystemProperties()).thenReturn(properties);

	Context context = new DefaultContext(mojo);
	Step step = new SetSystemPropertiesStep();
	step.execute(context);
	// validate result
	for (String key : properties.keySet()) {
	    Assert.assertNotNull(System.getProperty(key));
	}
    }

    protected Map<String, String> getSystemPropertiesToSet() {
	Map<String, String> props = new HashMap<String, String>();
	for (int i = 0; i < 10; i++) {
	    props.put(PROPERTY_NAME + i, PROPERTY_VALUE + i);
	}
	return props;
    }

}
