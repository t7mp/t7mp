package com.googlecode.t7mp.steps.resources;

import com.googlecode.t7mp.steps.DefaultStepSequence;

public class ConfigFilesSequence extends DefaultStepSequence {

    public ConfigFilesSequence() {
        this.add(new BuildCatalinaPropertiesFileStep());
        this.add(new CopyUserConfigStep());
    }

}
