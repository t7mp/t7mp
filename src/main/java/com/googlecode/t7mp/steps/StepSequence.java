package com.googlecode.t7mp.steps;

/**
 * 
 * @author jbellmann
 *
 */
public interface StepSequence extends Step {

    void add(Step step);

}
