/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.t7mp.steps.deployment;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;

import com.googlecode.t7mp.AbstractT7BaseMojo;
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
        final AbstractT7BaseMojo mojo = context.getMojo();
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
