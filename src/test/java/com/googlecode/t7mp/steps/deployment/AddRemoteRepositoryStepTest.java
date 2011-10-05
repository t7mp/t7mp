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
package com.googlecode.t7mp.steps.deployment;

import java.util.List;

import junit.framework.Assert;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;

public class AddRemoteRepositoryStepTest {
    
    @Test
    public void testAddRemoteRepository(){
        AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
        List<ArtifactRepository> remoteRepos = Lists.newArrayList();
        Mockito.when(mojo.getRemoteRepos()).thenReturn(remoteRepos);
        Context context = new DefaultContext(mojo);
        AddRemoteRepositoryStep step = new AddRemoteRepositoryStep();
        step.execute(context);
        Assert.assertTrue("There should be an repository in the list.",remoteRepos.size() == 1);
        ArtifactRepository repository = remoteRepos.get(0);
        Assert.assertEquals("Id should be 't7mp.maven.repo.releases'","t7mp.maven.repo.releases", repository.getId());
    }

}
