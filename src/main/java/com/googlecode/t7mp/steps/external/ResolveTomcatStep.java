package com.googlecode.t7mp.steps.external;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.steps.deployment.MyArtifactResolver;
import com.googlecode.t7mp.util.ZipUtil;

/**
 * 
 * 
 * @author jbellmann
 *
 */
public class ResolveTomcatStep implements Step {
	
	public static final String TOMCAT_GROUPID = "com.googlecode.t7mp";
	public static final String TOMCAT_ARTIFACTID = "tomcat";
	public static final String TOMCAT_TYPE = "zip";
	

	protected AbstractT7Mojo mojo;
	protected MyArtifactResolver myArtifactResolver;
	protected Log logger;
	
	@Override
	public void execute(Context context) {
		this.mojo = context.getMojo();
		this.myArtifactResolver = new MyArtifactResolver(context.getMojo());
		this.logger = context.getMojo().getLog();
		String version = context.getMojo().getTomcatVersion();
		File unpackDirectory = null;
		try {
			Artifact artifact = resolveTomcatArtifact(version + "-SNAPSHOT");
			unpackDirectory = getUnpackDirectory();
			ZipUtil.unzip(artifact.getFile(), unpackDirectory);
			copyToTomcatDirectory(unpackDirectory);
		} catch (MojoExecutionException e) {
			logger.error(e.getMessage(), e);
			throw new TomcatSetupException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new TomcatSetupException(e.getMessage(), e);
		}finally{
			if(unpackDirectory != null){
				try {
					FileUtils.deleteDirectory(unpackDirectory);
				} catch (IOException e) {
					logger.error("Could not delete tomcat upack directory : " + unpackDirectory.getAbsolutePath());
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	private void copyToTomcatDirectory(File unpackDirectory) throws IOException {
		File[] files = unpackDirectory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		// should only be one
		FileUtils.copyDirectory(files[0], this.mojo.getCatalinaBase());
	}

	protected Artifact resolveTomcatArtifact(String tomcatVersion) throws MojoExecutionException{
		Artifact artifact = myArtifactResolver.resolve(TOMCAT_GROUPID,
												TOMCAT_ARTIFACTID,
												tomcatVersion,
												null,
												TOMCAT_TYPE,
												Artifact.SCOPE_COMPILE);
		return artifact;
	}
	
	
	protected File getUnpackDirectory(){
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File upackDirectory = new File(tempDir, UUID.randomUUID().toString());
		upackDirectory.mkdirs();
		return upackDirectory;
	}

}
