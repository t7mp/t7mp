package com.googlecode.t7mp;

import java.util.List;

public interface TomcatArtifactDescriptorReader {
	
	List<JarArtifact> getJarArtifacts(String tomcatVersion);

}