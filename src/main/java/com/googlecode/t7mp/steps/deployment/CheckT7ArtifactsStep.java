package com.googlecode.t7mp.steps.deployment;

import java.util.Collection;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractArtifact;
import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;

public class CheckT7ArtifactsStep implements Step {

    private Predicate<AbstractArtifact> noVersionPredicate;

    private AbstractT7Mojo mojo;
    private Log log;
    private Collection<AbstractArtifact> noVersionArtifacts = Lists.newArrayList();

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Context context) {
        mojo = context.getMojo();
        log = mojo.getLog();
        noVersionPredicate = new NoVersionPredicate(log);
        log.debug("Fitler libs");
        noVersionArtifacts.addAll(Collections2.filter(mojo.getWebapps(), noVersionPredicate));
        log.debug("Filter webapps");
        noVersionArtifacts.addAll(Collections2.filter(mojo.getLibs(), noVersionPredicate));
        if (noVersionArtifacts.size() > 0) {
            log.debug("artifacts without version found ");
            List<Dependency> projectDependencies = mojo.getMavenProject().getDependencies();
            List<Dependency> managedDependencies = mojo.getMavenProject().getDependencyManagement().getDependencies();
            if (log.isDebugEnabled()) {
                log.debug("Project-Dependencies : " + projectDependencies.size());
                logDependencies(projectDependencies);
                log.debug("Managed-Dependencies : " + managedDependencies.size());
                logDependencies(managedDependencies);
            }
            Predicate<Dependency> depsfilter = new FindVersionPredicate(noVersionArtifacts, log);
            log.debug("Filter projectArtifacts");
            // first managed dependencies
            Collection<Dependency> depsApplied = Collections2.filter(managedDependencies, depsfilter);
            // project dependencies can overwrite filtering results
            depsApplied.addAll(Collections2.filter(projectDependencies, depsfilter));
            log.debug(depsApplied.size() + " dependenciey applied");
            log.debug("check for noversion-artifacts again ...");
            Collection<AbstractArtifact> noVersionsFound = Collections2.filter(noVersionArtifacts, noVersionPredicate);
            if (noVersionsFound.size() > 0) {
                for (AbstractArtifact artifact : noVersionsFound) {
                    mojo.getLog().error("No version configured for artifact --" + artifact.toString());
                }
                throw new TomcatSetupException("ConfigurationException");
            }
        }
    }

    protected void logDependencies(List<Dependency> dependencies) {
        for (Dependency dependency : dependencies) {
            log.debug("found dependency : " + dependency.toString() + " groupId:" + dependency.getGroupId()
                    + " artifactId:" + dependency.getArtifactId() + " version:" + dependency.getVersion()
                    + " packaging:" + dependency.getType());
        }
    }

}
