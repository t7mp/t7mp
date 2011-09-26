package com.googlecode.t7mp.steps.deployment;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.steps.deployment.TomcatSetupSequenceTest.TestTomcatSetupSequence;
import com.googlecode.t7mp.steps.resources.ConfigFilesSequence;
import com.googlecode.t7mp.steps.resources.CopyConfigResourcesFromClasspathSequence;
import com.googlecode.t7mp.steps.resources.CopyProjectWebappStep;
import com.googlecode.t7mp.steps.resources.OverwriteWebXmlStep;
import com.googlecode.t7mp.steps.resources.SetSystemPropertiesStep;

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
    }
    
    static class TestForkedSetupSequence extends ForkedSetupSequence {

        public List<Step> getSteps(){
            return super.sequence;
        }
    }

}
