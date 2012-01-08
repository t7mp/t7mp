package com.googlecode.t7mp.steps.deployment;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.sonatype.aether.util.StringUtils;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.ZipUtil;

/**
 * Comment.
 * 
 * @author jbellmann
 *
 */
public class ResolveTomeeStep implements Step {

    public static final String TOMEE_GROUPID = "org.apache.openejb";
    public static final String TOMEE_ARTIFACTID = "apache-tomee";
    public static final String TOMEE_CLASSIFIER = "webprofile";
    public static final String TOMEE_TYPE = "zip";

    protected AbstractT7Mojo mojo;
    protected MyArtifactResolver myArtifactResolver;
    protected Log logger;

    @Override
    public void execute(Context context) {
        this.mojo = context.getMojo();
        this.myArtifactResolver = new MyArtifactResolver(context.getMojo());
        this.logger = context.getMojo().getLog();
        String version = null;
        String configuredVersion = context.getMojo().getTomcatVersion();
        if (context.getMojo().isDownloadTomcatExamples()) {
            logger.info("Resolve Tomcat with 'docs' and 'examples'");
            version = configuredVersion;
        } else {
            logger.info("Resolve Tomcat without 'docs' and 'examples'.");
            version = configuredVersion + ".A";
        }
        if (StringUtils.isEmpty(version)) {
            throw new TomcatSetupException("Version should not be null or empty.");
        }

        File unpackDirectory = null;
        try {
            Artifact artifact = resolveTomcatArtifact(version);
            unpackDirectory = getUnpackDirectory();
            ZipUtil.unzip(artifact.getFile(), unpackDirectory);
            copyToTomcatDirectory(unpackDirectory);
        } catch (MojoExecutionException e) {
            logger.error(e.getMessage(), e);
            throw new TomcatSetupException(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new TomcatSetupException(e.getMessage(), e);
        } finally {
            if (unpackDirectory != null) {
                try {
                    FileUtils.deleteDirectory(unpackDirectory);
                } catch (IOException e) {
                    logger.error("Could not delete tomcat upack directory : " + unpackDirectory.getAbsolutePath());
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void copyToTomcatDirectory(File unpackDirectory) throws IOException {
        File[] files = unpackDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        // should only be one
        FileUtils.copyDirectory(files[0], this.mojo.getCatalinaBase());
    }

    protected Artifact resolveTomcatArtifact(String tomcatVersion) throws MojoExecutionException {
        Artifact artifact = myArtifactResolver.resolve(TOMEE_GROUPID, TOMEE_ARTIFACTID, tomcatVersion,
                TOMEE_CLASSIFIER, TOMEE_TYPE, Artifact.SCOPE_COMPILE);
        return artifact;
    }

    protected File getUnpackDirectory() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File upackDirectory = new File(tempDir, UUID.randomUUID().toString());
        upackDirectory.mkdirs();
        return upackDirectory;
    }
}
