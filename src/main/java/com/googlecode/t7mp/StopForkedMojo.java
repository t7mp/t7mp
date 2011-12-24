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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.googlecode.t7mp.util.SystemUtil;

/**
 * 
 * @goal stop-forked
 * @requiresDependencyResolution runtime
 * 
 *
 */
public class StopForkedMojo extends AbstractT7Mojo {

    private static final long SLEEPTIME = 10000;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ProcessBuilder processBuilder = new ProcessBuilder(getStopSkriptCommand());
        int exitValue = -1;
        try {
            File binDirectory = new File(getCatalinaBase(), "/bin/");
            Process p = processBuilder.directory(binDirectory).start();
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                getLog().info(line);
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
            getLog().error(e.getMessage(), e);
        }
        getLog().debug("Exit-Value ForkedTomcatShutdownHook " + exitValue);
    }

    protected String[] getStopSkriptCommand() {
        if (SystemUtil.isWindowsSystem()) {
            return new String[] { "cmd", "/c", "catalina.bat", "stop" };
        } else {
            return new String[] { "./catalina.sh", "stop" };
        }
    }

}
