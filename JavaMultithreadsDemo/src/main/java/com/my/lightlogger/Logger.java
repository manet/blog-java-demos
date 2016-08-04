package com.my.lightlogger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Super Small Logger Utility
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 Manet Yim
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.F
 * 
 * @author Manet Yim
 * 
 * @version 1.1
 */
public class Logger {
	
	private static enum Level {
		ALL, 	// aggressive trace, log everything
		TRACE, 	// trace message or higher
		DEBUG, 	// debug message or higher
		INFO, 	// info message or higher
		WARN, 	// warn message or higher
		ERROR, 	// error message or higher
		FATAL, 	// fatal message or higher
		OFF		// silent please
	}

	private static final String LOG_LEVEL_CONFIG_KEY = "logLevel";
	private static final String LOG_FILE_CONFIG_KEY  = "logFile";
	private static final int 	MAX_LABEL_LENGTH 	 = 5; // for pretty log level label

	private static final Integer defaultLogLevel = Level.DEBUG.ordinal(); // default level

	/**
	 * Set logLevel using VM argument with number representing level. Example:
	 * -DlogLevel=1 If logLevel is not set, defaultLogLevel is used.
	 */
	public static Integer logLevel = (System.getProperty(LOG_LEVEL_CONFIG_KEY) != null
			? new Integer(System.getProperty(LOG_LEVEL_CONFIG_KEY)) : defaultLogLevel);

	/**
	 * Set logFile using VM argument with number representing level. Example:
	 * -DlogFile=light.log If logFile is not set, no file will be created.
	 */
	public static String logFile = System.getProperty(LOG_FILE_CONFIG_KEY);

	private static PrintStream logFileWriter = null;

	/**
	 * Alias method to log with level and message
	 * 
	 * @param level
	 * @param message
	 */
	public static void log(Level logLevel, final String message){
		switch(logLevel){
		case ALL: 	log(message); 	break;
		case TRACE:	trace(message); break;
		case DEBUG: debug(message); break;
		case INFO: 	info(message); 	break;
		case WARN: 	warn(message); 	break;
		case ERROR: error(message);	break;
		case FATAL: fatal(message);	break;
		default: log(message); break;
		}
	}

