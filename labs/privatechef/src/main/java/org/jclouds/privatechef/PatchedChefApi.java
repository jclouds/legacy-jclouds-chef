/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.privatechef;

import java.util.concurrent.TimeUnit;

import org.jclouds.chef.ChefApi;
import org.jclouds.concurrent.Timeout;
import org.jclouds.rest.AuthorizationException;

/**
 * Private chef api seems to miss support for HEAD method in the node resource.
 * This class overrides the {@link ChefApi#nodeExists(String)} method to
 * use GET instead of HEAD.
 * 
 * @author Ignasi Barrera
 *
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface PatchedChefApi extends ChefApi
{
    /**
     * 
     * @return true if the specified node name exists.
     * 
     * @throws AuthorizationException
     *            <p/>
     *            "401 Unauthorized" if you are not a recognized user.
     *            <p/>
     *            "403 Forbidden" if you do not have rights to view the node.
     */
    @Override
    boolean nodeExists(String name);
}
