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
package org.jclouds.chef;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jclouds.chef.domain.Client;
import org.jclouds.chef.domain.CookbookVersion;
import org.jclouds.chef.domain.Node;
import org.jclouds.chef.internal.BaseChefService;
import org.jclouds.scriptbuilder.domain.Statement;

import com.google.common.base.Predicate;
import com.google.common.io.InputSupplier;
import com.google.inject.ImplementedBy;

/**
 * Provides high level chef operations
 * 
 * @author Adrian Cole
 */
@ImplementedBy(BaseChefService.class)
public interface ChefService {
   /**
    * @return a reference to the context that created this.
    */
   ChefContext getContext();

   byte[] encrypt(InputSupplier<? extends InputStream> supplier) throws IOException;

   byte[] decrypt(InputSupplier<? extends InputStream> supplier) throws IOException;

   void cleanupStaleNodesAndClients(String prefix, int secondsStale);

   /**
    * 
    * @param nodeName
    * @param runList
    * @return node sent to the server containing the automatic attributes
    */
   Node createNodeAndPopulateAutomaticAttributes(String nodeName, Iterable<String> runList);

   /**
    * Creates all steps necessary to bootstrap and run the chef api.
    * 
    * @param group
    *           corresponds to a configured
    *           {@link org.jclouds.chef.config.ChefProperties#CHEF_BOOTSTRAP_DATABAG
    *           databag} where run_list and other information are stored
    * @return boot script
    * @see #updateRunListForTag
    */
   Statement createBootstrapScriptForGroup(String group);

   /**
    * assigns a run list to all nodes bootstrapped with a certain group
    * 
    * @param runList
    *           list of recipes or roles to assign. syntax is
    *           {@code recipe[name]} and {@code role[name]}
    * 
    * @param group
    *           corresponds to a configured
    *           {@link org.jclouds.chef.config.ChefProperties#CHEF_BOOTSTRAP_DATABAG
    *           databag} where run_list and other information are stored
    * @see #makeChefApiBootstrapScriptForTag
    */
   void updateRunListForGroup(Iterable<String> runList, String group);

   /**
    * @param group
    *           corresponds to a configured
    *           {@link org.jclouds.chef.config.ChefProperties#CHEF_BOOTSTRAP_DATABAG
    *           databag} where run_list and other information are stored
    * @return run list for all nodes bootstrapped with a certain group
    * @see #updateRunListForTag
    */
   List<String> getRunListForGroup(String group);

   void deleteAllNodesInList(Iterable<String> names);

   Iterable<? extends Node> listNodes();

   Iterable<? extends Node> listNodesMatching(Predicate<String> nodeNameSelector);

   Iterable<? extends Node> listNodesNamed(Iterable<String> names);

   void deleteAllClientsInList(Iterable<String> names);

   Iterable<? extends Client> listClientsDetails();

   Iterable<? extends Client> listClientsDetailsMatching(Predicate<String> clientNameSelector);

   Iterable<? extends Client> listClientsNamed(Iterable<String> names);

   Iterable<? extends CookbookVersion> listCookbookVersions();

   Iterable<? extends CookbookVersion> listCookbookVersionsMatching(Predicate<String> cookbookNameSelector);

   Iterable<? extends CookbookVersion> listCookbookVersionsNamed(Iterable<String> cookbookNames);

   void updateAutomaticAttributesOnNode(String nodeName);
}
