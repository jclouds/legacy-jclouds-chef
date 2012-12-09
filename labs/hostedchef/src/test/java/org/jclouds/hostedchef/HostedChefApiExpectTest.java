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
package org.jclouds.hostedchef;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.jclouds.chef.BaseChefApiExectTest;
import org.jclouds.chef.ChefAsyncApi;
import org.jclouds.date.TimeStamp;
import org.jclouds.hostedchef.config.HostedChefRestClientModule;
import org.jclouds.hostedchef.domain.Group;
import org.jclouds.hostedchef.domain.User;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.rest.ConfiguresRestClient;
import org.jclouds.rest.ResourceNotFoundException;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Expect tests for the {@link HostedChefApi} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit", testName = "HostedChefApiExpectTest")
public class HostedChefApiExpectTest extends BaseChefApiExectTest<HostedChefApi> {
   public HostedChefApiExpectTest() {
      provider = "hostedchef";
   }

   public void testGetUserReturns2xx() {
      HostedChefApi api = requestSendsResponse(
            signed(HttpRequest.builder() //
                  .method("GET") //
                  .endpoint("https://api.opscode.com/users/nacx") //
                  .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
                  .addHeader("Accept", MediaType.APPLICATION_JSON).build()), //
            HttpResponse.builder().statusCode(200)
                  .payload(payloadFromResourceWithContentType("/user.json", MediaType.APPLICATION_JSON)) //
                  .build());

      User user = api.getUser("nacx");
      assertEquals(user.getUsername(), "nacx");
      assertEquals(user.getDisplayName(), "Ignasi Barrera");
   }

   public void testGetUserReturns404() {
      HostedChefApi api = requestSendsResponse(signed(HttpRequest.builder() //
            .method("GET") //
            .endpoint("https://api.opscode.com/users/foo") //
            .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
            .addHeader("Accept", MediaType.APPLICATION_JSON) //
            .build()), //
            HttpResponse.builder().statusCode(404).build());

      assertNull(api.getUser("foo"));
   }

   public void testListGroups() {
      HostedChefApi api = requestSendsResponse(
            signed(HttpRequest.builder() //
                  .method("GET") //
                  .endpoint("https://api.opscode.com/groups") //
                  .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
                  .addHeader("Accept", MediaType.APPLICATION_JSON).build()), //
            HttpResponse.builder().statusCode(200)
                  .payload(payloadFromResourceWithContentType("/groups.json", MediaType.APPLICATION_JSON)) //
                  .build());

      Set<String> groups = api.listGroups();
      assertEquals(groups.size(), 5);
      assertTrue(groups.contains("admins"));
   }

   public void testGetGroupReturns2xx() {
      HostedChefApi api = requestSendsResponse(
            signed(HttpRequest.builder() //
                  .method("GET") //
                  .endpoint("https://api.opscode.com/groups/admins") //
                  .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
                  .addHeader("Accept", MediaType.APPLICATION_JSON).build()), //
            HttpResponse.builder().statusCode(200)
                  .payload(payloadFromResourceWithContentType("/group.json", MediaType.APPLICATION_JSON)) //
                  .build());

      Group group = api.getGroup("admins");
      assertEquals(group.getName(), "admins");
      assertEquals(group.getGroupname(), "admins");
   }

   public void testGetGroupReturns404() {
      HostedChefApi api = requestSendsResponse(signed(HttpRequest.builder() //
            .method("GET") //
            .endpoint("https://api.opscode.com/groups/foo") //
            .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
            .addHeader("Accept", MediaType.APPLICATION_JSON) //
            .build()), //
            HttpResponse.builder().statusCode(404).build());

      assertNull(api.getGroup("foo"));
   }

   public void testCreateGroupReturns2xx() {
      HostedChefApi api = requestSendsResponse(signed(HttpRequest.builder() //
            .method("POST") //
            .endpoint("https://api.opscode.com/groups") //
            .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
            .addHeader("Accept", MediaType.APPLICATION_JSON) //
            .payload(payloadFromStringWithContentType("{\"groupname\":\"foo\"}", MediaType.APPLICATION_JSON)) //
            .build()), //
            HttpResponse.builder().statusCode(201).build());

      api.createGroup("foo");
   }

   public void testDeleteGroupReturns2xx() {
      HostedChefApi api = requestSendsResponse(signed(HttpRequest.builder() //
            .method("DELETE") //
            .endpoint("https://api.opscode.com/groups/foo") //
            .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
            .addHeader("Accept", MediaType.APPLICATION_JSON) //
            .build()), //
            HttpResponse.builder().statusCode(200).build());

      api.deleteGroup("foo");
   }

   @Test(expectedExceptions = ResourceNotFoundException.class)
   public void testDeleteGroupFailsOn404() {
      HostedChefApi api = requestSendsResponse(signed(HttpRequest.builder() //
            .method("DELETE") //
            .endpoint("https://api.opscode.com/groups/foo") //
            .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
            .addHeader("Accept", MediaType.APPLICATION_JSON) //
            .build()), //
            HttpResponse.builder().statusCode(404).build());

      api.deleteGroup("foo");
   }

   public void testUpdateGroupReturns2xx() {
      HostedChefApi api = requestSendsResponse(signed(HttpRequest.builder() //
            .method("PUT") //
            .endpoint("https://api.opscode.com/groups/admins") //
            .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
            .addHeader("Accept", MediaType.APPLICATION_JSON) //
            .payload(payloadFromResourceWithContentType("/group-update.json", MediaType.APPLICATION_JSON)) //
            .build()), //
            HttpResponse.builder().statusCode(200).build());

      Group group = new Group("admins");
      group.setClients(ImmutableSet.of("abiquo"));
      group.setGroups(ImmutableSet.of("admins"));
      group.setUsers(ImmutableSet.of("nacx"));

      api.updateGroup(group);
   }

   @Test(expectedExceptions = ResourceNotFoundException.class)
   public void testUpdateGroupFailsOn404() {
      HostedChefApi api = requestSendsResponse(signed(HttpRequest.builder() //
            .method("PUT") //
            .endpoint("https://api.opscode.com/groups/admins") //
            .addHeader("X-Chef-Version", ChefAsyncApi.VERSION) //
            .addHeader("Accept", MediaType.APPLICATION_JSON) //
            .payload(payloadFromResourceWithContentType("/group-update.json", MediaType.APPLICATION_JSON)) //
            .build()), //
            HttpResponse.builder().statusCode(404).build());

      Group group = new Group("admins");
      group.setClients(ImmutableSet.of("abiquo"));
      group.setGroups(ImmutableSet.of("admins"));
      group.setUsers(ImmutableSet.of("nacx"));

      api.updateGroup(group);
   }

   @Override
   protected Module createModule() {
      return new TestHostedChefRestClientModule();
   }

   @ConfiguresRestClient
   static class TestHostedChefRestClientModule extends HostedChefRestClientModule {
      @Override
      protected String provideTimeStamp(@TimeStamp Supplier<String> cache) {
         return "timestamp";
      }
   }

   @Override
   protected ProviderMetadata createProviderMetadata() {
      return new HostedChefProviderMetadata();
   }

}
