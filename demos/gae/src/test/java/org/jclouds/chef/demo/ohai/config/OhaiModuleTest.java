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
package org.jclouds.chef.demo.ohai.config;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.testng.Assert.assertEquals;

import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.jclouds.chef.config.ChefParserModule;
import org.jclouds.domain.JsonBall;
import org.jclouds.json.Json;
import org.jclouds.json.config.GsonModule;
import org.jclouds.ohai.Automatic;
import org.jclouds.ohai.config.OhaiModule;
import org.jclouds.ohai.servlet.config.ServletOhaiModule;
import org.jclouds.util.Utils;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

/**
 * Tests behavior of {@code OhaiModule}
 * 
 * @author Adrian Cole
 */
@Test(groups = { "unit" })
public class OhaiModuleTest {

   @Test
   public void testAllModulesOutput() throws SocketException {
      final ServletContext servletContext = createMock(ServletContext.class);

      Enumeration<?> parameters = createMock(Enumeration.class);

      expect(servletContext.getContextPath()).andReturn("").atLeastOnce();
      expect(servletContext.getServerInfo()).andReturn("server info");
      expect(servletContext.getInitParameterNames()).andReturn(parameters);
      expect(parameters.hasMoreElements()).andReturn(false);

      expect(servletContext.getResourceAsStream("/WEB-INF/web.xml")).andReturn(
            Utils.toInputStream("<tag name=\"fooy\"></tag>"));

      replay(servletContext);
      replay(parameters);

      final Properties sysProperties = new Properties();

      sysProperties.setProperty("os.name", "Mac OS X");
      sysProperties.setProperty("os.version", "10.3.0");
      sysProperties.setProperty("user.name", "user");

      Injector injector = Guice.createInjector(new ChefParserModule(), new GsonModule(), new WebXmlOhaiModule(),
            new ServletOhaiModule(), new OhaiModule() {
               @SuppressWarnings("unused")
               @Provides
               protected ServletContext provideServletContext() {
                  return servletContext;
               }

               @Override
               protected Long millis() {
                  return 1279992919l;
               }

               @Override
               protected Properties systemProperties() {
                  return sysProperties;
               }

            });
      Ohai ohai = injector.getInstance(Ohai.class);
      Json json = injector.getInstance(Json.class);

      assertEquals(
            json.toJson(ohai.ohai.get(), new TypeLiteral<Map<String, JsonBall>>() {
            }.getType()),
            "{\"webapp\":{\"/\":{\"web_xml\":\"<tag name=\\\"fooy\\\"></tag>\",\"server_info\":\"server info\",\"init_params\":{}}},\"ohai_time\":1279992919,\"platform\":\"macosx\",\"platform_version\":\"10.3.0\",\"current_user\":\"user\",\"jvm\":{\"system\":{\"user.name\":\"user\",\"os.version\":\"10.3.0\",\"os.name\":\"Mac OS X\"}}}");

   }

   static class Ohai {
      private Supplier<Map<String, JsonBall>> ohai;

      @Inject
      public Ohai(@Automatic Supplier<Map<String, JsonBall>> ohai) {
         this.ohai = ohai;
      }
   }
}
