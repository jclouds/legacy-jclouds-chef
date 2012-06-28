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
package org.jclouds.chef;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.Constants;
import org.jclouds.chef.binders.BindChecksumsToJsonPayload;
import org.jclouds.chef.binders.BindCreateClientOptionsToJsonPayload;
import org.jclouds.chef.binders.BindGenerateKeyForClientToJsonPayload;
import org.jclouds.chef.binders.BindIsCompletedToJsonPayload;
import org.jclouds.chef.binders.BindNameToJsonPayload;
import org.jclouds.chef.binders.DatabagItemId;
import org.jclouds.chef.binders.NodeName;
import org.jclouds.chef.binders.RoleName;
import org.jclouds.chef.domain.Client;
import org.jclouds.chef.domain.CookbookVersion;
import org.jclouds.chef.domain.DatabagItem;
import org.jclouds.chef.domain.Node;
import org.jclouds.chef.domain.Resource;
import org.jclouds.chef.domain.Role;
import org.jclouds.chef.domain.Sandbox;
import org.jclouds.chef.domain.SearchResult;
import org.jclouds.chef.domain.UploadSandbox;
import org.jclouds.chef.filters.SignedHeaderAuth;
import org.jclouds.chef.functions.ParseKeySetFromJson;
import org.jclouds.chef.functions.ParseSearchClientsFromJson;
import org.jclouds.chef.functions.ParseSearchDatabagFromJson;
import org.jclouds.chef.functions.ParseSearchNodesFromJson;
import org.jclouds.chef.functions.ParseSearchRolesFromJson;
import org.jclouds.chef.functions.UriForResource;
import org.jclouds.chef.options.CreateClientOptions;
import org.jclouds.io.Payload;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.Headers;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.ParamParser;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.annotations.Unwrap;
import org.jclouds.rest.binders.BindToJsonPayload;
import org.jclouds.rest.functions.ReturnEmptySetOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnFalseOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.functions.ReturnVoidOnNotFoundOr404;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Chef via their REST API.
 * <p/>
 * 
 * @see ChefClient
 * @see <a href="TODO: insert URL of provider documentation" />
 * @author Adrian Cole
 */
@RequestFilters(SignedHeaderAuth.class)
@Headers(keys = "X-Chef-Version", values = "{" + Constants.PROPERTY_API_VERSION + "}")
@Consumes(MediaType.APPLICATION_JSON)
public interface ChefAsyncClient {
   public static final String VERSION = "0.9.8";

   /**
    * @see ChefClient#getUploadSandboxForChecksums(Set)
    */
   @POST
   @Path("/sandboxes")
   ListenableFuture<UploadSandbox> getUploadSandboxForChecksums(
            @BinderParam(BindChecksumsToJsonPayload.class) Set<List<Byte>> md5s);

   /**
    * @see ChefClient#uploadContent(URI, Payload)
    */
   @PUT
   @Produces("application/x-binary")
   ListenableFuture<Void> uploadContent(@EndpointParam URI location, Payload content);

   /**
    * @see ChefClient#commitSandbox(String, boolean)
    */
   @PUT
   @Path("/sandboxes/{id}")
   ListenableFuture<Sandbox> commitSandbox(@PathParam("id") String id,
            @BinderParam(BindIsCompletedToJsonPayload.class) boolean isCompleted);

   /**
    * @see ChefClient#listCookbooks()
    */
   @GET
   @Path("/cookbooks")
   @ResponseParser(ParseKeySetFromJson.class)
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> listCookbooks();

   /**
    * @see ChefClient#updateCookbook(String, String, CookbookVersion)
    */
   @PUT
   @Path("/cookbooks/{cookbookname}/{version}")
   ListenableFuture<CookbookVersion> updateCookbook(@PathParam("cookbookname") String cookbookName,
            @PathParam("version") String version, @BinderParam(BindToJsonPayload.class) CookbookVersion cookbook);

   /**
    * @see ChefClient#deleteCookbook(String, String)
    */
   @DELETE
   @Path("/cookbooks/{cookbookname}/{version}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<CookbookVersion> deleteCookbook(@PathParam("cookbookname") String cookbookName,
            @PathParam("version") String version);

   /**
    * @see ChefClient#getVersionsOfCookbook(String)
    */
   @GET
   @Path("/cookbooks/{cookbookname}")
   @Unwrap
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> getVersionsOfCookbook(@PathParam("cookbookname") String cookbookName);

