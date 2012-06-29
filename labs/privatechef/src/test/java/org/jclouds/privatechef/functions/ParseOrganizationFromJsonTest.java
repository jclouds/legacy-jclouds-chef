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
package org.jclouds.privatechef.functions;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.jclouds.chef.ChefAsyncClient;
import org.jclouds.chef.config.ChefParserModule;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.io.Payloads;
import org.jclouds.json.config.GsonModule;
import org.jclouds.privatechef.domain.Organization;
import org.jclouds.rest.annotations.ApiVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * Tests behavior of {@code ParseOrganizationFromJson}
 * 
 * @author Adrian Cole
 */
@Test(groups = { "unit" }, sequential = true)
public class ParseOrganizationFromJsonTest {

   private ParseJson<Organization> handler;

   @BeforeTest
   protected void setUpInjector() throws IOException {
      Injector injector = Guice.createInjector(new AbstractModule() {
          @Override
          protected void configure()
          {
              bind(String.class).annotatedWith(ApiVersion.class).toInstance(ChefAsyncClient.VERSION);
          }
       }, new ChefParserModule(), new GsonModule());
      handler = injector.getInstance(Key.get(new TypeLiteral<ParseJson<Organization>>() {
      }));
   }

   public void test() {

      Organization org = new Organization("486ca3ac66264fea926aa0b4ff74341c", "jclouds", "jclouds",
            "jclouds-validator", "Business", null);

      String toParse = "{\"guid\":\"486ca3ac66264fea926aa0b4ff74341c\",\"name\":\"jclouds\",\"full_name\":\"jclouds\",\"clientname\":\"jclouds-validator\",\"org_type\":\"Business\",\"name\":\"jclouds\"}";
      assertEquals(handler.apply(new HttpResponse(200, "ok", Payloads.newStringPayload(toParse))), org);
   }
}
