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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Constants;
import org.jclouds.chef.filters.SignedHeaderAuth;
import org.jclouds.chef.functions.ParseKeySetFromJson;
import org.jclouds.hostedchef.binders.BindGroupNameToJsonPayload;
import org.jclouds.hostedchef.binders.BindGroupToUpdateRequestJsonPayload;
import org.jclouds.hostedchef.binders.GroupName;
import org.jclouds.hostedchef.domain.Group;
import org.jclouds.hostedchef.domain.User;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.Headers;
import org.jclouds.rest.annotations.ParamParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to the Hosted Chef via their REST API.
 * 
 * @see HostedChefApi
 * @author Ignasi Barrera
 */
@RequestFilters(SignedHeaderAuth.class)
@Consumes(MediaType.APPLICATION_JSON)
@Headers(keys = "X-Chef-Version", values = "{" + Constants.PROPERTY_API_VERSION + "}")
public interface HostedChefAsyncApi {

   /**
    * @see HostedChefApi#getChefApi()
    */
   @Delegate
   PatchedChefAsyncApi getChefApi();

   /**
    * @see HostedChefApi#getUser(String)
    */
   @GET
   @Path("/users/{name}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<User> getUser(@PathParam("name") String name);

   /**
    * @see HostedChefApi#listGroups()
    */
   @GET
   @Path("/groups")
   @ResponseParser(ParseKeySetFromJson.class)
   ListenableFuture<Set<String>> listGroups();

   /**
    * @see HostedChefApi#getGroup(String)
    */
   @GET
   @Path("/groups/{name}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Group> getGroup(@PathParam("name") String name);

   /**
    * @see HostedChefApi#createGroup(String)
    */
   @POST
   @Path("/groups")
   ListenableFuture<Void> createGroup(@BinderParam(BindGroupNameToJsonPayload.class) String name);

   /**
    * @see HostedChefApi#updateGroup(Group)
    */
   @PUT
   @Path("/groups/{name}")
   ListenableFuture<Void> updateGroup(
         @PathParam("name") @ParamParser(GroupName.class) @BinderParam(BindGroupToUpdateRequestJsonPayload.class) Group group);

   /**
    * @see HostedChefApi#getGroup(String)
    */
   @DELETE
   @Path("/groups/{name}")
   ListenableFuture<Void> deleteGroup(@PathParam("name") String name);

}
