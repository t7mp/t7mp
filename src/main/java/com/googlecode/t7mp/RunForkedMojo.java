package com.googlecode.t7mp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.googlecode.t7mp.scanner.ScannerSetup;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.StepSequence;
import com.googlecode.t7mp.steps.external.ForkedSetupSequence;
import com.googlecode.t7mp.steps.resources.CopySetenvScriptStep;
import com.googlecode.t7mp.util.SystemUtil;
import com.googlecode.t7mp.util.TomcatUtil;


/**
 * 
 * @goal run-forked
 * @requiresDependencyResolution test
 * 
 *
 */
public class RunForkedMojo extends AbstractT7Mojo {
	
	private Process p;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		PreConditions.checkConfiguredTomcatVersion(getLog(), tomcatVersion);

		getSetupStepSequence().execute(new DefaultContext(this));
		setStartScriptPermissions(TomcatUtil.getBinDirectory(this.getCatalinaBase()));
		getLog().info("Starting Tomcat ...");
		try {
		    
			if(this.tomcatSetAwait){
				startTomcat();
			} else {
				new Runner().start();
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void startTomcat() {
		ProcessBuilder processBuilder = new ProcessBuilder(TomcatUtil.getStartScriptName(), "run");
		processBuilder.directory(TomcatUtil.getBinDirectory(getCatalinaBase()));
		processBuilder.redirectErrorStream(true);
		int exitValue = -1;
		try{
			this.p = processBuilder.start();
			final ForkedTomcatProcessShutdownHook shutdownHook = new ForkedTomcatProcessShutdownHook(this.p, getLog());
			ScannerSetup.configureScanners(shutdownHook, this);
			Runtime.getRuntime().addShutdownHook(shutdownHook);
			
			InputStream is = this.p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			do {
			    line = getNextLine( br );
            } while( line != null );
			//
			exitValue = this.p.waitFor();
		}catch(InterruptedException e){
			getLog().error(e.getMessage(), e);
		} catch (IOException e) {
			getLog().error(e.getMessage(), e);
		}
		getLog().info("Exit-Value " + exitValue);
	}

    private String getNextLine( BufferedReader br ) {
        String line;
        try {
            line = br.readLine();
            System.out.println(line);
        } catch ( IOException e ) {
            line = null;
        }
        return line;
    }

	private void setStartScriptPermissions(File binDirectory) {
		if(SystemUtil.isWindowsSystem()){
			// do we have filepermissions on windows
			return;
		}
		ProcessBuilder processBuilder = new ProcessBuilder("chmod", "755", "catalina.sh", "setclasspath.sh" , "startup.sh", "shutdown.sh");
		processBuilder.directory(binDirectory);
		processBuilder.redirectErrorStream(true);
		int exitValue = -1;
		try {
			Process p = processBuilder.start();
			exitValue = p.waitFor();
		} catch (InterruptedException e) {
			getLog().error(e.getMessage(), e);
			throw new TomcatSetupException(e.getMessage(), e);
		} catch (IOException e) {
			getLog().error(e.getMessage(), e);
			throw new TomcatSetupException(e.getMessage(), e);
		}
		getLog().debug("SetStartScriptPermission return value " + exitValue);
	}
	
	class Runner extends Thread {
		
		Runner(){
			setDaemon(true);
		}
		
		public void run(){
			startTomcat();
		}
		
	}

	protected StepSequence getSetupStepSequence() {
		StepSequence seq = new ForkedSetupSequence();
		seq.add(new CopySetenvScriptStep());
		return seq;
	}

	protected Bootstrap getBootstrap() {
		return new Bootstrap();
	}
}
