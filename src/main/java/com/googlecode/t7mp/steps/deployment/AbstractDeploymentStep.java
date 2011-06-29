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
package com.googlecode.t7mp.steps.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoExecutionException;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractArtifact;
import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.CommonsSetupUtil;

public abstract class AbstractDeploymentStep implements Step {

    protected SetupUtil setupUtil = new CommonsSetupUtil();

    protected MyArtifactResolver myArtifactResolver;

    protected Context context;
    
    @Override
    public void execute(Context context) {
      this.context = context;
      this.myArtifactResolver = new MyArtifactResolver(context.getMojo());

      List<AbstractArtifact> artifactList = getArtifactList();
      artifactList = resolveArtifacts(artifactList);
      deployArtifacts(artifactList);
    }
    
    protected abstract List<AbstractArtifact> getArtifactList();
    
    protected String createTargetFileName(AbstractArtifact abstractArtifact) {
        return abstractArtifact.getArtifactId() + "-" + abstractArtifact.getVersion() + "."
                + abstractArtifact.getType();
    }
    
    protected void deployArtifacts(List<AbstractArtifact> artifactList) {
        for (AbstractArtifact artifact : artifactList) {
                try {
                    String targetFileName = createTargetFileName(artifact);
                    File sourceFile = artifact.getArtifact().getFile();
                    File targetFile = new File(context.getMojo().getCatalinaBase(), "/lib/" + targetFileName);
                    context.getLog().debug("Copy artifact from " + sourceFile.getAbsolutePath() + " to "
                            + targetFile.getAbsolutePath());
                    this.setupUtil.copy(new FileInputStream(sourceFile), new FileOutputStream(targetFile));
                } catch (IOException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
        }
    }
    
    protected List<AbstractArtifact> resolveArtifacts(List<? extends AbstractArtifact> artifacts) {
        List<AbstractArtifact> resolvedArtifacts = Lists.newArrayList();
        for (AbstractArtifact abstractArtifact : artifacts) {
            context.getLog().debug("Resolve artifact for " + abstractArtifact.toString());
            if (!abstractArtifact.getGroupId().equals("local")) {
                Artifact artifact;
                try {
                    artifact = myArtifactResolver.resolve(abstractArtifact.getGroupId(),
                            abstractArtifact.getArtifactId(), abstractArtifact.getVersion(),
                            abstractArtifact.getClassifier(),
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
        return resolvedArtifacts;
    }

}
