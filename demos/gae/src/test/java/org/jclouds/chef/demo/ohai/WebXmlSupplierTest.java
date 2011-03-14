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
package org.jclouds.chef.demo.ohai;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.testng.Assert.assertEquals;

import java.util.Map;

import javax.servlet.ServletContext;

import org.jclouds.json.Json;
import org.jclouds.json.config.GsonModule;
import org.jclouds.util.Utils;
import org.testng.annotations.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

/**
 * 
 * @author Adrian Cole
 */

@Test(groups = { "unit" })
public class WebXmlSupplierTest {

   @Test
   public void testApplyEmptyPath() {

      final ServletContext servletContext = createMock(ServletContext.class);

      expect(servletContext.getContextPath()).andReturn("");
      expect(servletContext.getResourceAsStream("/WEB-INF/web.xml")).andReturn(
            Utils.toInputStream("<tag name=\"fooy\"></tag>"));

      replay(servletContext);

      Injector injector = Guice.createInjector(new GsonModule(), new AbstractModule() {
         @SuppressWarnings("unused")
         @Provides
         protected ServletContext provideServletContext() {
            return servletContext;
         }

         @Override
         protected void configure() {
            
         }

      });

      Json json = injector.getInstance(Json.class);
      WebXmlSupplier ohai = injector.getInstance(WebXmlSupplier.class);
      String jsonV = json.toJson(ohai.get());
      json.fromJson(jsonV, new TypeLiteral<Map<String, Map<String, String>>>() {
      }.getType());
      assertEquals(json.<Map<String, Map<String, String>>> fromJson(jsonV,
            new TypeLiteral<Map<String, Map<String, String>>>() {
            }.getType()).get("/").get("web_xml"), "<tag name=\"fooy\"></tag>");

   }
}
