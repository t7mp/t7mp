package com.googlecode.t7mp.steps.deployment;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractArtifact;
import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.JarArtifact;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultContext;
import com.googlecode.t7mp.steps.Step;

public class CheckT7ArtifactsStepTest {

    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
    private Log log = Mockito.mock(Log.class);
    private MavenProject mavenProject = Mockito.mock(MavenProject.class);
    private DependencyManagement dependencyManagement = Mockito.mock(DependencyManagement.class);

    private Context context;

    @Before
    public void setUp() {
        Mockito.when(mojo.getLog()).thenReturn(log);
        Mockito.when(mojo.getWebapps()).thenReturn(getWeabapps());
        Mockito.when(mojo.getLibs()).thenReturn(getLibs());
        Mockito.when(mojo.getMavenProject()).thenReturn(mavenProject);
        Mockito.when(mavenProject.getDependencies()).thenReturn(getDependencies());
        Mockito.when(mavenProject.getDependencyManagement()).thenReturn(dependencyManagement);
        Mockito.when(dependencyManagement.getDependencies()).thenReturn(getManagedDependencies());

        context = new DefaultContext(mojo);

    }

    @Test
    public void testCheckT7ArtifactsStep() {
        Step step = new CheckT7ArtifactsStep();
        step.execute(context);
    }

    private List<AbstractArtifact> getLibs() {
        List<AbstractArtifact> result = Lists.newArrayList();
        JarArtifact jar = new JarArtifact();
        jar.setArtifactId("commons-logging");
        jar.setGroupId("commons-logging");
        jar.setType("jar");
        result.add(jar);
        JarArtifact jar1 = new JarArtifact();
        jar1.setGroupId("log4j");
        jar1.setArtifactId("log4j");
        jar1.setType("jar");
        result.add(jar1);
        return result;
    }

    private List<Dependency> getDependencies() {
        List<Dependency> result = Lists.newArrayList();
        Dependency dep = new Dependency();
        dep.setGroupId("commons-logging");
        dep.setArtifactId("commons-logging");
        dep.setVersion("1.1.1");
        result.add(dep);
        return result;
    }

    private List<Dependency> getManagedDependencies() {
        List<Dependency> result = Lists.newArrayList();
        Dependency dep = new Dependency();
        dep.setGroupId("commons-logging");
        dep.setArtifactId("commons-logging");
        dep.setVersion("1.1.0");
        result.add(dep);
        Dependency dep1 = new Dependency();
        dep1.setGroupId("log4j");
        dep1.setArtifactId("log4j");
        dep1.setVersion("1.2.16");
        result.add(dep1);
        return result;
    }

    private List<AbstractArtifact> getWeabapps() {
        return Lists.newArrayList();
    }

}
