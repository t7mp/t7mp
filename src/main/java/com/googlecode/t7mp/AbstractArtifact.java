package com.googlecode.t7mp;

import org.apache.maven.artifact.Artifact;

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
	
	AbstractArtifact(){
		// default constructor
	}
	
	AbstractArtifact(Artifact artifact){
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

	public String getType() {
		return type;
	}

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

}
