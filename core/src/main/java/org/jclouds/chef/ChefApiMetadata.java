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

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jclouds.Constants.PROPERTY_SESSION_INTERVAL;
import static org.jclouds.Constants.PROPERTY_TIMEOUTS_PREFIX;
import static org.jclouds.chef.config.ChefProperties.CHEF_BOOTSTRAP_DATABAG;
import static org.jclouds.reflect.Reflection2.typeTokenOf;

import java.net.URI;
import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.chef.config.ChefParserModule;
import org.jclouds.chef.config.ChefRestClientModule;
import org.jclouds.ohai.config.JMXOhaiModule;
import org.jclouds.rest.internal.BaseRestApiMetadata;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for OpsCode's Chef api.
 * 
 * @author Adrian Cole
 */
public class ChefApiMetadata extends BaseRestApiMetadata {

   @Override
   public Builder<?> toBuilder() {
      return new ConcreteBuilder().fromApiMetadata(this);
   }

   public ChefApiMetadata() {
      this(new ConcreteBuilder());
   }

   protected ChefApiMetadata(Builder<?> builder) {
      super(Builder.class.cast(builder));
   }

   public static Properties defaultProperties() {
      Properties properties = BaseRestApiMetadata.defaultProperties();
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "default", SECONDS.toMillis(30) + "");
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "ChefApi.updateCookbook", MINUTES.toMillis(10) + "");
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "ChefApi.createClient", MINUTES.toMillis(2) + "");
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "ChefApi.generateKeyForClient", MINUTES.toMillis(2) + "");
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "ChefApi.createNode", MINUTES.toMillis(2) + "");
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "ChefApi.updateNode", MINUTES.toMillis(10) + "");
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "ChefApi.createRole", MINUTES.toMillis(2) + "");
      properties.setProperty(PROPERTY_TIMEOUTS_PREFIX + "ChefApi.updateRole", MINUTES.toMillis(10) + "");
      properties.setProperty(PROPERTY_SESSION_INTERVAL, "1");
      properties.setProperty(CHEF_BOOTSTRAP_DATABAG, "bootstrap");
      return properties;
   }

   public static abstract class Builder<T extends Builder<T>> extends BaseRestApiMetadata.Builder<T> {
      protected Builder(){
         this(ChefApi.class, ChefAsyncApi.class);
      }

      protected Builder(Class<?> api, Class<?> asyncApi) {
         super(api, asyncApi);
         id("chef")
         .name("OpsCode Chef Api")
         .identityName("User")
         .credentialName("Certificate")
         .version(ChefAsyncApi.VERSION)
         .documentation(URI.create("http://wiki.opscode.com/display/chef/Server+API"))
         .defaultEndpoint("http://localhost:4000")
         .defaultProperties(ChefApiMetadata.defaultProperties())
         .context(typeTokenOf(ChefContext.class))
         .defaultModules(ImmutableSet.<Class<? extends Module>>of(ChefRestClientModule.class, ChefParserModule.class, JMXOhaiModule.class));
      }

      @Override
      public ChefApiMetadata build() {
         return new ChefApiMetadata(this);
      }
   }
   
   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }
}
