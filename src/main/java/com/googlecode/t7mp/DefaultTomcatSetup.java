package com.googlecode.t7mp;

public class DefaultTomcatSetup extends AbstractTomcatSetup {

	public DefaultTomcatSetup(AbstractT7Mojo t7Mojo) {
		super(t7Mojo);
	}

	@Override
	protected void configure() throws TomcatSetupException {
		if(t7Mojo.lookInside){
			log = new LookInsideLog(t7Mojo.getLog());
		}else{
			log = t7Mojo.getLog();
		}
		directorySetup = new TomcatDirectorySetup(t7Mojo.catalinaBase);
		configFilesSetup = new TomcatConfigFilesSetup(t7Mojo.catalinaBase, log, setupUtil);
		artifactDescriptorReader = new DefaultTomcatArtifactDescriptorReader(log);
		MyArtifactResolver myArtifactResolver = new MyArtifactResolver(t7Mojo.resolver, t7Mojo.factory, t7Mojo.local, t7Mojo.remoteRepos);
		libDispatcher = new TomcatArtifactDispatcher(myArtifactResolver, this.t7Mojo.catalinaBase, setupUtil);
	}

}