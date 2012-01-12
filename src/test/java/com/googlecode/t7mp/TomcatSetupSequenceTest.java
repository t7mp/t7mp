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
package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.io.Files;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.StepSequence;
import com.googlecode.t7mp.steps.deployment.TomcatSetupSequence;

@Ignore
public class TomcatSetupSequenceTest {
    
    private final static String TOMCAT_VERSION = "7.0.14";
    
    private File catalinaBaseDir;
    private Context context;
    private AbstractT7BaseMojo mojo = Mockito.mock(AbstractT7BaseMojo.class);
    private Log log = new SysoutLog();
    
    private ArtifactResolver resolver = Mockito.mock(ArtifactResolver.class);
    private ArtifactFactory factory = Mockito.mock(ArtifactFactory.class);
    private ArtifactRepository local = Mockito.mock(ArtifactRepository.class);
    
    
    @Before
    public void setUp(){
	catalinaBaseDir = Files.createTempDir();
        Mockito.when(mojo.getLog()).thenReturn(log);
        Mockito.when(mojo.getCatalinaBase()).thenReturn(catalinaBaseDir);
        Mockito.when(mojo.getTomcatVersion()).thenReturn(TOMCAT_VERSION);
        // 
        context = new DefaultContext(mojo);
    }
    
    @After
    public void tearDown() throws IOException{
        FileUtils.deleteDirectory(catalinaBaseDir);
    }
    
    @Test
    public void testTomcatSetupSequence(){
        StepSequence setupSequence = new TomcatSetupSequence();
        setupSequence.execute(context);
    }

}
