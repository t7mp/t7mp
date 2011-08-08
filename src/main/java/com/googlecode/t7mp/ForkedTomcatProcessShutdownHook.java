package com.googlecode.t7mp;

import org.apache.maven.plugin.logging.Log;

public class ForkedTomcatProcessShutdownHook extends Thread {

	private final Process tomcatProcess;
	private final Log log;
	
	public ForkedTomcatProcessShutdownHook(Process tomcatProcess, Log log){
		this.tomcatProcess = tomcatProcess;
		this.log = log;
	}
	
	@Override
	public void run() {
		log.error("Stopping tomcatProcess ...");
		this.tomcatProcess.destroy();
		int returnValue = -1;
		try {
			returnValue = this.tomcatProcess.waitFor();
		} catch (InterruptedException e) {
			log.error("error when waiting for destroying tomcatProcess", e);
		}
		log.error("... tomcatProcess stopped. ReturnValue:" + returnValue);
	}
}
