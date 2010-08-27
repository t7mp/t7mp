package com.googlecode.t7mp;

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * This Mojo uses a global reference to shutdown.
 * 
 * @goal stop
 *
 */
public final class StopMojo extends AbstractMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Bootstrap bootstrap = (Bootstrap) getPluginContext().get("TEST");
		if(bootstrap != null){
			try {
				bootstrap.stop();
			} catch (Exception e) {
				throw new MojoExecutionException("tst");
			}
		}
//		if(GlobalTomcatHolder.bootstrap != null){
//			try {
//				GlobalTomcatHolder.bootstrap.stop();
//				GlobalTomcatHolder.bootstrap = null;
//			} catch (Exception e) {
//				throw new MojoExecutionException(e.getMessage(), e);
//			}
//		}
	}
}