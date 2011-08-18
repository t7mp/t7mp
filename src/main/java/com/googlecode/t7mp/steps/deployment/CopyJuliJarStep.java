package com.googlecode.t7mp.steps.deployment;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.TomcatUtil;

public class CopyJuliJarStep implements Step {
	
	private static final String JAR_NAME = "tomcat-juli.jar";

	@Override
	public void execute(Context context) {
		try {
			AbstractT7Mojo mojo = context.getMojo();
			File juliJarFileSource = new File(TomcatUtil.getBinDirectory(mojo.getCatalinaBase()), JAR_NAME);
			File juliJarFileDestination = new File(TomcatUtil.getLibDirectory(mojo.getCatalinaBase()), JAR_NAME);
			FileUtils.copyFile(juliJarFileSource, juliJarFileDestination);
		} catch (IOException e) {
			throw new TomcatSetupException(e.getMessage(), e);
		}

	}

}
