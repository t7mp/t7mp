package com.googlecode.t7mp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.IOUtil;


/**
 * Resolves artifacts from configuration-section and copy them to a specified directory.
 * 
 */
class ArtifactDispatcher {
	
	protected MyArtifactResolver myArtifactResolver;
	
	protected List<AbstractArtifact> resolvedArtifacts = new ArrayList<AbstractArtifact>();
	
	protected File catalinaBaseDir;
	
	ArtifactDispatcher(MyArtifactResolver myArtifactResolver, File catalinaBaseDir){
		this.myArtifactResolver = myArtifactResolver;
		this.catalinaBaseDir = catalinaBaseDir;
	}
	
	ArtifactDispatcher resolveArtifacts(List<? extends AbstractArtifact> artifacts) throws MojoExecutionException{
		for(AbstractArtifact abstractArtifact : artifacts){
			Artifact artifact = myArtifactResolver.resolve(abstractArtifact.getGroupId(), abstractArtifact.getArtifactId(), abstractArtifact.getVersion(), abstractArtifact.getType(), Artifact.SCOPE_COMPILE);
			abstractArtifact.setArtifact(artifact);
			resolvedArtifacts.add(abstractArtifact);
		}
		return this;
	}
	
	void copyTo(String directoryName) throws MojoExecutionException{
		for(AbstractArtifact artifact : this.resolvedArtifacts){
			try {
				String targetFileName = createTargetFileName(artifact);
				IOUtil.copy(new FileInputStream(artifact.getArtifact().getFile()), new FileOutputStream(new File(catalinaBaseDir, "/" + directoryName + "/" + targetFileName)));
			} catch (FileNotFoundException e) {
				throw new MojoExecutionException(e.getMessage(), e);
			} catch (IOException e) {
				throw new MojoExecutionException(e.getMessage(), e);
			}
		}
	}
	
	private String createTargetFileName(AbstractArtifact abstractArtifact){
		if(abstractArtifact.getClass().isAssignableFrom(WebappArtifact.class)){
			return abstractArtifact.getArtifactId() + "." + abstractArtifact.getType();
		}
		return abstractArtifact.getArtifactId() + "-" + abstractArtifact.getVersion() + "." + abstractArtifact.getType();
	}
	
	void clear(){
		resolvedArtifacts.clear();
	}
}
