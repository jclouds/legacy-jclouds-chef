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
package org.jclouds.hostedchef;

import java.util.Set;

import org.jclouds.chef.ChefApi;
import org.jclouds.hostedchef.domain.Group;
import org.jclouds.hostedchef.domain.User;

/**
 * Provides synchronous access to the Hosted Chef Api.
 * 
 * @author Ignasi Barrera
 */
public interface HostedChefApi extends ChefApi {

   /**
    * Check if there exists a node with the given name.
    * 
    * @return <code>true</code> if the specified node name exists.
    */
   @Override
   boolean nodeExists(String name);

   /**
    * Retrieves an existing user.
    * 
    * @param name
    *           The name of the user to get.
    * @return The details of the user or <code>null</code> if not found.
    */
   User getUser(String name);

   /**
    * List all existing groups.
    * 
    * @return The list of groups.
    */
   Set<String> listGroups();

   /**
    * Retrieves an existing group.
    * 
    * @param name
    *           The name of the group to get.
    * @return The details of the group or <code>null</code> if not found.
    */
   Group getGroup(String name);

   /**
    * Creates a new group.
    * 
    * @param name
    *           The name of the group to create.
    */
   void createGroup(String name);

   /**
    * Updates a group.
    * <p>
    * This method can be used to add actors (clients, groups) to the group.
    * 
    * @param group
    *           The group with the updated information.
    */
   void updateGroup(Group group);

   /**
    * Deletes a group.
    * 
    * @param name
    *           The name of the group to delete.
    */
   void deleteGroup(String name);

}
