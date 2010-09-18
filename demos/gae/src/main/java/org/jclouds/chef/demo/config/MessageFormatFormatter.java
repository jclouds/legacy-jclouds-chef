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