	/**
	 * Breaking the rule, log freely without level check except "OFF"
	 * 
	 * @param message
	 */
	public static void log(final String message) {
		output(Level.ALL, message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void log(final Object message) {
		output(Level.ALL, message.toString());
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void trace(final String message) {
		output(Level.TRACE, message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void trace(final Object message) {
		output(Level.TRACE, message.toString());
	}

	/**
	 * Log debug message
	 * 
	 * @param message
	 */
	public static void debug(final String message) {
		output(Level.DEBUG, message);
	}
	
	/**
	 * Log debug message
	 * 
	 * @param message
	 */
	public static void debug(final Object message) {
		output(Level.DEBUG, message.toString());
	}

	/**
	 * Log info message
	 * 
	 * @param message
	 */
	public static void info(final String message) {
		output(Level.INFO, message);
	}
	
	/**
	 * Log info message
	 * 
	 * @param message
	 */
	public static void info(final Object message) {
		output(Level.INFO, message.toString());
	}

	/**
	 * Log warning message
	 * 
	 * @param message
	 */
	public static void warn(final String message) {
		output(Level.WARN, message);
	}
	
	/**
	 * Log warning message
	 * 
	 * @param message
	 */
	public static void warn(final Object message) {
		output(Level.WARN, message.toString());
	}

	/**
	 * Log error message
	 * 
	 * @param message
	 */
	public static void error(final String message) {
		output(Level.ERROR, message);
	}
	
	/**
	 * Log error message
	 * 
	 * @param message
	 */
	public static void error(final Object message) {
		output(Level.ERROR, message.toString());
	}

	/**
	 * Log fatal message
	 * 
	 * @param message
	 */
	public static void fatal(final String message) {
		output(Level.FATAL, message);
	}
	
	/**
	 * Log fatal message
	 * 
	 * @param message
	 */
	public static void fatal(final Object message) {
		output(Level.FATAL, message.toString());
	}

	/**
	 * Printing using System.out.println
	 * 
	 * @param level
	 * @param msg
	 */
	private static void output(Level level, final String msg) {
		Integer checkLevel = (logLevel != null ? logLevel : defaultLogLevel);
		if (level == Level.OFF || level.ordinal() < checkLevel.intValue() ){
			return;
		}
		LogRecord record = buildLogRecord(level.ordinal(), msg);
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
		sb.append(sdf.format(new Date())).append(" ");
		sb.append("[").append(prettyLevel(level)).append("] ");
		sb.append("[").append(record.getCallerClass());
		sb.append(".").append(record.getMethod()).append("()");
		sb.append(":").append(record.getLine()).append("] ");
		sb.append(record.getMessage());
		if (level.ordinal() >= Level.ERROR.ordinal() && level.ordinal() < Level.OFF.ordinal()){
			System.err.println(sb);
		} else {
			System.out.println(sb);
		}
		if (logFile != null) {
			appendable(logFile);
			logFileWriter.println(sb);
		}
	}

	/**
	 * Make log file appendable or start a new file if not yet existed
	 * 
	 * @param fileName
	 */
	private static void appendable(String fileName) {
		if (logFileWriter == null) {
			try {
				logFileWriter = new PrintStream(new FileOutputStream(fileName, true));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Magic happens here. Construction log record and automatically discover
	 * caller class name and line number where log message was sent.
	 * 
	 * @param level
	 * @param msg
	 * @return LogRecord
	 */
	private static LogRecord buildLogRecord(int level, String msg) {
		LogRecord lr = LogRecord.getInstance();
		lr.reset();
		lr.setLevel(level);
		lr.setMessage(msg);
		try {
			Throwable t = new Throwable();
			StackTraceElement[] elements = t.getStackTrace();
			if (elements != null && elements.length > 0) {
				for (StackTraceElement element : elements) {
					lr.setCallerClass(element.getClassName());
					lr.setMethod(element.getMethodName());
					lr.setLine(element.getLineNumber());
					if (element.getClassName() != null && !element.getClassName().equals(Logger.class.getName())) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lr;
	}

	/**
	 * Make very log level looks clean and pretty
	 * 
	 * @param level
	 * @return StringBuffer of level pretty text
	 */
	private static StringBuffer prettyLevel(Level level) {
		StringBuffer text = null;
		switch (level) {
		case ALL:   text = new StringBuffer("LOG"); break;
		case TRACE: text = new StringBuffer("TRACE"); break;
		case DEBUG: text = new StringBuffer("DEBUG"); break;
		case INFO:  text = new StringBuffer("INFO"); break;
		case WARN:  text = new StringBuffer("WARN"); break;
		case ERROR: text = new StringBuffer("ERROR"); break;
		case FATAL: text = new StringBuffer("FATAL"); break;
		default: text = new StringBuffer("LOG"); break;
		}
		if (text.length() < MAX_LABEL_LENGTH) {
			int gap = MAX_LABEL_LENGTH - text.length();
			for (int x = 0; x < gap; x++) {
				text = text.append(" ");
			}
		}
		return text;
	}

	/**
	 * Bean instance for storing magical information
	 */
	static class LogRecord {

		Integer level;
		String callerClass;
		String method;
		Integer line;
		String message;

		// recycling object in the pool
		static LogRecord instance = null;

		static LogRecord getInstance() {
			if (instance == null) {
				return new LogRecord();
			} else {
				return instance;
			}
		}

		LogRecord() {
			this.reset();
		}

		void reset() {
			this.level = null;
			this.callerClass = null;
			this.method = null;
			this.line = null;
			this.message = null;
		}

		int getLevel() {
			return level;
		}

		void setLevel(int level) {
			this.level = level;
		}

		String getCallerClass() {
			return callerClass;
		}

		void setCallerClass(String callerClass) {
			this.callerClass = callerClass;
		}

		String getMethod() {
			return method;
		}

		void setMethod(String method) {
			this.method = method;
		}

		int getLine() {
			return line;
		}

		void setLine(int line) {
			this.line = line;
		}

		String getMessage() {
			return message;
		}

		void setMessage(String message) {
			this.message = message;
		}
	}
}
