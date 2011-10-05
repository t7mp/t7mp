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

import com.googlecode.t7mp.steps.resources.ConfigFilesSequence;
import com.googlecode.t7mp.steps.resources.CopyConfigResourcesFromClasspathSequence;
import com.googlecode.t7mp.steps.resources.WebappSequence;

/**
 * Combines all steps needed for setting up a forked-tomcat with all configured
 * elements.
 * 
 * @author jbellmann
 *
 */
public class ForkedSetupSequence extends SetupStepSequence {

    public ForkedSetupSequence() {
        add(new CheckT7ArtifactsStep());
        add(new ResolveTomcatStep());
        add(new CopyConfigResourcesFromClasspathSequence());
        add(new ConfigFilesSequence());
        add(new ArtifactDeploymentSequence());
        add(new WebappSequence());
//        add(new CopyProjectWebappStep());
//        add(new OverwriteWebXmlStep());
    }

}
