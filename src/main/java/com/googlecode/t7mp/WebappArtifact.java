package com.googlecode.t7mp;

/**
 * 
 * 
 *
 */
public class WebappArtifact extends AbstractArtifact {
	
	private String contextPath;

	@Override
	public String getType() {
		return "war";
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
}