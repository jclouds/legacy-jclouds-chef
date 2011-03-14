/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.chef.demo.integration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;

import org.jclouds.chef.ChefAsyncClient;
import org.jclouds.chef.ChefClient;
import org.jclouds.chef.ChefContext;
import org.jclouds.chef.ChefService;
import org.jclouds.chef.domain.Node;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.rest.RestContextFactory;
import org.jclouds.util.Utils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.appengine.repackaged.com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.inject.Module;

/**
 * Starts up the Google App Engine for Java Development environment and deploys
 * an application which tests Chef.
 * 
 * @author Adrian Cole
 */
@Test(groups = { "live" }, sequential = true)
public class GoogleAppEngineLiveTest {

   GoogleDevServer server;
   private URL url;

   @BeforeTest
   @Parameters( { "jclouds.chef.identity.pem", "warfile", "devappserver.address", "devappserver.port" })
   public void startDevAppServer(final String pemFile, final String warfile, final String address, final String port)
         throws Exception {
      url = new URL(String.format("http://%s:%s", address, port));

      server = new GoogleDevServer();
      // TODO move this out of band
      Files.write(Files.toString(new File(pemFile), Charsets.UTF_8), new File(String.format(
            "%s/WEB-INF/classes/chef.credential", warfile)), Charsets.UTF_8);
      server.startServer(address, port, warfile);
   }

   @Test
   public void shouldPass() throws InterruptedException, IOException {
      InputStream i = url.openStream();
      String string = Utils.toStringAndClose(i);
      assert string.indexOf("Welcome") >= 0 : string;
   }

   @Test(invocationCount = 5, enabled = true)
   public void testGuiceJCloudsSerial() throws InterruptedException, IOException {
      URL gurl = new URL(url, "/nodes/do");
      InputStream i = gurl.openStream();
      String string = Utils.toStringAndClose(i);
      assert string.indexOf("Opscode Platform") >= 0 : string;
   }

   @Test(invocationCount = 10, enabled = true, threadPoolSize = 3)
   public void testGuiceJCloudsParallel() throws InterruptedException, IOException {
      URL gurl = new URL(url, "/nodes/do");
      InputStream i = gurl.openStream();
      String string = Utils.toStringAndClose(i);
      assert string.indexOf("Opscode Platform") >= 0 : string;
   }

   @BeforeTest(groups = { "live" })
   @AfterTest(groups = { "live" })
   @Parameters( { "appengine.applicationid", "jclouds.chef.endpoint", "jclouds.chef.identity",
         "jclouds.chef.identity.pem" })
   public void cleanupDevelopmentClientsAndNodes(String applicationId, String endpoint, String identity,
         String identityPemFile) throws IOException {

      Properties props = new Properties();
      props.setProperty("chef.endpoint", endpoint);

      ChefContext context = buildContext(identity, identityPemFile, props);

      // careful... you may clean up your production nodes!
      // context.getChefService().cleanupStaleNodesAndClients(applicationId, 30
      // * 60);
      cleanupDevelopmentNodes(context.getChefService(), applicationId);
   }

   private ChefContext buildContext(String identity, String identityPemFile, Properties props) throws IOException {
      ChefContext context = (ChefContext) new RestContextFactory().<ChefClient, ChefAsyncClient> createContextBuilder(
            "chef", identity, Files.toString(new File(identityPemFile), Charsets.UTF_8),
            ImmutableSet.<Module> of(new Log4JLoggingModule()), props).buildContext();
      return context;
   }

   private void cleanupDevelopmentNodes(ChefService chef, final String applicationId) {
      for (Node node : chef.listNodesMatching(new Predicate<String>() {

         @Override
         public boolean apply(String input) {
            return input.startsWith(applicationId);
         }

      })) {
         if (node.getAutomatic().get("java") != null) {
            Properties systemProperties = chef.getContext().utils().json().fromJson(
                  node.getAutomatic().get("java").toString(), Properties.class);
            String environment = systemProperties.getProperty("com.google.appengine.runtime.environment");
            if (environment.equalsIgnoreCase("Development")) {
               chef.deleteAllNodesInList(Collections.singleton(node.getName()));
            }
         }
      }
   }
}
