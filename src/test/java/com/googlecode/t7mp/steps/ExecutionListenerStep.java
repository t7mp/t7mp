package com.googlecode.t7mp.steps;

public class ExecutionListenerStep implements Step {

    private final StepExecutionListener listener;
    
    public ExecutionListenerStep(StepExecutionListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void execute(Context context) {
        this.listener.stepExecuted(this);
    }

}
