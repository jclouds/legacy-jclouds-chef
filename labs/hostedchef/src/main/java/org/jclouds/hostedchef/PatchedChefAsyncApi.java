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

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Constants;
import org.jclouds.Fallbacks.FalseOnNotFoundOr404;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.ChefAsyncApi;
import org.jclouds.chef.filters.SignedHeaderAuth;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.Headers;
import org.jclouds.rest.annotations.RequestFilters;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Private chef api seems to miss support for HEAD method in the node resource.
 * This class overrides the {@link ChefAsyncApi#nodeExists(String)} method to
 * use GET instead of HEAD.
 * 
 * @author Ignasi Barrera
 * @deprecated {@link PatchedChefAsyncApi} interface will be removed in jclouds 1.7.
 */
@Deprecated
@RequestFilters(SignedHeaderAuth.class)
@Consumes(MediaType.APPLICATION_JSON)
@Headers(keys = "X-Chef-Version", values = "{" + Constants.PROPERTY_API_VERSION + "}")
public interface PatchedChefAsyncApi extends ChefAsyncApi {
   /**
    * @see ChefApi#nodeExists(String)
    */
   @Override
   // Use get instead of HEAD
   @Named("node:exists")
   @GET
   @Path("/nodes/{nodename}")
   @Fallback(FalseOnNotFoundOr404.class)
   ListenableFuture<Boolean> nodeExists(@PathParam("nodename") String nodename);
}
