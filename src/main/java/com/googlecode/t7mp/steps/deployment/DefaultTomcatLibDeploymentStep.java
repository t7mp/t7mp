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

import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractArtifact;
import com.googlecode.t7mp.DefaultTomcatArtifactDescriptorReader;
import com.googlecode.t7mp.JarArtifact;
import com.googlecode.t7mp.TomcatArtifactDescriptorReader;
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
