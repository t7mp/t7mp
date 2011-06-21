package com.googlecode.t7mp.steps;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;

public class DefaultStepSequenceTest implements StepExecutionListener {

    private AtomicInteger stepsExecuted = new AtomicInteger(0);

    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);

    @Before
    public void setUp(){
        stepsExecuted = new AtomicInteger(0);
    }

    @Test
    public void testDefaultStepSequence(){
        StepSequence sequence = new DefaultStepSequence();
        final int rounds = 10;
        for(int i = 0; i < rounds; i++){
            sequence.add(new ExecutionListenerStep(this));
        }
        Context context = new DefaultContext(mojo);
        sequence.execute(context);
        Assert.assertEquals("should be invoked " + rounds + " times", rounds , stepsExecuted.get());
    }

    @Override
    public void stepExecuted(Step source) {
       stepsExecuted.incrementAndGet();
    }

}
