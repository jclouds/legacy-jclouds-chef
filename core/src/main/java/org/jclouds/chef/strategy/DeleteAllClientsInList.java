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
package org.jclouds.chef.strategy;

import org.jclouds.chef.strategy.internal.DeleteAllClientsInListImpl;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.ImplementedBy;

/**
 * Deletes all clients in a given list.
 * 
 * @author Adrian Cole
 * @author Ignasi Barrera
 */
@ImplementedBy(DeleteAllClientsInListImpl.class)
public interface DeleteAllClientsInList {

   public void execute(Iterable<String> names);

   public void execute(ListeningExecutorService executor, Iterable<String> names);

}
