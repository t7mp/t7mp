package com.googlecode.t7mp.steps.directory;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;

/**
 * 
 * @author jbellmann
 *
 */
public class CreateTomcatDirectoryStep implements Step {

    private final String directory;

    public CreateTomcatDirectoryStep(String directory) {
        this.directory = directory;
    }

    @Override
    public void execute(Context context) {
        final Log log = context.getLog();
        final File createdDirectory = new File(context.getMojo().getCatalinaBase(), directory);
        boolean created = createdDirectory.mkdirs();
        if (created) {
            log.debug("directory " + createdDirectory.getAbsolutePath() + " created");
        } else {
            log.warn("could not create " + createdDirectory.getAbsolutePath() + ", maybe it exist.");
        }
    }

}
