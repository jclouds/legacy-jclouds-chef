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
import org.jclouds.chef.ChefApiMetadata;
import org.jclouds.chef.ChefAsyncApi;
import org.jclouds.chef.config.ChefParserModule;
import org.jclouds.ohai.config.JMXOhaiModule;
import org.jclouds.privatechef.config.PrivateChefRestClientModule;
import org.jclouds.rest.internal.BaseRestApiMetadata;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for Private Chef api.
 * 
 * @author Adrian Cole
 */
public class PrivateChefApiMetadata extends BaseRestApiMetadata {

   /** The serialVersionUID */
   private static final long serialVersionUID = 3450830053589179249L;

   @Override
   public Builder toBuilder() {
      return new Builder(getApi(), getAsyncApi()).fromApiMetadata(this);
   }

   public PrivateChefApiMetadata() {
      this(new Builder(PrivateChefApi.class, PrivateChefAsyncApi.class));
   }

   protected PrivateChefApiMetadata(Builder builder) {
      super(Builder.class.cast(builder));
   }

   public static Properties defaultProperties() {
      Properties properties = ChefApiMetadata.defaultProperties();
      return properties;
   }

   public static class Builder extends BaseRestApiMetadata.Builder {

      protected Builder(Class<?> api, Class<?> asyncApi) {
         super(api, asyncApi);
         id("privatechef")
         .name("Private Chef Api")
         .identityName("User")
         .credentialName("Certificate")
         .version(ChefAsyncApi.VERSION)
         .documentation(URI.create("http://help.opscode.com/kb/api"))
         .defaultEndpoint("https://api.opscode.com")
         .defaultProperties(PrivateChefApiMetadata.defaultProperties())
         .context(TypeToken.of(PrivateChefContext.class))
         .defaultModules(ImmutableSet.<Class<? extends Module>>of(PrivateChefRestClientModule.class, ChefParserModule.class, JMXOhaiModule.class));
      }

      @Override
      public PrivateChefApiMetadata build() {
         return new PrivateChefApiMetadata(this);
      }
      
      @Override
      public Builder fromApiMetadata(ApiMetadata in) {
         super.fromApiMetadata(in);
         return this;
      }
   }

}
