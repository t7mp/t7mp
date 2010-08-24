package com.googlecode.t7mp;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * This Mojo uses a global reference to shutdown.
 * 
 * @goal stop
 *
 */
public final class Stop extends AbstractMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if(GlobalTomcatHolder.bootstrap != null){
			try {
				GlobalTomcatHolder.bootstrap.stop();
				GlobalTomcatHolder.bootstrap = null;
			} catch (Exception e) {
				throw new MojoExecutionException(e.getMessage(), e);
			}
		}
	}
}