package com.googlecode.t7mp.steps.deployment;

import java.util.List;

import junit.framework.Assert;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;

public class AddRemoteRepositoryStepTest {
    
    @Test
    public void testAddRemoteRepository(){
        AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
        List<ArtifactRepository> remoteRepos = Lists.newArrayList();
        Mockito.when(mojo.getRemoteRepos()).thenReturn(remoteRepos);
        Context context = new DefaultContext(mojo);
        AddRemoteRepositoryStep step = new AddRemoteRepositoryStep();
        step.execute(context);
        Assert.assertTrue("There should be an repository in the list.",remoteRepos.size() == 1);
        ArtifactRepository repository = remoteRepos.get(0);
        Assert.assertEquals("Id should be 't7mp.maven.repo.releases'","t7mp.maven.repo.releases", repository.getId());
    }

}
