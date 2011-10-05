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

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.steps.resources.ConfigFilesSequence;
import com.googlecode.t7mp.steps.resources.CopyConfigResourcesFromClasspathSequence;
import com.googlecode.t7mp.steps.resources.WebappSequence;

public class ForkedSetupSequenceTest {

    @Test
    public void testForkedSetupSequence(){
        TestForkedSetupSequence sequence = new TestForkedSetupSequence();
        Assert.assertEquals("6 Steps expected", 6, sequence.getSteps().size());
        Step two = sequence.getSteps().get(0);
        Assert.assertTrue(two instanceof CheckT7ArtifactsStep);
        Step three = sequence.getSteps().get(1);
        Assert.assertTrue(three instanceof ResolveTomcatStep);
        Step four = sequence.getSteps().get(2);
        Assert.assertTrue(four instanceof CopyConfigResourcesFromClasspathSequence);
        Step five = sequence.getSteps().get(3);
        Assert.assertTrue(five instanceof ConfigFilesSequence);
        Step six = sequence.getSteps().get(4);
        Assert.assertTrue(six instanceof ArtifactDeploymentSequence);
        Step seven = sequence.getSteps().get(5);
        Assert.assertTrue(seven instanceof WebappSequence);

        AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
        List<ArtifactRepository> repositoryList = Lists.newArrayList();
        Mockito.when(mojo.getRemoteRepos()).thenReturn(repositoryList);
        Mockito.when(mojo.isAddGithubRepository()).thenReturn(true);
        
        sequence.execute(new DefaultContext(mojo));
        
        Assert.assertEquals("The github.repo should be in the list", 1, repositoryList.size());
    }
    
    static class TestForkedSetupSequence extends ForkedSetupSequence {

        public List<Step> getSteps(){
            return super.sequence;
        }
        
        @Override
        public void execute(Context context){
            sequence = new ArrayList<Step>();
            super.execute(context);
        }
    }

}