   /**
    * @see ChefClient#getCookbook(String, String)
    */
   @GET
   @Path("/cookbooks/{cookbookname}/{version}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<CookbookVersion> getCookbook(@PathParam("cookbookname") String cookbookName,
            @PathParam("version") String version);

   /**
    * @see ChefClient#createClient(String)
    */
   @POST
   @Path("/clients")
   @MapBinder(BindToJsonPayload.class)
   ListenableFuture<Client> createClient(@PayloadParam("name") String clientname);
   
   /**
    * @see ChefClient#createClient(String, CreateClientOptions)
    */
   @POST
   @Path("/clients")
   @MapBinder(BindCreateClientOptionsToJsonPayload.class)
   ListenableFuture<Client> createClient(@PayloadParam("name") String clientname, CreateClientOptions options);

   /**
    * @see ChefClient#generateKeyForClient(String)
    */
   @PUT
   @Path("/clients/{clientname}")
   ListenableFuture<Client> generateKeyForClient(
            @PathParam("clientname") @BinderParam(BindGenerateKeyForClientToJsonPayload.class) String clientname);

   /**
    * @see ChefClient#clientExists(String)
    */
   @HEAD
   @Path("/clients/{clientname}")
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   ListenableFuture<Boolean> clientExists(@PathParam("clientname") String clientname);

