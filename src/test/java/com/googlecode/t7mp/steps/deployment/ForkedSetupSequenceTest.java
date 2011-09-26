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
import com.googlecode.t7mp.steps.resources.CopyProjectWebappStep;
import com.googlecode.t7mp.steps.resources.OverwriteWebXmlStep;

public class ForkedSetupSequenceTest {

    @Test
    public void testForkedSetupSequence(){
        TestForkedSetupSequence sequence = new TestForkedSetupSequence();
        Assert.assertEquals("7 Steps expected", 7, sequence.getSteps().size());
//        Step one = sequence.getSteps().get(0);
//        Assert.assertTrue(one instanceof AddRemoteRepositoryStep);
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
        Assert.assertTrue(seven instanceof CopyProjectWebappStep);
//        Step eight = sequence.getSteps().get(7);
//        Assert.assertTrue(eight instanceof SetSystemPropertiesStep);
        Step nine = sequence.getSteps().get(6);
        Assert.assertTrue(nine instanceof OverwriteWebXmlStep);
        
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
