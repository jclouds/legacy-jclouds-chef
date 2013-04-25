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

import java.util.Set;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Constants;
import org.jclouds.Fallbacks.FalseOnNotFoundOr404;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.filters.SignedHeaderAuth;
import org.jclouds.chef.functions.ParseKeySetFromJson;
import org.jclouds.hostedchef.binders.BindGroupNameToJsonPayload;
import org.jclouds.hostedchef.binders.BindGroupToUpdateRequestJsonPayload;
import org.jclouds.hostedchef.binders.GroupName;
import org.jclouds.hostedchef.domain.Group;
import org.jclouds.hostedchef.domain.User;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.Headers;
import org.jclouds.rest.annotations.ParamParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;

/**
 * Provides synchronous access to the Hosted Chef Api.
 * 
 * @author Ignasi Barrera
 */
@RequestFilters(SignedHeaderAuth.class)
@Consumes(MediaType.APPLICATION_JSON)
@Headers(keys = "X-Chef-Version", values = "{" + Constants.PROPERTY_API_VERSION + "}")
public interface HostedChefApi extends ChefApi {

   /**
    * Check if there exists a node with the given name.
    * 
    * @return <code>true</code> if the specified node name exists.
    */
   @Override
   // Use get instead of HEAD
   @Named("node:exists")
   @GET
   @Path("/nodes/{nodename}")
   @Fallback(FalseOnNotFoundOr404.class)
   boolean nodeExists(@PathParam("nodename") String nodename);

   /**
    * Retrieves an existing user.
    * 
    * @param name
    *           The name of the user to get.
    * @return The details of the user or <code>null</code> if not found.
    */
   @Named("user:get")
   @GET
   @Path("/users/{name}")
   @Fallback(NullOnNotFoundOr404.class)
   User getUser(@PathParam("name") String name);

   /**
    * List all existing groups.
    * 
    * @return The list of groups.
    */
   @Named("group:list")
   @GET
   @Path("/groups")
   @ResponseParser(ParseKeySetFromJson.class)
   Set<String> listGroups();

   /**
    * Retrieves an existing group.
    * 
    * @param name
    *           The name of the group to get.
    * @return The details of the group or <code>null</code> if not found.
    */
   @Named("group:get")
   @GET
   @Path("/groups/{name}")
   @Fallback(NullOnNotFoundOr404.class)
   Group getGroup(@PathParam("name") String name);

   /**
    * Creates a new group.
    * 
    * @param name
    *           The name of the group to create.
    */
   @Named("group:create")
   @POST
   @Path("/groups")
   void createGroup(@BinderParam(BindGroupNameToJsonPayload.class) String name);

   /**
    * Updates a group.
    * <p>
    * This method can be used to add actors (clients, groups) to the group.
    * 
    * @param group
    *           The group with the updated information.
    */
   @Named("group:update")
   @PUT
   @Path("/groups/{name}")
   void updateGroup(
         @PathParam("name") @ParamParser(GroupName.class) @BinderParam(BindGroupToUpdateRequestJsonPayload.class) Group group);

   /**
    * Deletes a group.
    * 
    * @param name
    *           The name of the group to delete.
    */
   @Named("group:delete")
   @DELETE
   @Path("/groups/{name}")
   void deleteGroup(@PathParam("name") String name);

}
