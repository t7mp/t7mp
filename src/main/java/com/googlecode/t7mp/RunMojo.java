/**
 * Copyright (C) 2010 Joerg Bellmann <joerg.bellmann@googlemail.com>
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

import java.io.File;

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * @goal run
 * @requiresDependencyResolution runtime
 * 
 *
 */
public class RunMojo extends AbstractT7Mojo {

    protected Bootstrap bootstrap;

    protected TomcatSetup tomcatSetup;

    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        PreConditions.checkConfiguredTomcatVersion(getLog(), tomcatVersion);

        this.tomcatSetup = getTomcatSetup();
        this.tomcatSetup.buildTomcat();

        bootstrap = getBootstrap();
        getLog().info("Starting Tomcat ...");
        try {
            bootstrap.init();
            final TomcatShutdownHook shutdownHook = new TomcatShutdownHook(bootstrap);
            for (ScannerConfiguration scannerConfiguration : getScanners()) {
                scannerConfiguration.setRootDirectory(webappSourceDirectory);
                scannerConfiguration.setWebappDirectory(new File(catalinaBase, "webapps/" + buildFinalName));
                Scanner scanner = new Scanner(scannerConfiguration, getLog());
                scanner.start();
                shutdownHook.addScanner(scanner);
            }
            if (scanClasses) {
                ScannerConfiguration scannerConfiguration = new ScannerConfiguration();
                scannerConfiguration.setRootDirectory(webappClassDirectory);
                scannerConfiguration.setWebappDirectory(new File(catalinaBase, "webapps/" + buildFinalName + "/WEB-INF/classes"));
                scannerConfiguration.setEndings("%"); // it's all or nothing
                Scanner scanner = new Scanner(scannerConfiguration, getLog());
                scanner.start();
                shutdownHook.addScanner(scanner);
            }
            if (tomcatSetAwait) {
                Runtime.getRuntime().addShutdownHook(shutdownHook);
                bootstrap.setAwait(tomcatSetAwait);
                bootstrap.start();
            } else {
                bootstrap.start();
                getPluginContext().put(T7_BOOTSTRAP_CONTEXT_ID, bootstrap);
                Runtime.getRuntime().addShutdownHook(shutdownHook);
                getLog().info("Tomcat started");
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    protected TomcatSetup getTomcatSetup() {
        return new DefaultTomcatSetup((AbstractT7Mojo) this);
    }

    protected Bootstrap getBootstrap() {
        return new Bootstrap();
    }

}
