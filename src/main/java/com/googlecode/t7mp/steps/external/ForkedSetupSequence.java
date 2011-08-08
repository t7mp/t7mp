package com.googlecode.t7mp.steps.external;

import com.googlecode.t7mp.steps.DefaultStepSequence;
import com.googlecode.t7mp.steps.deployment.AdditionalTomcatLibDeploymentStep;
import com.googlecode.t7mp.steps.deployment.CheckT7ArtifactsStep;
import com.googlecode.t7mp.steps.deployment.WebappsDeploymentStep;
import com.googlecode.t7mp.steps.resources.ConfigFilesSequence;
import com.googlecode.t7mp.steps.resources.CopyProjectWebappStep;
import com.googlecode.t7mp.steps.resources.OverwriteWebXmlStep;

public class ForkedSetupSequence extends DefaultStepSequence {
	
	public ForkedSetupSequence(){
        add(new CheckT7ArtifactsStep());
		add(new ResolveTomcatStep());
        add(new ConfigFilesSequence());
        add(new AdditionalTomcatLibDeploymentStep());
        add(new WebappsDeploymentStep());
        add(new CopyProjectWebappStep());
        add(new OverwriteWebXmlStep());
	}

}