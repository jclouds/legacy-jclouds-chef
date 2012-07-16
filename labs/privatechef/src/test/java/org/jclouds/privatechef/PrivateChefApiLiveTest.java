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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Set;

import org.jclouds.chef.ChefApi;
import org.jclouds.chef.internal.BaseChefApiLiveTest;
import org.jclouds.privatechef.domain.Organization;
import org.jclouds.privatechef.domain.User;
import org.jclouds.rest.AuthorizationException;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;

/**
 * Tests behavior of {@code PrivateChefApi}
 * TODO: needs complete refactoring
 * @author Adrian Cole
 */
@Test(groups = "live", testName = "PrivateChefApiLiveTest")
public class PrivateChefApiLiveTest extends BaseChefApiLiveTest<PrivateChefContext> {

   public PrivateChefApiLiveTest(){
       provider = "privatechef";
   }

   private String orgname = System.getProperty("test.jclouds." + provider + ".org");
   private Organization org;
   private User orgUser;
   private String createdOrgname;
   
   // Private Chef does not allow validators to create apis
   @Override
   @Test(expectedExceptions = AuthorizationException.class)
   public void testValidatorCreateClient() throws Exception {
       super.testValidatorCreateClient();
   }

//   @Test(expectedExceptions = AuthorizationException.class)
//   public void testListOrganizations() throws Exception {
//      Set<String> orgs = context.getApi().listOrganizations();
//      assertNotNull(orgs);
//   }

//   /**
//    * this test only works when you have a super user not yet supported in the official api
//    */
//   @Test(enabled = false, expectedExceptions = AuthorizationException.class)
//   public void testCreateOrganization() throws Exception {
//      createdOrgname = orgname + 1;
//      context.getApi().deleteOrganization(createdOrgname);
//      org = context.getApi().createOrganization(new Organization(createdOrgname, Organization.Type.BUSINESS));
//      assertNotNull(org);
//      assertNull(org.getName());
//      assertNull(org.getFullName());
//      assertEquals(org.getApiname(), createdOrgname + "-validator");
//      assertNull(org.getOrgType());
//      assertNotNull(org.getPrivateKey());
//      context connection = null;
//      try {
//         connection = createConnection(org.getApiname(), Pems.pem(org.getPrivateKey()));
//      } finally {
//         if (connection != null)
//            connection.close();
//      }
//   }

//   public void testOrganizationExists() throws Exception {
//      assertNotNull(context.getApi().organizationExists(orgname));
//   }

//   @Test(enabled = false, dependsOnMethods = "testCreateOrganization", expectedExceptions = AuthorizationException.class)
//   public void testUpdateOrganization() throws Exception {
//      Organization org = context.getApi().getOrganization(createdOrgname);
//      context.getApi().updateOrganization(org);
//   }

//   public void testGetOrganization() throws Exception {
//       context.getApi().getOrganization(orgname);
//   }
   
// @Test//(expectedExceptions = AuthorizationException.class)
// public void testGetOrganizationFailsForValidationKey() throws Exception {
//     context.getApi().getOrganization(orgname);
// }
//
// @Test//(dependsOnMethods = "testGenerateKeyForApi", expectedExceptions = AuthorizationException.class)
// public void testGetOrganizationFailsForApi() throws Exception {
//     context.getApi().getOrganization(orgname);
// }
//
   
//
   
//   @Test
//   public void testListUsers() throws Exception {
//      Set<String> orgs = context.getApi().listUsers();
//      assertNotNull(orgs);
//   }

//   @Test(enabled = false)
//   public void testCreateUser() throws Exception {
//      context.getApi().deleteUser(PREFIX);
//      context.getApi().createUser(new User(PREFIX));
//      orgUser = context.getApi().getUser(PREFIX);
//      assertNotNull(orgUser);
//      assertEquals(orgUser.getUsername(), PREFIX);
//      assertNotNull(orgUser.getPrivateKey());
//   }
//
//   @Test(enabled = false)
//   public void testUserExists() throws Exception {
//      assertNotNull(context.getApi().userExists(identity));
//   }
//
//   @Test(enabled = false)
//   public void testGetUser() throws Exception {
//       context.getApi().getUser(identity);
//   }
//
//   @Test(dependsOnMethods = "testCreateUser", enabled = false)
//   public void testUpdateUser() throws Exception {
//      User user = context.getApi().getUser(PREFIX);
//      context.getApi().updateUser(user);
//   }

    @Override
    protected ChefApi getChefApi(PrivateChefContext context) {
        return context.getApi().getChefApi();
    }

    @Override
    protected TypeToken<PrivateChefContext> contextType() {
        return TypeToken.of(PrivateChefContext.class);
    }
}
