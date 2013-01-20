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
package org.jclouds.hostedchef.config;

import static org.jclouds.reflect.Reflection2.typeTokenOf;

import java.util.Map;

import org.jclouds.chef.config.BaseChefRestClientModule;
import org.jclouds.hostedchef.HostedChefApi;
import org.jclouds.hostedchef.HostedChefAsyncApi;
import org.jclouds.hostedchef.PatchedChefApi;
import org.jclouds.hostedchef.PatchedChefAsyncApi;
import org.jclouds.rest.ConfiguresRestClient;

import com.google.common.collect.ImmutableMap;

/**
 * Configures the Hosted Chef connection.
 * 
 * @author Ignasi Barrera
 */
@ConfiguresRestClient
public class HostedChefRestClientModule extends BaseChefRestClientModule<HostedChefApi, HostedChefAsyncApi> {
   public static final Map<Class<?>, Class<?>> DELEGATE_MAP = ImmutableMap.<Class<?>, Class<?>> builder()//
         .put(PatchedChefApi.class, PatchedChefAsyncApi.class)//
         .build();

   public HostedChefRestClientModule() {
      super(typeTokenOf(HostedChefApi.class), typeTokenOf(HostedChefAsyncApi.class), DELEGATE_MAP);
   }

}
