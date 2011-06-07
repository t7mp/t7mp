package com.googlecode.t7mp.steps.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.artifact.AbstractArtifact;
import com.googlecode.t7mp.artifact.WebappArtifact;
import com.googlecode.t7mp.util.WarUnzipper;

public class WebappsDeploymentStep extends AbstractDeploymentStep {

    @Override
    protected List<AbstractArtifact> getArtifactList() {
        List<AbstractArtifact> artifactList = Lists.newArrayList();
        artifactList.addAll(context.getMojo().getWebapps());
        return artifactList;
    }

    @Override
    protected void deployArtifacts(List<AbstractArtifact> artifactList) {
        for (AbstractArtifact artifact : artifactList) {
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
                    File targetFile = new File(this.context.getMojo().getCatalinaBase(), "/webapps/" + targetFileName);
                    this.context.getLog().debug(
                            "Copy artifact from " + sourceFile.getAbsolutePath() + " to "
                                    + targetFile.getAbsolutePath());
                    this.setupUtil.copy(new FileInputStream(sourceFile), new FileOutputStream(targetFile));
                } catch (IOException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    protected String createTargetFileName(AbstractArtifact abstractArtifact) {
        return ((WebappArtifact) abstractArtifact).getContextPath() + "." + abstractArtifact.getType();
    }

    protected void unzipWebappArtifact(WebappArtifact webappArtifact) {
        WarUnzipper.unzip(webappArtifact.getArtifact().getFile(), new File(createTargetFileName(webappArtifact)));
    }
}
