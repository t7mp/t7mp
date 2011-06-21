/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.t7mp.scanner;

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
