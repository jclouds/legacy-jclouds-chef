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
package org.jclouds.chef.demo.domain;

/**
 * Metadata about a google appengine instance which is registered in chef.
 * 
 * @author Adrian Cole
 */
public class GoogleAppEngineNodeStatus {
   private final boolean isMe;
   private final String name;
   private final String runList;
   private final String applicationId;
   private final String environment;
   private final String runtimeVersion;
   private final String applicationVersion;
   private final String checkIn;

   public GoogleAppEngineNodeStatus(boolean isMe, String name, String runList, String applicationId,
            String environment, String runtimeVersion, String applicationVersion, String checkIn) {
      this.isMe = isMe;
      this.name = name;
      this.runList = runList;
      this.applicationId = applicationId;
      this.environment = environment;
      this.runtimeVersion = runtimeVersion;
      this.applicationVersion = applicationVersion;
      this.checkIn = checkIn;
   }

   public boolean isMe() {
      return isMe;
   }

   public String getCheckIn() {
      return checkIn;
   }

   public String getName() {
      return name;
   }

   public String getRunList() {
      return runList;
   }

   public String getApplicationId() {
      return applicationId;
   }

   public String getEnvironment() {
      return environment;
   }

   public String getRuntimeVersion() {
      return runtimeVersion;
   }

   public String getApplicationVersion() {
      return applicationVersion;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
      result = prime * result + ((applicationVersion == null) ? 0 : applicationVersion.hashCode());
      result = prime * result + ((checkIn == null) ? 0 : checkIn.hashCode());
      result = prime * result + ((environment == null) ? 0 : environment.hashCode());
      result = prime * result + (isMe ? 1231 : 1237);
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((runList == null) ? 0 : runList.hashCode());
      result = prime * result + ((runtimeVersion == null) ? 0 : runtimeVersion.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      GoogleAppEngineNodeStatus other = (GoogleAppEngineNodeStatus) obj;
      if (applicationId == null) {
         if (other.applicationId != null)
            return false;
      } else if (!applicationId.equals(other.applicationId))
         return false;
      if (applicationVersion == null) {
         if (other.applicationVersion != null)
            return false;
      } else if (!applicationVersion.equals(other.applicationVersion))
         return false;
      if (checkIn == null) {
         if (other.checkIn != null)
            return false;
      } else if (!checkIn.equals(other.checkIn))
         return false;
      if (environment == null) {
         if (other.environment != null)
            return false;
      } else if (!environment.equals(other.environment))
         return false;
      if (isMe != other.isMe)
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (runList == null) {
         if (other.runList != null)
            return false;
      } else if (!runList.equals(other.runList))
         return false;
      if (runtimeVersion == null) {
         if (other.runtimeVersion != null)
            return false;
      } else if (!runtimeVersion.equals(other.runtimeVersion))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "[applicationId=" + applicationId + ", applicationVersion=" + applicationVersion + ", checkIn=" + checkIn
               + ", environment=" + environment + ", isMe=" + isMe + ", name=" + name + ", runList=" + runList
               + ", runtimeVersion=" + runtimeVersion + "]";
   }

}