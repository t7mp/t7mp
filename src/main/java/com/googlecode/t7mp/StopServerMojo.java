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

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * This Mojo uses the StandardServer for shutdown.
 * 
 * @goal stop-server
 * 
 *
 */
public class StopServerMojo extends AbstractT7Mojo {
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("StopMojo -- shutdown Tomcat.");
			Socket socket = new Socket(tomcatShutdownHost, tomcatShutdownPort);
			OutputStream out = socket.getOutputStream();
			for(int i = 0; i < tomcatShutdownCommand.length(); i++){
				out.write(tomcatShutdownCommand.charAt(i));
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