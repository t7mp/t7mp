/**
 * Copyright (C) 2010 Joerg Bellmann <joerg.bellmann@googlemail.com>
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

package com.googlecode.t7mp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * TODO Comment.
 * @author jbellmann
 *
 */
final class TomcatJarArtifactHelper {

    protected Properties tomcatLibs = new Properties();

    List<JarArtifact> getTomcatArtifacts(String tomcatVersion) throws MojoExecutionException {
        List<JarArtifact> tomcatArtifactList = new ArrayList<JarArtifact>();
        //		Properties tomcatLibs = new Properties();
        try {
            tomcatLibs.load(TomcatJarArtifactHelper.class.getResourceAsStream("artifacts.properties"));
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        for (Map.Entry<Object, Object> entry : tomcatLibs.entrySet()) {
            String groupId = entry.getKey().toString().substring(0, entry.getKey().toString().lastIndexOf("."));
            String artifactId = entry.getValue().toString();
            String version = null;
            if (artifactId.startsWith("tomcat-")) {
                version = tomcatVersion;
            } else {
                String[] split = artifactId.split(":");
                version = split[1];
                artifactId = split[0];
            }
            JarArtifact jarArtifact = new JarArtifact();
            jarArtifact.setGroupId(groupId);
            jarArtifact.setArtifactId(artifactId);
            jarArtifact.setVersion(version);
            tomcatArtifactList.add(jarArtifact);
        }
        return tomcatArtifactList;
    }

}
