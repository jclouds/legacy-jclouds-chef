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
package org.jclouds.chef.servlet;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.singleton;
import static org.jclouds.chef.config.ChefProperties.CHEF_NODE;
import static org.jclouds.chef.config.ChefProperties.CHEF_NODE_PATTERN;
import static org.jclouds.chef.config.ChefProperties.CHEF_RUN_LIST;
import static org.jclouds.chef.config.ChefProperties.CHEF_SERVICE_CLIENT;

import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jclouds.ContextBuilder;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.ChefApiMetadata;
import org.jclouds.chef.ChefContext;
import org.jclouds.chef.ChefService;
import org.jclouds.chef.domain.Node;
import org.jclouds.chef.servlet.functions.InitParamsToProperties;
import org.jclouds.logging.Logger;
import org.jclouds.logging.jdk.JDKLogger;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.AbstractModule;

/**
 * Registers a new node in Chef and binds its name to
 * {@link ChefConstants.CHEF_NODE}, its role to {@link ChefConstants.CHEF_ROLE}
 * and the {@link ChefService} for the client to
 * {@link ChefConstants.CHEF_SERVICE_CLIENT} upon initialized. Deletes the node
 * and client when the context is destroyed.
 * 
 * @author Adrian Cole
 */
public class ChefRegistrationListener implements ServletContextListener {

   private Logger logger = new JDKLogger.JDKLoggerFactory().getLogger(ChefRegistrationListener.class.getName());

   @Override
   public void contextInitialized(ServletContextEvent servletContextEvent) {
      try {
         logger.debug("starting initialization");
         Properties overrides = InitParamsToProperties.INSTANCE.apply(servletContextEvent.getServletContext());

         logger.trace("creating client connection");

         ChefService client = createService(overrides, servletContextEvent);
         logger.debug("created client connection");

         Node node;
         String nodeName;
         while (true) {
            nodeName = findNextNodeName(client, getInitParam(servletContextEvent, CHEF_NODE_PATTERN));
            try {
               node = client.createNodeAndPopulateAutomaticAttributes(nodeName,
                     Splitter.on(',').split(getInitParam(servletContextEvent, CHEF_RUN_LIST)));
               break;
            } catch (IllegalStateException ex) {
               logger.debug("client already exists %s: %s", nodeName, ex.getMessage());
            }
         }

         servletContextEvent.getServletContext().setAttribute(CHEF_NODE, node);
         servletContextEvent.getServletContext().setAttribute(CHEF_SERVICE_CLIENT, client);
         logger.debug("initialized");
      } catch (RuntimeException e) {
         logger.error(e, "error registering");
         throw e;
      }
   }

   private String findNextNodeName(ChefService client, String pattern) {
      ChefApi api = client.getContext().getApi(ChefApi.class);
      Set<String> nodes = api.listNodes();
      String nodeName;
      Set<String> names = newHashSet(nodes);
      int index = 0;
      while (true) {
         nodeName = String.format(pattern, index++);
         if (!names.contains(nodeName)) {
            break;
         }
      }
      return nodeName;
   }

   private ChefService createService(Properties props, final ServletContextEvent servletContextEvent) {
      return ContextBuilder.newBuilder(new ChefApiMetadata()).modules(ImmutableSet.of(new AbstractModule() {

         @Override
         protected void configure() {
            bind(ServletContext.class).toInstance(servletContextEvent.getServletContext());
         }

      })).overrides(props).buildView(ChefContext.class).getChefService();
   }

   private static String getInitParam(ServletContextEvent servletContextEvent, String name) {
      return checkNotNull(servletContextEvent.getServletContext().getInitParameter(name));
   }

   @SuppressWarnings("unchecked")
   private static <T> T getContextAttributeOrNull(ServletContextEvent servletContextEvent, String name) {
      return (T) servletContextEvent.getServletContext().getAttribute(name);
   }

   /**
    * removes the node and client if found, and closes the client context.
    */
   @Override
   public void contextDestroyed(ServletContextEvent servletContextEvent) {
      ChefService client = getContextAttributeOrNull(servletContextEvent, CHEF_SERVICE_CLIENT);
      Node node = getContextAttributeOrNull(servletContextEvent, CHEF_NODE);
      if (node != null && client != null) {
         client.deleteAllNodesInList(singleton(node.getName()));
      }
      if (client != null) {
         Closeables.closeQuietly(client.getContext());
      }
   }
}