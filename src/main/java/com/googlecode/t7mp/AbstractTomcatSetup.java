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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * TODO Comment.
 * 
 */
public abstract class AbstractTomcatSetup implements TomcatSetup {

    protected AbstractT7Mojo t7Mojo = null;
    protected SetupUtil setupUtil = new CommonsSetupUtil();

    protected Log log;

    protected TomcatDirectorySetup directorySetup;
    protected TomcatConfigFilesSetup configFilesSetup;
    protected TomcatArtifactDescriptorReader artifactDescriptorReader;
    protected MyArtifactResolver myArtifactResolver;
    protected TomcatArtifactDispatcher libDispatcher;

    public AbstractTomcatSetup(AbstractT7Mojo t7Mojo) {
        this.t7Mojo = t7Mojo;
        this.log = t7Mojo.getLog();
    }

    /**
     * Configure the tomcat.
     * @throws TomcatSetupException
     */
    protected abstract void configure();

    /**
     * Validates that all needed Dependencies are set.
     * 
     * @throws TomcatSetupException
     */
    protected void validateConfiguration() {
        TomcatSetupException.notNull(t7Mojo, "t7Mojo");
        TomcatSetupException.notNull(log, "log");
        TomcatSetupException.notNull(setupUtil, "setupUtil");
        TomcatSetupException.notNull(directorySetup, "directorySetup");
        TomcatSetupException.notNull(configFilesSetup, "configFilesSetup");
        TomcatSetupException.notNull(artifactDescriptorReader, "artifactDescriptorReader");
        TomcatSetupException.notNull(libDispatcher, "libDispatcher");
    }

    /**
     * 
     * @throws MojoExecutionException
     */
    @Override
    public void buildTomcat() throws MojoExecutionException {
        try {
            configure();
            validateConfiguration();
            directorySetup.createTomcatDirectories();
            configFilesSetup.copyDefaultConfig();
            buildCatalinaPropertiesFile();
            configFilesSetup.copyUserConfigs(this.t7Mojo.tomcatConfigDirectory);
            List<JarArtifact> tomcatLibs = artifactDescriptorReader.getJarArtifacts(this.t7Mojo.tomcatVersion);
            libDispatcher.resolveArtifacts(tomcatLibs).copyTo("lib");
            libDispatcher.clear();
            libDispatcher.resolveArtifacts(this.t7Mojo.libs).copyTo("lib");
            libDispatcher.clear();
            libDispatcher.resolveArtifacts(this.t7Mojo.webapps).copyTo("webapps");
            copyWebapp();
            setSystemProperties();
            copyOverwriteWebXML();
        } catch (TomcatSetupException e) {
            this.t7Mojo.getLog().error("Error setting up tomcat.");
            this.t7Mojo.getLog().error(e.getMessage(), e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    /**
     * @throws TomcatSetupException
     */
    //TODO think about merging with userconfig
    protected void buildCatalinaPropertiesFile() {
        try {
            Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, new LogNothingLogChute());
            Velocity.setProperty(Velocity.RESOURCE_LOADER, "class");
            Velocity.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
            Velocity.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
            Velocity.init();
            Template template = Velocity.getTemplate("com/googlecode/t7mp/conf/catalina.properties");
            VelocityContext context = new VelocityContext();
            context.put("tomcatHttpPort", this.t7Mojo.getTomcatHttpPort() + "");
            context.put("tomcatShutdownPort", this.t7Mojo.getTomcatShutdownPort() + "");
            context.put("tomcatShutdownCommand", this.t7Mojo.getTomcatShutdownCommand());
            Writer writer = new FileWriter(new File(this.t7Mojo.catalinaBase, "/conf/catalina.properties"));
            template.merge(context, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

    /**
     * @throws TomcatSetupException
     */
    protected void copyWebapp() {
        if (!this.t7Mojo.isWebProject()) {
            return;
        }
        if ((this.t7Mojo.webappOutputDirectory == null) || (!this.t7Mojo.webappOutputDirectory.exists())) {
            return;
        }
        try {
            setupUtil.copyDirectory(this.t7Mojo.webappOutputDirectory, new File(this.t7Mojo.catalinaBase, "/webapps/"
                    + this.t7Mojo.webappOutputDirectory.getName()));
            if (this.t7Mojo.testContextFile != null) {
                if (this.t7Mojo.testContextFile.exists()) {
                    final File metaInfDirectory = new File(this.t7Mojo.catalinaBase, "/webapps/"
                            + this.t7Mojo.webappOutputDirectory.getName() + "/META-INF");
                    metaInfDirectory.mkdirs();
                    setupUtil.copyFile(this.t7Mojo.testContextFile, new File(metaInfDirectory, "context.xml"));
                }
            }
        } catch (IOException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

    protected void copyOverwriteWebXML() {
        if ((this.t7Mojo.overwriteWebXML == null) || (!this.t7Mojo.overwriteWebXML.exists())) {
            return;
        }
        try {
            setupUtil.copyFile(this.t7Mojo.overwriteWebXML, new File(this.t7Mojo.catalinaBase, "/webapps/"
                    + this.t7Mojo.webappOutputDirectory.getName() + "/WEB-INF/web.xml"));
        } catch (IOException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

    protected void setSystemProperties() {
        System.setProperty("catalina.home", this.t7Mojo.catalinaBase.getAbsolutePath());
        log.debug("set systemproperty key: catalina.home to value " + this.t7Mojo.catalinaBase.getAbsolutePath());
        System.setProperty("catalina.base", this.t7Mojo.catalinaBase.getAbsolutePath());
        log.debug("set systemproperty key: catalina.base to value " + this.t7Mojo.catalinaBase.getAbsolutePath());
        for (SystemProperty property : this.t7Mojo.systemProperties) {
            String value = replaceCatalinas(property.getValue());
            System.setProperty(property.getKey(), value);
            log.debug("set systemproperty key: " + property.getKey() + " to value: "
                    + System.getProperty(property.getKey()));
        }
    }

    protected String replaceCatalinas(String value) {
        if (value.startsWith("${catalina.home}")) {
            value = value.replace("${catalina.home}", System.getProperty("catalina.home"));
        }
        if (value.startsWith("${catalina.base}")) {
            value = value.replace("${catalina.base}", System.getProperty("catalina.base"));
        }
        return value;
    }

    /**
     * TODO Comment.
     * @author jbellmann
     *
     */
    static class LogNothingLogChute implements LogChute {

        @Override
        public void init(RuntimeServices rs) throws Exception {

        }

        @Override
        public void log(int level, String message) {

        }

        @Override
        public void log(int level, String message, Throwable t) {

        }

        @Override
        public boolean isLevelEnabled(int level) {
            return false;
        }

    }

}
