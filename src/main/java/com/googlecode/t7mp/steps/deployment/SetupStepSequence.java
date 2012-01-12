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

import com.googlecode.t7mp.AbstractT7BaseMojo;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.DefaultStepSequence;

/**
 * Base sequence that handles optional github repository configuration.
 * 
 * @author jbellmann
 *
 */
public class SetupStepSequence extends DefaultStepSequence {

    @Override
    public void execute(Context context) {
        AbstractT7BaseMojo mojo = context.getMojo();
        if (mojo.isAddGithubRepository()) {
            sequence.add(0, new AddRemoteRepositoryStep());
        }
        super.execute(context);
    }

}
