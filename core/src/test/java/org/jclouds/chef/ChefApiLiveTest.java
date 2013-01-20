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

import static org.jclouds.reflect.Reflection2.typeToken;
import static org.testng.Assert.assertNotNull;

import org.jclouds.chef.domain.CookbookVersion;
import org.jclouds.chef.internal.BaseChefApiLiveTest;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;

/**
 * Tests behavior of {@code ChefApi} against a Chef Server <= 0.9.8.
 * 
 * @author Adrian Cole
 */
@Test(groups = { "live" })
public class ChefApiLiveTest extends BaseChefApiLiveTest<ChefContext> {

   @Test
   public void testListCookbookVersionsWithChefService() throws Exception {
      Iterable<? extends CookbookVersion> cookbooks = context.getChefService().listCookbookVersions();
      assertNotNull(cookbooks);
   }

   @Override
   protected ChefApi getChefApi(ChefContext context) {
      return context.getApi();
   }

   @Override
   protected TypeToken<ChefContext> contextType() {
      return typeToken(ChefContext.class);
   }

}
