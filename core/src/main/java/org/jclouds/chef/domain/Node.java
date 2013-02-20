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
package org.jclouds.chef.domain;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import org.jclouds.domain.JsonBall;
import org.jclouds.javax.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Sandbox object.
 * 
 * @author Adrian Cole
 */
public class Node {

   private String name;
   private Map<String, JsonBall> normal = Maps.newLinkedHashMap();
   private Map<String, JsonBall> override = Maps.newLinkedHashMap();
   @SerializedName("default")
   private Map<String, JsonBall> defaultA = Maps.newLinkedHashMap();
   private Map<String, JsonBall> automatic = Maps.newLinkedHashMap();
   @SerializedName("run_list")
   private List<String> runList = Lists.newArrayList();

   /**
    * @since chef 0.10
    */
   @SerializedName("chef_environment")
   @Nullable
   private String chefEnvironment;

   // internal
   @SerializedName("json_class")
   private String _jsonClass = "Chef::Node";


    @SerializedName("chef_type")
    private String _chefType = "node";

   public Node(String name, Map<String, JsonBall> normal, Map<String, JsonBall> override,
         Map<String, JsonBall> defaultA, Map<String, JsonBall> automatic, Iterable<String> runList) {
      this(name, normal, override, defaultA, automatic, runList, null);
   }

   /**
    * @since chef 0.10
    */
   public Node(String name, Map<String, JsonBall> normal, Map<String, JsonBall> override,
         Map<String, JsonBall> defaultA, Map<String, JsonBall> automatic, Iterable<String> runList,
         String chefEnvironment) {
      this.name = name;
      this.chefEnvironment = chefEnvironment;
      this.normal.putAll(normal);
      this.override.putAll(override);
      this.defaultA.putAll(defaultA);
      this.automatic.putAll(automatic);
      Iterables.addAll(this.runList, runList);
   }

   @Override
   public String toString() {
      return "Node [name=" + name + ", runList=" + runList + ", normal=" + normal + ", default=" + defaultA
            + ", override=" + override + ", chefEnvironment=" + chefEnvironment + ", automatic=" + automatic + "]";
   }

   public Node(String name, Iterable<String> runList) {
      this(name, runList, "_default");
   }

   /**
    * @since chef 0.10
    */
   public Node(String name, Iterable<String> runList, String chefEnvironment) {
      this.name = name;
      this.chefEnvironment = chefEnvironment;
      Iterables.addAll(this.runList, runList);
   }

   // hidden but needs to be here for json deserialization to work
   Node() {

   }

   public String getName() {
      return name;
   }

   public Map<String, JsonBall> getNormal() {
      return normal;
   }

   public Map<String, JsonBall> getOverride() {
      return override;
   }

   public Map<String, JsonBall> getDefault() {
      return defaultA;
   }

   public Map<String, JsonBall> getAutomatic() {
      return automatic;
   }

   public List<String> getRunList() {
      return runList;
   }

   /**
    * @since chef 0.10
    */
   public String getChefEnvironment() {
      return chefEnvironment;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((_chefType == null) ? 0 : _chefType.hashCode());
      result = prime * result + ((_jsonClass == null) ? 0 : _jsonClass.hashCode());
      result = prime * result + ((automatic == null) ? 0 : automatic.hashCode());
      result = prime * result + ((defaultA == null) ? 0 : defaultA.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((normal == null) ? 0 : normal.hashCode());
      result = prime * result + ((override == null) ? 0 : override.hashCode());
      result = prime * result + ((runList == null) ? 0 : runList.hashCode());
      result = prime * result + ((chefEnvironment == null) ? 0 : chefEnvironment.hashCode());
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
      Node other = (Node) obj;
      if (_chefType == null) {
         if (other._chefType != null)
            return false;
      } else if (!_chefType.equals(other._chefType))
         return false;
      if (_jsonClass == null) {
         if (other._jsonClass != null)
            return false;
      } else if (!_jsonClass.equals(other._jsonClass))
         return false;
      if (automatic == null) {
         if (other.automatic != null)
            return false;
      } else if (!automatic.equals(other.automatic))
         return false;
      if (defaultA == null) {
         if (other.defaultA != null)
            return false;
      } else if (!defaultA.equals(other.defaultA))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (normal == null) {
         if (other.normal != null)
            return false;
      } else if (!normal.equals(other.normal))
         return false;
      if (override == null) {
         if (other.override != null)
            return false;
      } else if (!override.equals(other.override))
         return false;
      if (runList == null) {
         if (other.runList != null)
            return false;
      } else if (!runList.equals(other.runList))
         return false;
      if (chefEnvironment == null) {
         if (other.chefEnvironment != null)
            return false;
      } else if (!chefEnvironment.equals(other.chefEnvironment))
         return false;
      return true;
   }

}
