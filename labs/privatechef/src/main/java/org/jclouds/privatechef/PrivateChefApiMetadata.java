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

import java.net.URI;
import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.ChefApiMetadata;
import org.jclouds.chef.ChefContext;
import org.jclouds.chef.config.ChefBootstrapModule;
import org.jclouds.chef.config.ChefParserModule;
import org.jclouds.ohai.config.JMXOhaiModule;
import org.jclouds.privatechef.config.PrivateChefHttpApiModule;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for Private Chef api.
 * 
 * @author Adrian Cole
 */
public class PrivateChefApiMetadata extends BaseHttpApiMetadata<PrivateChefApi> {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public PrivateChefApiMetadata() {
      this(new Builder());
   }

   protected PrivateChefApiMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      return ChefApiMetadata.defaultProperties();
   }

   public static class Builder extends BaseHttpApiMetadata.Builder<PrivateChefApi, Builder> {

      protected Builder() {
         id("privatechef")
               .name("Private Chef Api")
               .identityName("User")
               .credentialName("Certificate")
               .version(ChefApi.VERSION)
               .documentation(URI.create("http://www.opscode.com/support/"))
               .defaultEndpoint("https://api.opscode.com")
               .defaultProperties(PrivateChefApiMetadata.defaultProperties())
               .view(ChefContext.class)
               .defaultModules(
                     ImmutableSet.<Class<? extends Module>> of(PrivateChefHttpApiModule.class, ChefParserModule.class,
                           ChefBootstrapModule.class, JMXOhaiModule.class));
      }

      @Override
      public PrivateChefApiMetadata build() {
         return new PrivateChefApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }
   }
}
