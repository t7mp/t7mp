package com.googlecode.t7mp.steps.deployment;

import com.googlecode.t7mp.steps.resources.ConfigFilesSequence;
import com.googlecode.t7mp.steps.resources.CopyConfigResourcesFromClasspathSequence;
import com.googlecode.t7mp.steps.resources.WebappSequence;

/**
 * Comment.
 * 
 * @author jbellmann
 *
 */
public class ForkedTomeeSetupSequence extends SetupStepSequence {

    public ForkedTomeeSetupSequence() {
        add(new CheckT7ArtifactsStep());
        add(new ResolveTomeeStep());
        add(new CopyConfigResourcesFromClasspathSequence());
        add(new ConfigFilesSequence());
        add(new ArtifactDeploymentSequence());
        add(new WebappSequence());
        //        add(new CopyProjectWebappStep());
        //        add(new OverwriteWebXmlStep());
    }
}
