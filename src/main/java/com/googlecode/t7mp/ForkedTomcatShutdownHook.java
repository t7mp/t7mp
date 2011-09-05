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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.util.TomcatUtil;

/**
 * 
 * @author jbellmann
 *
 */
public final class ForkedTomcatShutdownHook extends Thread {

    private static final long SLEEPTIME = 10000;
    private final File binDirectory;
    private final Log log;

    public ForkedTomcatShutdownHook(File binDirectory, Log log) {
        this.binDirectory = binDirectory;
        this.log = log;
    }

    @Override
    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder(TomcatUtil.getStopScriptName(), "stop");
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
            Thread.sleep(SLEEPTIME);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.debug("Exit-Value ForkedTomcatShutdownHook " + exitValue);
    }

}
