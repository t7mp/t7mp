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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Resolves artifacts from configuration-section and copy them to a specified directory.
 * 
 */
class TomcatArtifactDispatcher {

    protected MyArtifactResolver myArtifactResolver;

    protected List<AbstractArtifact> resolvedArtifacts = new ArrayList<AbstractArtifact>();

    protected File catalinaBaseDir;

    protected SetupUtil setupUtil;

    protected Log log;

    public TomcatArtifactDispatcher(MyArtifactResolver myArtifactResolver, File catalinaBaseDir, SetupUtil setupUtil,
            Log log) {
        this.myArtifactResolver = myArtifactResolver;
        this.catalinaBaseDir = catalinaBaseDir;
        this.setupUtil = setupUtil;
        this.log = log;
    }

    public TomcatArtifactDispatcher resolveArtifacts(List<? extends AbstractArtifact> artifacts) {
        for (AbstractArtifact abstractArtifact : artifacts) {
            log.debug("Resolve artifact for " + abstractArtifact.toString());
            if (!abstractArtifact.getGroupId().equals("local")) {
                Artifact artifact;
                try {
                    artifact = myArtifactResolver.resolve(abstractArtifact.getGroupId(),
                            abstractArtifact.getArtifactId(), abstractArtifact.getVersion(),
                            abstractArtifact.getType(), Artifact.SCOPE_COMPILE);
                } catch (MojoExecutionException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
                abstractArtifact.setArtifact(artifact);
                resolvedArtifacts.add(abstractArtifact);
            } else {
                Artifact artifact = new DefaultArtifact(abstractArtifact.getGroupId(),
                        abstractArtifact.getArtifactId(),
                        VersionRange.createFromVersion(abstractArtifact.getVersion()), Artifact.SCOPE_COMPILE,
                        abstractArtifact.getType(), null, new DefaultArtifactHandler("jar"), false);
                String resourceName = "/com/googlecode/t7mp/repo/" + abstractArtifact.getArtifactId() + "/"
                        + abstractArtifact.getVersion() + "/" + abstractArtifact.getArtifactId() + "-"
                        + abstractArtifact.getVersion() + ".jar";
                BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream(resourceName));
                File tempFile;
                try {
                    tempFile = File.createTempFile("local_Artifact", ".maven.tmp");
                    tempFile.deleteOnExit();
                    IOUtils.copy(bis, new FileOutputStream(tempFile));
                    artifact.setFile(tempFile);
                    abstractArtifact.setArtifact(artifact);
                    resolvedArtifacts.add(abstractArtifact);
                } catch (FileNotFoundException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                } catch (IOException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
            }
        }
        return this;
    }

    public void copyTo(String directoryName) {
        for (AbstractArtifact artifact : this.resolvedArtifacts) {
            if ("jar".equals(artifact.getType())) {
                try {
                    String targetFileName = createTargetFileName(artifact);
                    File sourceFile = artifact.getArtifact().getFile();
                    File targetFile = new File(catalinaBaseDir, "/" + directoryName + "/" + targetFileName);
                    log.debug("Copy artifact from " + sourceFile.getAbsolutePath() + " to "
                            + targetFile.getAbsolutePath());
                    this.setupUtil.copy(new FileInputStream(sourceFile), new FileOutputStream(targetFile));
                } catch (IOException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
            }
            if ("war".equals(artifact.getType())) {
                final WebappArtifact webappArtifact = (WebappArtifact) artifact;
                if (webappArtifact.isUnpack() || webappArtifact.getTestContextFile() != null) {
                    unzipWebappArtifact(webappArtifact);
                    if (webappArtifact.getTestContextFile() != null) {
                        File metaInfDirectory = new File(createTargetFileName(webappArtifact) + "/META-INF");
                        metaInfDirectory.mkdirs();
                        try {
                            IOUtils.copy(new FileInputStream(webappArtifact.getTestContextFile()), new FileOutputStream(
                                    new File(metaInfDirectory, "context.xml")));
                        } catch (FileNotFoundException e) {
                            throw new TomcatSetupException(e.getMessage(), e);
                        } catch (IOException e) {
                            throw new TomcatSetupException(e.getMessage(), e);
                        }
                    }
                } else {
                    try {
                        String targetFileName = createTargetFileName(artifact);
                        File sourceFile = artifact.getArtifact().getFile();
                        File targetFile = new File(catalinaBaseDir, "/" + directoryName + "/" + targetFileName);
                        log.debug("Copy artifact from " + sourceFile.getAbsolutePath() + " to "
                                + targetFile.getAbsolutePath());
                        this.setupUtil.copy(new FileInputStream(sourceFile), new FileOutputStream(targetFile));
                    } catch (IOException e) {
                        throw new TomcatSetupException(e.getMessage(), e);
                    }
                }
            }
        }
    }

    protected String createTargetFileName(AbstractArtifact abstractArtifact) {
        if (abstractArtifact.getClass().isAssignableFrom(WebappArtifact.class)) {
            return ((WebappArtifact) abstractArtifact).getContextPath() + "." + abstractArtifact.getType();
        }
        return abstractArtifact.getArtifactId() + "-" + abstractArtifact.getVersion() + "."
                + abstractArtifact.getType();
    }

    protected void unzipWebappArtifact(WebappArtifact webappArtifact) {
        WarUnzipper.unzip(webappArtifact.getArtifact().getFile(), new File(createTargetFileName(webappArtifact)));
    }

    void clear() {
        resolvedArtifacts.clear();
    }
}
