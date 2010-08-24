package com.googlecode.t7mp;

import org.apache.maven.plugin.logging.Log;

/**
 * 
 *
 */
public final class GlobalTomcatShutdownHook extends Thread {
	
	private final Log log;
	
	public GlobalTomcatShutdownHook(Log log){
		this.log = log;
	}
	
	@Override
	public void run() {
		try {
			if(GlobalTomcatHolder.bootstrap != null){
				log.warn("GlobalTomcatShutdownHook");
				log.warn("Shutting down global tomcat instance. ");
				GlobalTomcatHolder.bootstrap.stop();
				GlobalTomcatHolder.bootstrap = null;
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

}
