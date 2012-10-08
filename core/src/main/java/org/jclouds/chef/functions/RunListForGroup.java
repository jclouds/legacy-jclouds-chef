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
package org.jclouds.chef.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.jclouds.chef.config.ChefProperties.CHEF_BOOTSTRAP_DATABAG;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.chef.ChefApi;
import org.jclouds.chef.domain.DatabagItem;
import org.jclouds.json.Json;

import com.google.common.base.Function;
import com.google.inject.TypeLiteral;

/**
 * 
 * Retrieves the run-list for a specific group
 * 
 * 
 * @author Adrian Cole
 */
@Singleton
public class RunListForGroup implements Function<String, List<String>> {
   public static final Type RUN_LIST_TYPE = new TypeLiteral<Map<String, Object>>() {
   }.getType();
   private final ChefApi api;
   private final Json json;
   private final String databag;

   @Inject
   public RunListForGroup(@Named(CHEF_BOOTSTRAP_DATABAG) String databag, ChefApi api, Json json) {
      this.databag = checkNotNull(databag, "databag");
      this.api = checkNotNull(api, "api");
      this.json = checkNotNull(json, "json");
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<String> apply(String from) {
      DatabagItem list = api.getDatabagItem(databag, from);
      checkState(list != null, "databag item %s/%s not found", databag, from);
      return ((Map<String, List<String>>) json.fromJson(list.toString(), RUN_LIST_TYPE)).get("run_list");
   }

}
