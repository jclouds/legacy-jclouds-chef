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
package org.jclouds.chef.demo.controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jclouds.chef.ChefService;
import org.jclouds.chef.domain.Node;
import org.jclouds.chef.domain.Role;
import org.jclouds.chef.util.ChefUtils;
import org.jclouds.logging.Logger;

/**
 * Shows an example of how to use @{link ChefClient}
 * 
 * @author Adrian Cole
 */
@Singleton
public class GetNodeStatusController extends HttpServlet {

   @Resource
   protected Logger logger = Logger.NULL;

   private static final long serialVersionUID = 1L;

   private final ChefService chef;
   private final Node node;

   @Inject
   GetNodeStatusController(ChefService chef, Node node) {
      this.chef = checkNotNull(chef, "chef");
      this.node = checkNotNull(node, "node");
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      try {
         addStatusResultsToRequest(request);
         RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/status.jsp");
         dispatcher.forward(request, response);
      } catch (Exception e) {
         logger.error(e, "Error listing status");
         throw new ServletException(e);
      }
   }

   private void addStatusResultsToRequest(HttpServletRequest request) throws InterruptedException, ExecutionException,
         TimeoutException {
      String roleName = ChefUtils.findRoleInRunList(node.getRunList());

      Future<Role> roleF = chef.getContext().getAsyncApi().getRole(roleName);
      Future<Set<String>> items = chef.getContext().getAsyncApi().listDatabagItems(roleName);

      request.setAttribute("node", node);
      request.setAttribute("role", roleF.get());
      request.setAttribute("items", items.get());
   }

}
