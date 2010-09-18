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
package org.jclouds.chef.demo.integration;

import java.io.IOException;

import com.google.appengine.tools.KickStart;

/**
 * Basic functionality to start a local google app engine instance.
 * 
 * @author Adrian Cole
 */
public class GoogleDevServer {

   Thread server;

   public void startServer(final String address, final String port, final String warfile) throws IOException,
         InterruptedException {
      this.server = new Thread(new Runnable() {
         public void run() {
            KickStart.main(new String[] { "com.google.appengine.tools.development.DevAppServerMain",
                  "--jvm_flag=-Dtask_queue.disable_auto_task_execution=true", "--disable_update_check", "-a", address,
                  "-p", port, warfile });
         }

      });
      server.start();
      Thread.sleep(60 * 1000);
   }

   @SuppressWarnings("deprecation")
   public void stop() throws Exception {
      server.stop();
   }

}