package com.googlecode.t7mp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * This Mojo uses the StandardServer for shutdown.
 * 
 * @goal stop-server
 * 
 *
 */
public class StopServerMojo extends AbstractMojo {
	
	/**
	 * 
	 * @parameter expression="${t7.tomcat.shutdownCommand}" default-value="SHUTDOWN"
	 * 
	 */
	private String shutdownCommand;
	
	/**
	 * 
	 * @parameter expression="${t7.tomcat.shutdownPort}" default-value="8005"
	 * 
	 */
	private int shutdownPort;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("StopMojo -- shutdown Tomcat.");
		try {
			Socket socket = new Socket("localhost", shutdownPort);
			OutputStream out = socket.getOutputStream();
			for(int i = 0; i < shutdownCommand.length(); i++){
				out.write(shutdownCommand.charAt(i));
			}
			out.flush();
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
