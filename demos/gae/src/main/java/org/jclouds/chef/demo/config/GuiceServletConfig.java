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
package org.jclouds.chef.demo.config;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.inject.name.Names.bindProperties;
import static org.jclouds.chef.reference.ChefConstants.CHEF_NODE;
import static org.jclouds.chef.reference.ChefConstants.CHEF_SERVICE_CLIENT;
import static org.jclouds.util.Utils.modulesForProviderInProperties;

import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.jclouds.PropertiesBuilder;
import org.jclouds.chef.ChefService;
import org.jclouds.chef.demo.controllers.GetNodeStatusController;
import org.jclouds.chef.domain.Node;
import org.jclouds.chef.servlet.functions.InitParamsToProperties;
import org.jclouds.json.config.GsonModule;
import org.jclouds.logging.Logger;
import org.jclouds.logging.jdk.JDKLogger;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Finds the nodename and client connection in the servlet context and binds
 * them to guice.
 * 
 * Guice then registers the servlets associated with this demo.
 * 
 * @author Adrian Cole
 */
public class GuiceServletConfig extends GuiceServletContextListener {

   private Logger logger = new JDKLogger.JDKLoggerFactory().getLogger(GuiceServletConfig.class.getName());
   private Properties properties;

   private Node node;
   private ChefService clientConnection;

   @Override
   public void contextInitialized(ServletContextEvent servletContextEvent) {
      try {
         logger.debug("starting initialization");
         properties = InitParamsToProperties.INSTANCE.apply(servletContextEvent.getServletContext());
         node = getContextAttribute(servletContextEvent, CHEF_NODE);
         clientConnection = getContextAttribute(servletContextEvent, CHEF_SERVICE_CLIENT);
         super.contextInitialized(servletContextEvent);
         logger.debug("initialized");
      } catch (RuntimeException e) {
         logger.error(e, "error initializing");
         throw e;
      }
   }

   @Override
   protected Injector getInjector() {
      return Guice.createInjector(concat(modulesForProviderInProperties("chef", properties), ImmutableSet.of(
            new GsonModule(), new ServletModule() {
               @Override
               protected void configureServlets() {
                  bindProperties(binder(), new PropertiesBuilder().build());
                  bind(Node.class).toInstance(node);
                  bind(ChefService.class).toInstance(clientConnection);
                  serve("/nodes/do").with(GetNodeStatusController.class);
               }
            })));
   }

   @SuppressWarnings("unchecked")
   private static <T> T getContextAttribute(ServletContextEvent servletContextEvent, String name) {
      return (T) checkNotNull(servletContextEvent.getServletContext().getAttribute(name), name);
   }
}