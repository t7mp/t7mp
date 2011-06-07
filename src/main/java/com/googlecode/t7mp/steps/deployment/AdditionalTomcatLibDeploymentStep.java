package com.googlecode.t7mp.steps.deployment;

import java.util.List;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.artifact.AbstractArtifact;

public class AdditionalTomcatLibDeploymentStep extends AbstractDeploymentStep {

    protected List<AbstractArtifact> getArtifactList() {
        List<AbstractArtifact> artifactList = Lists.newArrayList();
        artifactList.addAll(context.getMojo().getLibs());
        return artifactList;
    }

}
