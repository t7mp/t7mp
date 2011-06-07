package com.googlecode.t7mp.steps.deployment;

import java.util.List;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.DefaultTomcatArtifactDescriptorReader;
import com.googlecode.t7mp.TomcatArtifactDescriptorReader;
import com.googlecode.t7mp.artifact.AbstractArtifact;
import com.googlecode.t7mp.artifact.JarArtifact;
import com.googlecode.t7mp.steps.Context;

/**
 * 
 * 
 * 
 * @author jbellmann
 *
 */
public class DefaultTomcatLibDeploymentStep extends AbstractDeploymentStep {

    private TomcatArtifactDescriptorReader tomcatArtifactDescriptorReader;
    
    @Override
    public void execute(Context context) {
        tomcatArtifactDescriptorReader = new DefaultTomcatArtifactDescriptorReader(context.getLog());
        super.execute(context);
    }

    @Override
    protected List<AbstractArtifact> getArtifactList() {
        List<JarArtifact> jarList = tomcatArtifactDescriptorReader.getJarArtifacts(context.getMojo().getTomcatVersion());
        List<AbstractArtifact> artifactList = Lists.newArrayList();
        artifactList.addAll(jarList);
        return artifactList;
    }

}
