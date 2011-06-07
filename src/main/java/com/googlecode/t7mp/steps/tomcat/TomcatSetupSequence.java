package com.googlecode.t7mp.steps.tomcat;

import com.googlecode.t7mp.steps.DefaultStepSequence;
import com.googlecode.t7mp.steps.deployment.ArtifactDeploymentSequence;
import com.googlecode.t7mp.steps.directory.CreateTomcatDirectoriesSequence;
import com.googlecode.t7mp.steps.resources.ConfigFilesSequence;
import com.googlecode.t7mp.steps.resources.CopyConfigResourcesFromClasspathSequence;
import com.googlecode.t7mp.steps.resources.CopyProjectWebappStep;
import com.googlecode.t7mp.steps.resources.OverwriteWebXmlStep;
import com.googlecode.t7mp.steps.resources.SetSystemPropertiesStep;

public class TomcatSetupSequence extends DefaultStepSequence {

    public TomcatSetupSequence() {
        this.add(new CreateTomcatDirectoriesSequence());
        this.add(new CopyConfigResourcesFromClasspathSequence());
        this.add(new ConfigFilesSequence());
        this.add(new ArtifactDeploymentSequence());
        this.add(new CopyProjectWebappStep());
        this.add(new SetSystemPropertiesStep());
        this.add(new OverwriteWebXmlStep());
    }

}
