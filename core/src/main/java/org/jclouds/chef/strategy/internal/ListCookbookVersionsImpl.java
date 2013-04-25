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
package org.jclouds.chef.strategy.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.util.concurrent.Futures.allAsList;
import static com.google.common.util.concurrent.Futures.getUnchecked;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.config.ChefProperties;
import org.jclouds.chef.domain.CookbookVersion;
import org.jclouds.chef.strategy.ListCookbookVersions;
import org.jclouds.logging.Logger;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;

/**
 * 
 * 
 * @author Adrian Cole
 */
@Singleton
public class ListCookbookVersionsImpl implements ListCookbookVersions {

   protected final ChefApi api;
   protected final ListeningExecutorService userExecutor;
   @Resource
   @Named(ChefProperties.CHEF_LOGGER)
   protected Logger logger = Logger.NULL;

   @Inject
   ListCookbookVersionsImpl(@Named(Constants.PROPERTY_USER_THREADS) ListeningExecutorService userExecutor, ChefApi api) {
      this.userExecutor = checkNotNull(userExecutor, "userExecuor");
      this.api = checkNotNull(api, "api");
   }

   @Override
   public Iterable<? extends CookbookVersion> execute() {
      return execute(userExecutor);
   }

   @Override
   public Iterable<? extends CookbookVersion> execute(Predicate<String> cookbookNameSelector) {
      return execute(userExecutor, cookbookNameSelector);
   }

   @Override
   public Iterable<? extends CookbookVersion> execute(Iterable<String> toGet) {
      return execute(userExecutor, toGet);
   }

   @Override
   public Iterable<? extends CookbookVersion> execute(ListeningExecutorService executor) {
      return execute(executor, api.listCookbooks());
   }

   @Override
   public Iterable<? extends CookbookVersion> execute(ListeningExecutorService executor,
         Predicate<String> cookbookNameSelector) {
      return execute(executor, filter(api.listCookbooks(), cookbookNameSelector));
   }

   @Override
   public Iterable<? extends CookbookVersion> execute(final ListeningExecutorService executor,
         Iterable<String> cookbookNames) {
      return concat(transform(cookbookNames, new Function<String, Iterable<? extends CookbookVersion>>() {

         @Override
         public Iterable<? extends CookbookVersion> apply(final String cookbook) {
            // TODO getting each version could also go parallel
            Set<String> cookbookVersions = api.getVersionsOfCookbook(cookbook);
            ListenableFuture<List<CookbookVersion>> futures = allAsList(transform(cookbookVersions,
                  new Function<String, ListenableFuture<CookbookVersion>>() {
                     @Override
                     public ListenableFuture<CookbookVersion> apply(final String version) {
                        return executor.submit(new Callable<CookbookVersion>() {
                           @Override
                           public CookbookVersion call() throws Exception {
                              return api.getCookbook(cookbook, version);
                           }
                        });
                     }
                  }));

            logger.trace(String.format("getting versions of cookbook: " + cookbook));
            return getUnchecked(futures);
         }
      }));
   }
}
