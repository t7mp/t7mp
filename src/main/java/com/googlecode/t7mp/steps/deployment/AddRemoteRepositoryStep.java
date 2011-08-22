package com.googlecode.t7mp.steps.deployment;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;

/**
 * TODO Comment.
 * 
 * @author jbellmann
 *
 */
public class AddRemoteRepositoryStep implements Step {

    private static final String REPO_URL = "https://github.com/t7mp/t7mp.maven.repo/raw/master/releases";

    @Override
    public void execute(Context context) {
        final AbstractT7Mojo mojo = context.getMojo();
        mojo.getRemoteRepos().add(createTomcatRepository());

    }

    private ArtifactRepository createTomcatRepository() {
        ArtifactRepository repository = new DefaultArtifactRepository("t7mp.maven.repo.releases", REPO_URL,
                new DefaultRepositoryLayout(), createSnapshotPolicy(), createRelasesPolicy());
        return repository;
    }

    //snapshots are not enabled
    private ArtifactRepositoryPolicy createSnapshotPolicy() {
        return new ArtifactRepositoryPolicy(false, ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER,
                ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN);
    }

    //releases are enabled
    private ArtifactRepositoryPolicy createRelasesPolicy() {
        return new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_DAILY,
                ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN);
    }

}
