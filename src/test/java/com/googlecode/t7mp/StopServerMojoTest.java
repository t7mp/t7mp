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
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class StopServerMojoTest {
	
	@Test
	public void testExecute() throws IOException, MojoExecutionException, MojoFailureException, InterruptedException{
		FakeServer server = new FakeServer(8005);
		Thread t = new Thread(server);
		t.start();
		Thread.sleep(2000);
		StopServerMojo mojo = new StopServerMojo();
		mojo.setTomcatShutdownCommand("TESTCOMMAND");
		mojo.setTomcatShutdownPort(server.getPort());
		mojo.execute();
		Thread.sleep(2000);
		Assert.assertEquals(server.getCommand(), mojo.getTomcatShutdownCommand());
		Assert.assertFalse(t.isAlive());
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testExecuteWithUnknowHostException() throws MojoExecutionException, MojoFailureException{
		StopServerMojo mojo = new StopServerMojo();
		mojo.setLog(Mockito.mock(Log.class));
		mojo.setTomcatShutdownCommand("SHUTDOWN");
		mojo.setTomcatShutdownPort(8005);
		mojo.setTomcatShutdownHost("petermeier");
		mojo.execute();
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testExecuteWithIOException() throws MojoExecutionException, MojoFailureException{
		StopServerMojo mojo = new StopServerMojo();
		mojo.setLog(Mockito.mock(Log.class));
		mojo.setTomcatShutdownCommand("SHUTDOWN");
		mojo.setTomcatShutdownPort(8005);
		mojo.setTomcatShutdownHost("localhost");
		mojo.execute();
	}
	
	@Test
	public void testDefaultValues(){
		StopServerMojo mojo = new StopServerMojo();
		Assert.assertEquals(8005, mojo.getTomcatShutdownPort());
		Assert.assertEquals("SHUTDOWN", mojo.getTomcatShutdownCommand());
		Assert.assertEquals("localhost", mojo.getTomcatShutdownHost());
	}
	
	public class FakeServer implements Runnable {
		
		private String command = "NO_COMMAND";
		
		private int port;
		
		public FakeServer(int port){
			this.port = port;
		}
		
		public void start() throws IOException{
			ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();
			InputStream is = socket.getInputStream();
			StringBuilder sb = new StringBuilder();
			int i = 0;
			while((i = is.read()) != -1){
				sb.append((char)i);
			}
			socket.close();
			server.close();
			command = sb.toString();
		}

		public String getCommand() {
			return command;
		}

		public int getPort() {
			return port;
		}

		@Override
		public void run() {
			try {
				start();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	
	}

}
