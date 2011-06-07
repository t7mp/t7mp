package com.googlecode.t7mp.steps.directory;

import com.googlecode.t7mp.steps.DefaultStepSequence;

/**
 * 
 * @author jbellmann
 *
 */
public class CreateTomcatDirectoriesSequence extends DefaultStepSequence {

    public CreateTomcatDirectoriesSequence() {
        this.add(new CreateTomcatDirectoryStep("conf"));
        this.add(new CreateTomcatDirectoryStep("webapps"));
        this.add(new CreateTomcatDirectoryStep("lib"));
        this.add(new CreateTomcatDirectoryStep("temp"));
        this.add(new CreateTomcatDirectoryStep("work"));
        this.add(new CreateTomcatDirectoryStep("logs"));
    }

}
