package com.googlecode.t7mp;

import java.io.File;

public class TomcatDirectorySetup {
	
	private final File catalinaBaseDir;
	
	public TomcatDirectorySetup(File catalinaBaseDir){
		this.catalinaBaseDir = catalinaBaseDir;
	}
	
	public void createTomcatDirectories() {
		if(!catalinaBaseDir.exists() && !catalinaBaseDir.mkdir()){
			throw new TomcatSetupException("could not create 'catalina.base' on " + catalinaBaseDir.getAbsolutePath());
		}
		createTomcatDirectory("conf");
		createTomcatDirectory("webapps");
		createTomcatDirectory("lib");
		createTomcatDirectory("temp");
		createTomcatDirectory("work");
		createTomcatDirectory("logs");
	}
	
	protected void createTomcatDirectory(String name) {
		File directory = new File(catalinaBaseDir, name);
		if(!directory.exists() && !directory.mkdir()){
			throw new TomcatSetupException("could not create '" + name + "' on " + directory.getAbsolutePath());
		}
	}

}
