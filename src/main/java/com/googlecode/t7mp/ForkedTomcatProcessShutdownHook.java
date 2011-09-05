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
package com.googlecode.t7mp;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.scanner.Scanner;

/**
 * 
 * @author jbellmann
 *
 */
public class ForkedTomcatProcessShutdownHook extends Thread implements ShutdownHook {

    private final Process tomcatProcess;
    private final Log log;
    private final List<Scanner> scanners = new ArrayList<Scanner>();

    public ForkedTomcatProcessShutdownHook(Process tomcatProcess, Log log) {
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
