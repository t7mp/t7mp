package com.googlecode.t7mp;

import java.util.Timer;

import org.apache.maven.plugin.logging.Log;

/**
 * Scanner that creates a Timer with a TimerTask.
 * 
 * @author jbellmann
 *
 */
public final class Scanner {

    private static final int DEFAULT_DELAY = 10000;
    private static final int MILLIS = 1000;

    private final ScannerConfiguration scannerConfiguration;
    private Timer timer;
    private final Log log;

    public Scanner(ScannerConfiguration scannerConfiguration, Log log) {
        this.scannerConfiguration = scannerConfiguration;
        this.log = log;
    }

    public void start() {
        log.info("Starting Scanner ....");
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new ModifiedFileTimerTask(scannerConfiguration.getRootDirectory(), scannerConfiguration.getWebappDirectory(), scannerConfiguration.getEndingsAsList()), DEFAULT_DELAY, scannerConfiguration.getInterval() * MILLIS);
        log.info("Scanner started");
    }

    public void stop() {
        log.info("Stopping Scanner ...");
        if (this.timer == null) {
            return;
        }
        this.timer.cancel();
        log.info("Scanner stopped");
    }

}
