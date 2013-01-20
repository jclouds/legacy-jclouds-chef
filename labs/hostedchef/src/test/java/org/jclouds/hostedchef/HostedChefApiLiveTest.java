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

import static org.jclouds.reflect.Reflection2.typeToken;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.jclouds.chef.ChefApi;
import org.jclouds.chef.internal.BaseChefApiLiveTest;
import org.jclouds.hostedchef.domain.Group;
import org.jclouds.hostedchef.domain.User;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.ResourceNotFoundException;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;

/**
 * Tests behavior of the HostedChefApi.
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", singleThreaded = true, testName = "HostedChefApiLiveTest")
public class HostedChefApiLiveTest extends BaseChefApiLiveTest<HostedChefContext> {

   private static final String GROUP_NAME = System.getProperty("user.name") + "-jcloudstest";

   public HostedChefApiLiveTest() {
      provider = "hostedchef";
   }

   @Override
   @Test(expectedExceptions = AuthorizationException.class)
   public void testValidatorCreateClient() throws Exception {
      validatorClient.createClient(VALIDATOR_PREFIX);
   }

   @Override
   @Test
   public void testSearchClientsWithOptions() throws Exception {
      // This test will fail because Hosted Chef does not index client name.
      // Once it is fixes, the test should suceed.
      // See: http://tickets.opscode.com/browse/CHEF-2477
      super.testSearchClientsWithOptions();
   }

   public void testGetUser() {
      User user = context.getApi().getUser(identity);
      assertEquals(user.getUsername(), identity);
      assertNotNull(user.getPublicKey());
   }

   public void testGetUnexistingUser() {
      User user = context.getApi().getUser(UUID.randomUUID().toString());
      assertNull(user);
   }

   public void testListGroups() {
      Set<String> groups = context.getApi().listGroups();
      assertNotNull(groups);
      assertFalse(groups.isEmpty());
   }

   public void testGetUnexistingGroup() {
      Group group = context.getApi().getGroup(UUID.randomUUID().toString());
      assertNull(group);
   }

   public void testCreateGroup() {
      context.getApi().createGroup(GROUP_NAME);
      Group group = context.getApi().getGroup(GROUP_NAME);
      assertNotNull(group);
      assertEquals(group.getGroupname(), GROUP_NAME);
   }

   @Test(dependsOnMethods = "testCreateGroup")
   public void testUpdateGroup() {
      Group group = context.getApi().getGroup(GROUP_NAME);
      group.setUsers(ImmutableSet.of(identity));
      group.setClients(ImmutableSet.of(validatorIdentity));

      context.getApi().updateGroup(group);
      group = context.getApi().getGroup(GROUP_NAME);

      assertNotNull(group);
      assertTrue(group.getUsers().contains(identity));
      assertTrue(group.getClients().contains(validatorIdentity));
   }

   @Test(expectedExceptions = ResourceNotFoundException.class)
   public void testUpdateUnexistingGroup() {
      context.getApi().updateGroup(new Group(UUID.randomUUID().toString()));
   }

   @Test(dependsOnMethods = "testUpdateGroup")
   public void testDeleteGroup() {
      context.getApi().deleteGroup(GROUP_NAME);
      Group group = context.getApi().getGroup(GROUP_NAME);
      assertNull(group);
   }

   @Test(expectedExceptions = ResourceNotFoundException.class)
   public void testDeleteUnexistingGroup() {
      context.getApi().deleteGroup(UUID.randomUUID().toString());
   }

   @Override
   protected ChefApi getChefApi(HostedChefContext context) {
      return context.getApi().getChefApi();
   }

   @Override
   protected TypeToken<HostedChefContext> contextType() {
      return typeToken(HostedChefContext.class);
   }

}
