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
package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.CommonsSetupUtil;
import com.googlecode.t7mp.util.FilesOnlyFileFilter;

/**
 * 
 * @author jbellmann
 *
 */
public class CopyUserConfigStep implements Step {
    
    private File catalinaBaseDir;
    private File userConfigDir;
    private Log log;
    
    private SetupUtil setupUtil = new CommonsSetupUtil();

    @Override
    public void execute(Context context) {
        // setup fields
        this.catalinaBaseDir = context.getMojo().getCatalinaBase();
        this.userConfigDir = context.getMojo().getTomcatConfigDirectory();
        this.log = context.getLog();
        
        //
        if (userConfigDir == null) {
            log.info("No directory for userConfigFiles configured.");
            return;
        }
        if (!userConfigDir.exists() || !userConfigDir.isDirectory()) {
            log.warn("The configured Directory for configuration files does not exist. "
                    + userConfigDir.getAbsolutePath());
            log.warn("Ignoring user configuration.");
        }
        if (userConfigDir.exists() && userConfigDir.isDirectory()) {
            File[] files = userConfigDir.listFiles(new FilesOnlyFileFilter());
            for (File configFile : files) {
                try {
                    if (configFile.getName().equals("catalina.properties")) {
                        mergeUserCatalinaPropertiesWithDefault(configFile);
                        continue;
                    }
                    log.debug("Copy provided config file '" + configFile.getName() + "' to "
                            + catalinaBaseDir.getAbsolutePath() + "/conf/" + configFile.getName());
                    this.setupUtil.copy(new FileInputStream(configFile), new FileOutputStream(new File(catalinaBaseDir,
                            "/conf/" + configFile.getName())));
                } catch (FileNotFoundException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                } catch (IOException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
            }
        }
    }

    private void mergeUserCatalinaPropertiesWithDefault(File userCatlinaPropertiesFile) throws IOException {
        File defaultConfigFile = new File(catalinaBaseDir, "/conf/catalina.properties");

        Properties userCatalinaProperties = loadPropertiesFromFile(userCatlinaPropertiesFile);
        Properties defaultCatalinProperties = loadPropertiesFromFile(defaultConfigFile);

        mergeProperties(defaultCatalinProperties, userCatalinaProperties, getExcludes());

        writePropertiesToFile(defaultCatalinProperties, defaultConfigFile);
    }

    private Properties loadPropertiesFromFile(File propertiesFile) throws IOException {
        Properties properties = new Properties();
        InputStream in = new FileInputStream(propertiesFile);
        try {
            properties.load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }

        return properties;
    }

    private Collection<String> getExcludes() {
        Collection<String> excludes = new ArrayList<String>();
        excludes.add("http.port");
        excludes.add("shutdown.port");
        excludes.add("shutdown.command");
        return excludes;
    }

    private void mergeProperties(Properties target, Properties source, Collection<String> excludes) {
        for (Object key : source.keySet()) {
            if (!excludes.contains(key)) {
                target.setProperty((String) key, (String) source.get(key));
            } else {
                log.debug("Skip property " + key + " from user catalina.properties");
            }
        }
    }

    private void writePropertiesToFile(Properties properties, File target) throws IOException {
        OutputStream defaultOut = null;
        try {
            defaultOut = new FileOutputStream(target);
            properties.store(defaultOut, "");
        } finally {
            IOUtils.closeQuietly(defaultOut);
        }
    }
    
}
