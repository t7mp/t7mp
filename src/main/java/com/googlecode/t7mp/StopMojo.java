package com.googlecode.t7mp;

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * This Mojo uses the plugin context to get a reference to shutdown.
 * 
 * @goal stop
 *
 */
public final class StopMojo extends AbstractMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Bootstrap bootstrap = (Bootstrap) getPluginContext().get(AbstractT7Mojo.T7_BOOTSTRAP_CONTEXT_ID);
		getPluginContext().remove(AbstractT7Mojo.T7_BOOTSTRAP_CONTEXT_ID);
		if(bootstrap != null){
			try {
				bootstrap.stop();
			} catch (Exception e) {
				throw new MojoExecutionException("Error stopping the Tomcat with Bootstrap from Plugin-Context", e);
			}
		}
	}
}