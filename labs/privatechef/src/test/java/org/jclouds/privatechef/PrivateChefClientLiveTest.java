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

import java.util.Properties;
import java.util.Set;

import org.jclouds.chef.ChefClient;
import org.jclouds.chef.internal.BaseChefClientLiveTest;
import org.jclouds.crypto.Pems;
import org.jclouds.privatechef.domain.Organization;
import org.jclouds.privatechef.domain.User;
import org.jclouds.rest.AuthorizationException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;

/**
 * Tests behavior of {@code PrivateChefClient}
 * TODO: needs complete refactoring
 * @author Adrian Cole
 */
@Test(groups = "live", testName = "PrivateChefClientLiveTest")
public class PrivateChefClientLiveTest extends BaseChefClientLiveTest<PrivateChefContext> {

   public PrivateChefClientLiveTest(){
       provider = "privatechef";
   }
    
//   private String privateChefIdentity;
//   private String privateChefCredential;
//   protected context context;
   

   private String orgname = System.getProperty("test.jclouds.privatechef.org");
   private Organization org;
   private User orgUser;
   private String createdOrgname;
   
   // Private Chef does not allow validators to create clients
   @Override
   @Test(expectedExceptions = AuthorizationException.class)
   public void testValidatorCreateClient() throws Exception {
       super.testValidatorCreateClient();
   }
   
//   protected Properties setupPrivateChefProperties() {
//       Properties overrides = setupProperties();
//       privateChefIdentity = setIfTestSystemPropertyPresent(overrides, provider + ".validator.identity");
//       privateChefCredential = setCredentialFromPemFile(overrides, privateChefIdentity, provider + ".validator.credential");
//       overrides.setProperty(provider + ".identity", privateChefIdentity);
//       overrides.setProperty(provider + ".credential", privateChefCredential);
//       return overrides;
//   }

//   @BeforeClass(groups = { "integration", "live" })
//   @Override
//   public void setupContext() {
//       super.setupContext();
//       context = newBuilder()
//           .modules(setupModules())
//           .overrides(setupProperties())
//           .build(TypeToken.of(context.class)); 
//   }

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
//      assertEquals(org.getClientname(), createdOrgname + "-validator");
//      assertNull(org.getOrgType());
//      assertNotNull(org.getPrivateKey());
//      context connection = null;
//      try {
//         connection = createConnection(org.getClientname(), Pems.pem(org.getPrivateKey()));
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
//
//   @Test(expectedExceptions = AuthorizationException.class, enabled = false)
//   public void testListUsers() throws Exception {
//      Set<String> orgs = context.getApi().listUsers();
//      assertNotNull(orgs);
//   }
//
//   // @Test(expectedExceptions = HttpResponseException.class)
//   @Test(enabled = false, expectedExceptions = AuthorizationException.class)
//   public void testCreateUser() throws Exception {
//       context.getApi().deleteUser(PREFIX);
//       context.getApi().createUser(new User(PREFIX));
//      orgUser = context.getApi().getUser(PREFIX);
//      assertNotNull(orgUser);
//      assertEquals(orgUser.getUsername(), PREFIX);
//      assertNotNull(orgUser.getPrivateKey());
//   }

//   @Test(enabled = false)
//   public void testUserExists() throws Exception {
//      assertNotNull(context.getApi().userExists(user));
//   }
//
//   @Test(enabled = false)
//   public void testGetUser() throws Exception {
//       context.getApi().getUser(user);
//   }

    // disabled while create user fails
//   @Test(dependsOnMethods = "testCreateUser", enabled = false)
//   public void testUpdateUser() throws Exception {
//      User user = context.getApi().getUser(PREFIX);
//      context.getApi().updateUser(user);
//   }
//
//   @Test//(expectedExceptions = AuthorizationException.class)
//   public void testGetOrganizationFailsForValidationKey() throws Exception {
//       context.getApi().getOrganization(orgname);
//   }
//
//   @Test//(dependsOnMethods = "testGenerateKeyForClient", expectedExceptions = AuthorizationException.class)
//   public void testGetOrganizationFailsForClient() throws Exception {
//       context.getApi().getOrganization(orgname);
//   }
//
    @Override
    protected ChefClient getChefClient(PrivateChefContext context) {
        return context.getApi().getChefClient();
    }

    @Override
    protected TypeToken<PrivateChefContext> contextType() {
        return TypeToken.of(PrivateChefContext.class);
    }
}
