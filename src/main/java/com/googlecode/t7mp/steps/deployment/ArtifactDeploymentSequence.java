package com.googlecode.t7mp.steps.deployment;

import com.googlecode.t7mp.steps.DefaultStepSequence;

public class ArtifactDeploymentSequence extends DefaultStepSequence {

    public ArtifactDeploymentSequence() {
        this.add(new DefaultTomcatLibDeploymentStep());
        this.add(new AdditionalTomcatLibDeploymentStep());
        this.add(new WebappsDeploymentStep());
    }

}
