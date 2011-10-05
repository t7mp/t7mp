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
