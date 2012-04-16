package org.jclouds.opscodeplatform;

import java.net.URI;
import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.chef.ChefApiMetadata;
import org.jclouds.chef.ChefAsyncClient;
import org.jclouds.ohai.config.JMXOhaiModule;
import org.jclouds.opscodeplatform.functions.OpscodePlatformRestClientModule;
import org.jclouds.rest.RestContext;
import org.jclouds.rest.internal.BaseRestApiMetadata;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for Opscode Platform api.
 * 
 * @author Adrian Cole
 */
public class OpscodePlatformApiMetadata extends BaseRestApiMetadata {

   /** The serialVersionUID */
   private static final long serialVersionUID = 3450830053589179249L;

   public static final TypeToken<RestContext<OpscodePlatformClient, OpscodePlatformAsyncClient>> CONTEXT_TOKEN = new TypeToken<RestContext<OpscodePlatformClient, OpscodePlatformAsyncClient>>() {
      private static final long serialVersionUID = -5070937833892503232L;
   };

   @Override
   public Builder toBuilder() {
      return new Builder(getApi(), getAsyncApi()).fromApiMetadata(this);
   }

   public OpscodePlatformApiMetadata() {
      this(new Builder(OpscodePlatformClient.class, OpscodePlatformAsyncClient.class));
   }

   protected OpscodePlatformApiMetadata(Builder builder) {
      super(Builder.class.cast(builder));
   }

   public static Properties defaultProperties() {
      Properties properties = ChefApiMetadata.defaultProperties();
      return properties;
   }

   public static class Builder extends BaseRestApiMetadata.Builder {

      protected Builder(Class<?> client, Class<?> asyncClient) {
         super(client, asyncClient);
         id("chef")
         .name("Opscode Platform Api")
         .identityName("User")
         .credentialName("Certificate")
         .version(ChefAsyncClient.VERSION)
         .documentation(URI.create("http://help.opscode.com/kb/api"))
         .defaultEndpoint("https://api.opscode.com")
         .defaultProperties(OpscodePlatformApiMetadata.defaultProperties())
         .context(TypeToken.of(OpscodePlatformContext.class))
         .defaultModules(ImmutableSet.<Class<? extends Module>>of(OpscodePlatformRestClientModule.class, JMXOhaiModule.class));
      }

      @Override
      public OpscodePlatformApiMetadata build() {
         return new OpscodePlatformApiMetadata(this);
      }
      
      @Override
      public Builder fromApiMetadata(ApiMetadata in) {
         super.fromApiMetadata(in);
         return this;
      }
   }

}