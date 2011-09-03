package com.googlecode.t7mp;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.scanner.Scanner;

public class ForkedTomcatProcessShutdownHook extends Thread implements ShutdownHook{

	private final Process tomcatProcess;
	private final Log log;
	private final List<Scanner> scanners = new ArrayList<Scanner>();
	
	public ForkedTomcatProcessShutdownHook(Process tomcatProcess, Log log){
		this.tomcatProcess = tomcatProcess;
		this.log = log;
	}
	
	@Override
	public void run() {
		log.info("Stopping tomcatProcess ...");
		stopScanners();
		this.tomcatProcess.destroy();
		int returnValue = -1;
		try {
			returnValue = this.tomcatProcess.waitFor();
		} catch (InterruptedException e) {
			log.error("error when waiting for destroying tomcatProcess", e);
		}
		log.info("... tomcatProcess stopped. ReturnValue:" + returnValue);
	}

    @Override
    public void addScanner(Scanner scanner) {
        scanners.add(scanner);
    }
    
    @Override
    public void stopScanners() {
        for (Scanner scanner : scanners) {
            scanner.stop();
        }
    }
}
