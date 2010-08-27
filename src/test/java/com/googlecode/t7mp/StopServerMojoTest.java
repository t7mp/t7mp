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
		StopServerMojo mojo = new StopServerMojo();
		mojo.setShutdownCommand("TESTCOMMAND");
		mojo.setShutdownPort(server.getPort());
		mojo.execute();
		Thread.sleep(2000);
		Assert.assertEquals(server.getCommand(), mojo.getShutdownCommand());
		Assert.assertFalse(t.isAlive());
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testExecuteWithUnknowHostException() throws MojoExecutionException, MojoFailureException{
		StopServerMojo mojo = new StopServerMojo();
		mojo.setLog(Mockito.mock(Log.class));
		mojo.setShutdownCommand("SHUTDOWN");
		mojo.setShutdownPort(8005);
		mojo.setShutdownHost("petermeier");
		mojo.execute();
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testExecuteWithIOException() throws MojoExecutionException, MojoFailureException{
		StopServerMojo mojo = new StopServerMojo();
		mojo.setLog(Mockito.mock(Log.class));
		mojo.setShutdownCommand("SHUTDOWN");
		mojo.setShutdownPort(8005);
		mojo.setShutdownHost("localhost");
		mojo.execute();
	}
	
	@Test
	public void testDefaultValues(){
		StopServerMojo mojo = new StopServerMojo();
		Assert.assertEquals(8005, mojo.getShutdownPort());
		Assert.assertEquals("SHUTDOWN", mojo.getShutdownCommand());
		Assert.assertEquals("localhost", mojo.getShutdownHost());
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
