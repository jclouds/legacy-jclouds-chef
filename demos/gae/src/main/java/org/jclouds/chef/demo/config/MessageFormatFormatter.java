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
package org.jclouds.chef.demo.config;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.google.appengine.repackaged.com.google.common.base.Throwables;

public class MessageFormatFormatter extends Formatter {

   private static final String FORMAT = "{0,date,hh:mm:ss.SSS} {3}[{1}|{2}]: {4} \n";
   private static final MessageFormat messageFormat = new MessageFormat(FORMAT);
   private static final MessageFormat thrownFormat = new MessageFormat(FORMAT + "{5}\n");

   public MessageFormatFormatter() {

      super();

   }

   @Override
   public String format(LogRecord record) {

      Object[] arguments = new Object[6];

      arguments[0] = new Date(record.getMillis());

      arguments[1] = record.getLevel();

      arguments[3] = record.getLoggerName();

      arguments[2] = Thread.currentThread().getName();

      arguments[4] = record.getMessage();

      arguments[5] = record.getThrown() != null ? Throwables.getStackTraceAsString(record.getThrown()) : null;

      return arguments[5] == null ? messageFormat.format(arguments) : thrownFormat.format(arguments);

   }

}