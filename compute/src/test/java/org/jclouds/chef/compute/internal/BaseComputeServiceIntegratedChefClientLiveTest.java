/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, ComputeServiceContextersion 2.0 (the
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
package org.jclouds.chef.compute.internal;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.apis.BaseViewLiveTest;
import org.jclouds.chef.ChefContext;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.TemplateBuilderSpec;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.domain.LoginCredentials.Builder;
import org.jclouds.io.CopyInputStreamInputSupplierMap;
import org.jclouds.rest.config.CredentialStoreModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.testng.annotations.AfterClass;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import com.google.inject.Module;

/**
 * @author Adrian Cole
 */
public abstract class BaseComputeServiceIntegratedChefClientLiveTest
		extends
			BaseViewLiveTest<ChefContext> {

	protected TemplateBuilderSpec template;
	protected LoginCredentials loginCredentials = LoginCredentials.builder()
			.user("root").build();

	// isolate tests from eachother, as default credentialStore is static
	protected Module credentialStoreModule = new CredentialStoreModule(
			new CopyInputStreamInputSupplierMap(
					new ConcurrentHashMap<String, InputSupplier<InputStream>>()));

	private String computeProvider;

	protected Properties setupComputeProperties() {
		Properties overrides = new Properties();
		overrides.setProperty(Constants.PROPERTY_TRUST_ALL_CERTS, "true");
		overrides.setProperty(Constants.PROPERTY_RELAX_HOSTNAME, "true");
		computeProvider = setIfTestSystemPropertyPresent(overrides, provider
				+ ".compute.provider");
		setIfTestSystemPropertyPresent(overrides, provider
				+ ".compute.identity");
		setIfTestSystemPropertyPresent(overrides, provider
				+ ".compute.credential");
		setIfTestSystemPropertyPresent(overrides, provider
				+ ".compute.endpoint");
		setIfTestSystemPropertyPresent(overrides, provider
				+ ".compute.api-version");
		setIfTestSystemPropertyPresent(overrides, provider
				+ ".compute.build-version");
		String spec = setIfTestSystemPropertyPresent(overrides, provider
				+ ".compute.template");
		if (spec != null) {
			template = TemplateBuilderSpec.parse(spec);
			if (template.getLoginUser() != null) {
				Iterable<String> userPass = Splitter.on(':').split(
						template.getLoginUser());
				Builder loginCredentialsBuilder = LoginCredentials.builder();
				loginCredentialsBuilder.user(Iterables.get(userPass, 0));
				if (Iterables.size(userPass) == 2)
					loginCredentialsBuilder
							.password(Iterables.get(userPass, 1));
				if (template.getAuthenticateSudo() != null)
					loginCredentialsBuilder.authenticateSudo(template
							.getAuthenticateSudo());
				loginCredentials = loginCredentialsBuilder.build();
			}
		}
		return overrides;
	}

	@Override
	protected Iterable<Module> setupModules() {
		return ImmutableSet.<Module> of(getLoggingModule(),
				credentialStoreModule, getSshModule());
	}

	protected Module getSshModule() {
		return new SshjSshClientModule();
	}

	protected volatile ComputeServiceContext computeContext;

	@Override
	protected void initializeContext() {
		super.initializeContext();
		computeContext = ContextBuilder.newBuilder(computeProvider)
				.modules(setupModules()).overrides(setupComputeProperties())
				.buildView(ComputeServiceContext.class);
	}

	@AfterClass(groups = {"integration", "live"})
	@Override
	protected void tearDownContext() {
		Closeables.closeQuietly(computeContext);
		super.tearDownContext();
	}
}
