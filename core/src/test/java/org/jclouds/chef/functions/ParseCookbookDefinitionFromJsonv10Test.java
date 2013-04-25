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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jclouds.chef.ChefApi;
import org.jclouds.chef.config.ChefParserModule;
import org.jclouds.chef.domain.CookbookDefinition;
import org.jclouds.http.HttpResponse;
import org.jclouds.json.config.GsonModule;
import org.jclouds.rest.annotations.ApiVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

@Test(groups = {"unit"}, singleThreaded = true)
public class ParseCookbookDefinitionFromJsonv10Test {

   private ParseCookbookDefinitionFromJsonv10 handler;

   @BeforeTest
   protected void setUpInjector() throws IOException {
      Injector injector = Guice.createInjector(new AbstractModule() {
         @Override
         protected void configure() {
            bind(String.class).annotatedWith(ApiVersion.class).toInstance(ChefApi.VERSION);
         }
      }, new ChefParserModule(), new GsonModule());

      handler = injector.getInstance(ParseCookbookDefinitionFromJsonv10.class);
   }

   public void testCookbokDefinitionParsing() throws URISyntaxException {
      assertEquals(handler.apply(HttpResponse
            .builder()
            .statusCode(200)
            .message("ok")
            .payload(
                  "{" + "\"apache2\" => {" + "\"url\" => \"http://localhost:4000/cookbooks/apache2\","
                        + "\"versions\" => [" + "{\"url\" => \"http://localhost:4000/cookbooks/apache2/5.1.0\","
                        + "\"version\" => \"5.1.0\"},"
                        + "{\"url\" => \"http://localhost:4000/cookbooks/apache2/4.2.0\","
                        + "\"version\" => \"4.2.0\"}" + "]" + "}" + "}").build()),
            new CookbookDefinition(new URI("http://localhost:4000/cookbooks/apache2"),
                  ImmutableSet.of(new CookbookDefinition.Version(new URI("http://localhost:4000/cookbooks/apache2/5.1.0"), "5.1.0"),
                        new CookbookDefinition.Version(new URI("http://localhost:4000/cookbooks/apache2/4.2.0"), "4.2.0"))));
   }
}
