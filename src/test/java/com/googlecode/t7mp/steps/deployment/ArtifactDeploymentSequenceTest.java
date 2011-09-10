package com.googlecode.t7mp.steps.deployment;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.t7mp.steps.Step;

public class ArtifactDeploymentSequenceTest {
    
    @Test
    public void testArtifactDeploymentSequence(){
        TestArtifactDeploymentSequence sequence = new TestArtifactDeploymentSequence();
        Assert.assertEquals("2 Steps expected", 2,sequence.getSteps().size());
        Step first = sequence.getSteps().get(0);
        Assert.assertTrue(first instanceof AdditionalTomcatLibDeploymentStep);
        Step second = sequence.getSteps().get(1);
        Assert.assertTrue(second instanceof WebappsDeploymentStep);
    }
    
    static class TestArtifactDeploymentSequence extends ArtifactDeploymentSequence {
        
        public List<Step> getSteps(){
            return super.sequence;
        }
    }

}
