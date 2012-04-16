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
package org.jclouds.chef.demo.ohai;

import static org.jclouds.util.Utils.escapeJsonString;
import static org.jclouds.util.Utils.toStringAndClose;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

import org.jclouds.logging.Logger;
import org.jclouds.ohai.servlet.suppliers.ServletContextSupplier;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class WebXmlSupplier extends ServletContextSupplier {

   @Resource
   protected Logger logger = Logger.NULL;

   @Inject
   public WebXmlSupplier(ServletContext servletContext) {
      super(servletContext);
   }

   @Override
   protected String provideJson(ServletContext servletContext) {
      try {
         logger.debug("loading web.xml");
         return String.format("{\"web_xml\":\"%s\"}", escapeJsonString(toStringAndClose(servletContext
               .getResourceAsStream("/WEB-INF/web.xml"))));
      } catch (Exception e) {
         logger.warn(e, "could not load web.xml");
         return "{}";
      }
   }
}