   /**
    * @see ChefClient#getClient(String)
    */
   @GET
   @Path("/clients/{clientname}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Client> getClient(@PathParam("clientname") String clientname);

   /**
    * @see ChefClient#deleteClient(String)
    */
   @DELETE
   @Path("/clients/{clientname}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Client> deleteClient(@PathParam("clientname") String clientname);

   /**
    * @see ChefClient#listClients()
    */
   @GET
   @Path("/clients")
   @ResponseParser(ParseKeySetFromJson.class)
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> listClients();

   /**
    * @see ChefClient#createNode(Node)
    */
   @POST
   @Path("/nodes")
   ListenableFuture<Void> createNode(@BinderParam(BindToJsonPayload.class) Node node);

   /**
    * @see ChefClient#updateNode(Node)
    */
   @PUT
   @Path("/nodes/{nodename}")
   ListenableFuture<Node> updateNode(
            @PathParam("nodename") @ParamParser(NodeName.class) @BinderParam(BindToJsonPayload.class) Node node);

   /**
    * @see ChefClient#nodeExists(String)
    */
   @HEAD
   @Path("/nodes/{nodename}")
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   ListenableFuture<Boolean> nodeExists(@PathParam("nodename") String nodename);

   /**
    * @see ChefClient#getNode(String)
    */
   @GET
   @Path("/nodes/{nodename}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Node> getNode(@PathParam("nodename") String nodename);

   /**
    * @see ChefNode#deleteNode
    */
   @DELETE
   @Path("/nodes/{nodename}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Node> deleteNode(@PathParam("nodename") String nodename);

   /**
    * @see ChefClient#listNodes()
    */
   @GET
   @Path("/nodes")
   @ResponseParser(ParseKeySetFromJson.class)
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> listNodes();

   /**
    * @see ChefClient#createRole(Role)
    */
   @POST
   @Path("/roles")
   ListenableFuture<Void> createRole(@BinderParam(BindToJsonPayload.class) Role role);

   /**
    * @see ChefClient#updateRole(Role)
    */
   @PUT
   @Path("/roles/{rolename}")
   ListenableFuture<Role> updateRole(
            @PathParam("rolename") @ParamParser(RoleName.class) @BinderParam(BindToJsonPayload.class) Role role);

   /**
    * @see ChefClient#roleExists(String)
    */
   @HEAD
   @Path("/roles/{rolename}")
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   ListenableFuture<Boolean> roleExists(@PathParam("rolename") String rolename);

   /**
    * @see ChefClient#getRole(String)
    */
   @GET
   @Path("/roles/{rolename}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Role> getRole(@PathParam("rolename") String rolename);

   /**
    * @see ChefClient#deleteRole(String)
    */
   @DELETE
   @Path("/roles/{rolename}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Role> deleteRole(@PathParam("rolename") String rolename);

   /**
    * @see ChefClient#listRoles()
    */
   @GET
   @Path("/roles")
   @ResponseParser(ParseKeySetFromJson.class)
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> listRoles();

   /**
    * @see ChefClient#listDatabags()
    */
   @GET
   @Path("/data")
   @ResponseParser(ParseKeySetFromJson.class)
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> listDatabags();

   /**
    * @see ChefClient#createDatabag(String)
    */
   @POST
   @Path("/data")
   ListenableFuture<Void> createDatabag(@BinderParam(BindNameToJsonPayload.class) String databagName);

   /**
    * @see ChefClient#databagExists(String)
    */
   @HEAD
   @Path("/data/{name}")
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   ListenableFuture<Boolean> databagExists(@PathParam("name") String databagName);

   /**
    * @see ChefClient#deleteDatabag(String)
    */
   @DELETE
   @Path("/data/{name}")
   @ExceptionParser(ReturnVoidOnNotFoundOr404.class)
   ListenableFuture<Void> deleteDatabag(@PathParam("name") String databagName);

   /**
    * @see ChefClient#listDatabagItems(String)
    */
   @GET
   @Path("/data/{name}")
   @ResponseParser(ParseKeySetFromJson.class)
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> listDatabagItems(@PathParam("name") String databagName);

   /**
    * @see ChefClient#createDatabagItem(String, DatabagItem)
    */
   @POST
   @Path("/data/{databagName}")
   ListenableFuture<DatabagItem> createDatabagItem(@PathParam("databagName") String databagName,
            @BinderParam(BindToJsonPayload.class) DatabagItem databagItem);

   /**
    * @see ChefClient#updateDatabagItem(String, DatabagItem)
    */
   @PUT
   @Path("/data/{databagName}/{databagItemId}")
   ListenableFuture<DatabagItem> updateDatabagItem(
            @PathParam("databagName") String databagName,
            @PathParam("databagItemId") @ParamParser(DatabagItemId.class) @BinderParam(BindToJsonPayload.class) DatabagItem item);

   /**
    * @see ChefClient#databagItemExists(String, String)
    */
   @HEAD
   @Path("/data/{databagName}/{databagItemId}")
   @ExceptionParser(ReturnFalseOnNotFoundOr404.class)
   ListenableFuture<Boolean> databagItemExists(@PathParam("databagName") String databagName,
            @PathParam("databagItemId") String databagItemId);

   /**
    * @see ChefClient#getDatabagItem(String, String)
    */
   @GET
   @Path("/data/{databagName}/{databagItemId}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<DatabagItem> getDatabagItem(@PathParam("databagName") String databagName,
            @PathParam("databagItemId") String databagItemId);

   /**
    * @see ChefClient#deleteDatabagItem(String, String)
    */
   @DELETE
   @Path("/data/{databagName}/{databagItemId}")
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<DatabagItem> deleteDatabagItem(@PathParam("databagName") String databagName,
            @PathParam("databagItemId") String databagItemId);

   /**
    * @see ChefClient#listSearchIndexes()
    */
   @GET
   @Path("/search")
   @ResponseParser(ParseKeySetFromJson.class)
   @ExceptionParser(ReturnEmptySetOnNotFoundOr404.class)
   ListenableFuture<Set<String>> listSearchIndexes();

   /**
    * @see ChefClient#searchRoles()
    */
   @GET
   @Path("/search/role")
   @ResponseParser(ParseSearchRolesFromJson.class)
   ListenableFuture<? extends SearchResult<? extends Role>> searchRoles();

   /**
    * @see ChefClient#searchClients()
    */
   @GET
   @Path("/search/client")
   @ResponseParser(ParseSearchClientsFromJson.class)
   ListenableFuture<? extends SearchResult<? extends Client>> searchClients();

   /**
    * @see ChefClient#searchNodes()
    */
   @GET
   @Path("/search/node")
   @ResponseParser(ParseSearchNodesFromJson.class)
   ListenableFuture<? extends SearchResult<? extends Node>> searchNodes();

   /**
    * @see ChefClient#searchDatabag(String)
    */
   @GET
   @Path("/search/{databagName}")
   @ResponseParser(ParseSearchDatabagFromJson.class)
   ListenableFuture<? extends SearchResult<? extends DatabagItem>> searchDatabag(
            @PathParam("databagName") String databagName);
   
   /**
    * @see ChefClient#getResourceContents(Resource)
    */
   @GET
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<InputStream> getResourceContents(@EndpointParam(parser=UriForResource.class) Resource resource);
}
