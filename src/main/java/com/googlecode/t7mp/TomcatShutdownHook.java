package com.googlecode.t7mp;

import org.apache.catalina.startup.Bootstrap;

public class TomcatShutdownHook extends Thread {
	
	private Bootstrap bootstrap;
	
	public TomcatShutdownHook(Bootstrap bootstrap){
		this.bootstrap = bootstrap;
	}

	@Override
	public void run() {
		if(bootstrap != null){
			try {
				bootstrap.stop();
				bootstrap = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}