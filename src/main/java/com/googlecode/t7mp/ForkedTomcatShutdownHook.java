package com.googlecode.t7mp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.util.TomcatUtil;

public final class ForkedTomcatShutdownHook extends Thread {

	private final File binDirectory;
	private final Log log;

	public ForkedTomcatShutdownHook(File binDirectory, Log log) {
		this.binDirectory = binDirectory;
		this.log = log;
	}

	@Override
	public void run() {
		ProcessBuilder processBuilder = new ProcessBuilder(
				TomcatUtil.getStopScriptName(), "stop");
		int exitValue = -1;
		try {
			Process p = processBuilder.directory(this.binDirectory).start();
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				log.info(line);
			}
			exitValue = p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
		log.debug("Exit-Value ForkedTomcatShutdownHook " + exitValue);
	}

}
