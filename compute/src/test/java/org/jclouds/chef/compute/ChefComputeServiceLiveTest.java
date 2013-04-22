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
package org.jclouds.chef.compute;

import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.getLast;
import static org.jclouds.chef.predicates.CookbookVersionPredicates.containsRecipe;
import static org.jclouds.chef.predicates.CookbookVersionPredicates.containsRecipes;
import static org.jclouds.compute.options.TemplateOptions.Builder.runScript;
import static org.jclouds.reflect.Reflection2.typeToken;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.jclouds.chef.ChefApi;
import org.jclouds.chef.ChefAsyncApi;
import org.jclouds.chef.ChefContext;
import org.jclouds.chef.compute.internal.BaseComputeServiceIntegratedChefClientLiveTest;
import org.jclouds.chef.domain.CookbookVersion;
import org.jclouds.chef.util.RunListBuilder;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.predicates.NodePredicates;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.util.Strings2;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", testName = "ChefComputeServiceLiveTest")
public class ChefComputeServiceLiveTest
		extends
			BaseComputeServiceIntegratedChefClientLiveTest {

	private String group;
	private String clientName;
	private Iterable<? extends NodeMetadata> nodes;

	@Test
	public void testCanUpdateRunList() throws IOException {
		String recipe = "apache2";

		Iterable<? extends CookbookVersion> cookbookVersions = view
				.getChefService().listCookbookVersions();

		if (any(cookbookVersions, containsRecipe(recipe))) {
			List<String> runList = new RunListBuilder().addRecipe(recipe)
					.build();
			view.getChefService().updateRunListForGroup(runList, group);
			assertEquals(view.getChefService().getRunListForGroup(group),
					runList);
		} else {
			assert false : String.format("recipe %s not in %s", recipe,
					cookbookVersions);
		}

		// TODO move this to a unit test
		assert any(cookbookVersions, containsRecipe("apache2::mod_proxy"));
		assert any(
				cookbookVersions,
				containsRecipes("apache2", "apache2::mod_proxy",
						"apache2::mod_proxy_http"));
		assert !any(cookbookVersions, containsRecipe("apache2::bar"));
		assert !any(cookbookVersions, containsRecipe("foo::bar"));
	}

	@Test(dependsOnMethods = "testCanUpdateRunList")
	public void testRunNodesWithBootstrap() throws IOException {

		Statement bootstrap = view.getChefService()
				.createBootstrapScriptForGroup(group);

		try {
			nodes = computeContext.getComputeService().createNodesInGroup(
					group, 1, runScript(bootstrap));
		} catch (RunNodesException e) {
			nodes = concat(e.getSuccessfulNodes(), e.getNodeErrors().keySet());
		}

		for (NodeMetadata node : nodes) {
			URI uri = URI
					.create("http://" + getLast(node.getPublicAddresses()));
			InputStream content = computeContext.utils().http().get(uri);
			String string = Strings2.toStringAndClose(content);
			assert string.indexOf("It works!") >= 0 : string;
		}

	}

	@AfterClass(groups = {"integration", "live"})
	@Override
	protected void tearDownContext() {
		if (computeContext != null)
			computeContext.getComputeService().destroyNodesMatching(
					NodePredicates.inGroup(group));
		if (context != null) {
		   view.getChefService()
					.cleanupStaleNodesAndClients(group + "-", 1);
		   ChefApi api = view.unwrap(new TypeToken<org.jclouds.rest.RestContext<ChefApi, ChefAsyncApi>>() {
         }).getApi();
			if (clientName != null && api.clientExists(clientName))
				api.deleteClient(clientName);
			context.close();
		}
		super.tearDownContext();
	}

   @Override
   protected TypeToken<ChefContext> viewType() {
      return typeToken(ChefContext.class);
   }

}
