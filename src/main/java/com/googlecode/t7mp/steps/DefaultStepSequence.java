package com.googlecode.t7mp.steps;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author jbellmann
 *
 */
public class DefaultStepSequence implements StepSequence {

    protected List<Step> sequence = new LinkedList<Step>();

    @Override
    public void execute(Context context) {
        for (Step step : sequence) {
            step.execute(context);
        }
    }

    @Override
    public void add(Step step) {
        this.sequence.add(step);
    }

}
