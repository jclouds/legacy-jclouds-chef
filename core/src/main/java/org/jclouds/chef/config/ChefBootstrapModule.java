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
package org.jclouds.chef.config;

import static org.jclouds.chef.config.ChefProperties.CHEF_GEM_SYSTEM_VERSION;
import static org.jclouds.chef.config.ChefProperties.CHEF_UPDATE_GEMS;
import static org.jclouds.chef.config.ChefProperties.CHEF_UPDATE_GEM_SYSTEM;
import static org.jclouds.chef.config.ChefProperties.CHEF_VERSION;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.scriptbuilder.domain.StatementList;
import org.jclouds.scriptbuilder.statements.chef.InstallChefGems;
import org.jclouds.scriptbuilder.statements.ruby.InstallRuby;
import org.jclouds.scriptbuilder.statements.ruby.InstallRubyGems;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;

/**
 * Provides bootstrap configuration for nodes.
 * 
 * @author Ignasi Barrera
 */
public class ChefBootstrapModule extends AbstractModule {

   @Provides
   @Named("installChefGems")
   @Singleton
   Statement installChef(BootstrapProperties bootstrapProperties) {

      InstallRubyGems installRubyGems = InstallRubyGems.builder()
            .version(bootstrapProperties.gemSystemVersion().orNull())
            .updateSystem(bootstrapProperties.updateGemSystem(), bootstrapProperties.gemSystemVersion().orNull())
            .updateExistingGems(bootstrapProperties.updateGems()) //
            .build();

      Statement installChef = InstallChefGems.builder().version(bootstrapProperties.chefVersion().orNull()).build();

      return new StatementList(InstallRuby.builder().build(), installRubyGems, installChef);
   }

   @Singleton
   private static class BootstrapProperties {
      @Named(CHEF_VERSION)
      @Inject(optional = true)
      private String chefVersionProperty;

      @Named(CHEF_GEM_SYSTEM_VERSION)
      @Inject(optional = true)
      private String gemSystemVersionProperty;

      @Named(CHEF_UPDATE_GEM_SYSTEM)
      @Inject
      private String updateGemSystemProeprty;

      @Named(CHEF_UPDATE_GEMS)
      @Inject
      private String updateGemsProperty;

      public Optional<String> chefVersion() {
         return Optional.fromNullable(chefVersionProperty);
      }

      public Optional<String> gemSystemVersion() {
         return Optional.fromNullable(gemSystemVersionProperty);
      }

      public boolean updateGemSystem() {
         return Boolean.parseBoolean(updateGemSystemProeprty);
      }

      public boolean updateGems() {
         return Boolean.parseBoolean(updateGemsProperty);
      }
   }

   @Override
   protected void configure() {
   }
}
