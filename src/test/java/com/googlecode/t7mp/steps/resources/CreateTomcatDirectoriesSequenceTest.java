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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.io.Files;
import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SysoutLog;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;

@Deprecated
@Ignore
public class CreateTomcatDirectoriesSequenceTest {
    
    private File catalinaBaseDir;
    private Context context = Mockito.mock(Context.class);
    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
    private Log log = new SysoutLog();
    
    
    @Before
    public void setUp(){
	catalinaBaseDir = Files.createTempDir();
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
        Step createTomcatDirectories = new CreateTomcatDirectoriesSequence();
        createTomcatDirectories.execute(context);
        
        Assert.assertTrue(new File(catalinaBaseDir, "conf").exists());
        Assert.assertTrue(new File(catalinaBaseDir, "conf").isDirectory());
        
        Assert.assertTrue(new File(catalinaBaseDir, "webapps").exists());
        Assert.assertTrue(new File(catalinaBaseDir, "webapps").isDirectory());
        
        Assert.assertTrue(new File(catalinaBaseDir, "lib").exists());
        Assert.assertTrue(new File(catalinaBaseDir, "lib").isDirectory());
        
        Assert.assertTrue(new File(catalinaBaseDir, "temp").exists());
        Assert.assertTrue(new File(catalinaBaseDir, "temp").isDirectory());
        
        Assert.assertTrue(new File(catalinaBaseDir, "work").exists());
        Assert.assertTrue(new File(catalinaBaseDir, "work").isDirectory());
        
        Assert.assertTrue(new File(catalinaBaseDir, "logs").exists());
        Assert.assertTrue(new File(catalinaBaseDir, "logs").isDirectory());
    }


}
