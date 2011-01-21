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

import org.apache.maven.artifact.Artifact;

/**
 * TODO Comment.
 * @author jbellmann
 *
 */
public abstract class AbstractArtifact {

    /**
     * 
     * @parameter
     * @required
     */
    protected String groupId;

    /**
     * 
     * @parameter
     * @required
     */
    protected String artifactId;

    /**
     * 
     * @parameter
     */
    protected String version;

    /**
     * 
     * @parameter
     */
    protected String type;

    /**
     * 
     * @parameter
     */
    protected String classifier;

    protected Artifact artifact;

    AbstractArtifact() {
        // default constructor
    }

    AbstractArtifact(Artifact artifact) {
        this.setArtifact(artifact);
        this.setGroupId(artifact.getGroupId());
        this.setArtifactId(artifact.getArtifactId());
        this.setVersion(artifact.getVersion());
        this.setClassifier(artifact.getClassifier());
        this.setType(artifact.getType());
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    abstract String getType();

    public void setType(String type) {
        this.type = type;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getGroupId()).append(":");
        sb.append(getArtifactId()).append(":");
        sb.append(getVersion()).append(":");
        sb.append(getClassifier()).append(":");
        sb.append(getType());
        return sb.toString().trim();
    }

}
