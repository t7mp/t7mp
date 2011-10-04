package com.googlecode.t7mp.steps.deployment;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultStepSequence;

/**
 * 
 * @author jbellmann
 *
 */
public class SetupStepSequence extends DefaultStepSequence {

    @Override
    public void execute(Context context) {
        AbstractT7Mojo mojo = context.getMojo();
        if (mojo.isAddGithubRepository()) {
            sequence.add(0, new AddRemoteRepositoryStep());
        }
        super.execute(context);
    }

}
