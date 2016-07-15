package com.my.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Super Small Logger Utility
 * 
 * License: MIT
 * 
 * @author Manet Yim
 * @version 1.0
 */
public class Lilog {

	private static final int LOG_ALL 	= 0; // aggressive trace, log everything
	private static final int LOG_DEBUG 	= 1; // debug message or higher
	private static final int LOG_INFO 	= 2; // info message or higher
	private static final int LOG_WARN 	= 3; // warn message or higher	
	private static final int LOG_ERROR 	= 4; // error message or higher
	private static final int LOG_FATAL 	= 5; // fatal message or higher
	private static final int LOG_OFF 	= 6; // do not log any message
	
	private static final String LOG_LEVEL_CONFIG_KEY = "logLevel";
	private static final String LOG_FILE_CONFIG_KEY  = "logFile";
	
	private static final int MAX_LABEL_LENGTH = 5; // for pretty log level label

	private static final Integer defaultLogLevel = LOG_DEBUG; // default level
	
	/**
	 * Set logLevel using VM argument with number representing level.
	 * Example: -DlogLevel=1
	 * If logLevel is not set, defaultLogLevel is used.
	 */
	public static Integer logLevel = (System.getProperty(LOG_LEVEL_CONFIG_KEY) != null
			? new Integer(System.getProperty(LOG_LEVEL_CONFIG_KEY)) : defaultLogLevel);
	
	/**
	 * Set logFile using VM argument with number representing level.
	 * Example: -DlogFile=light.log
	 * If logFile is not set, no file will be created.
	 */
	public static String logFile = System.getProperty(LOG_FILE_CONFIG_KEY);
	
	private static PrintStream logFileWriter = null;

	/**
	 * Alias method to log with level and message
	 * @param level
	 * @param message
	 */
	public static void log(int level, final String message) {
		switch (level) {
		case LOG_ALL: 	log(message); 	break;
		case LOG_DEBUG: debug(message); break;
		case LOG_INFO: 	info(message); 	break;
		case LOG_WARN: 	warn(message); 	break;
		case LOG_ERROR: error(message);	break;
		case LOG_FATAL: fatal(message);	break;
		default: log(message); break;
		}
	}

	/**
	 * Breaking the rule, log freely without level check except "OFF"
	 * @param message
	 */
	public static void log(final String message) {
		sop(LOG_ALL, message);
	}

	/**
	 * Log debug message
	 * @param message
	 */
	public static void debug(final String message) {
		sop(LOG_DEBUG, message);
	}

	/**
	 * Log info message
	 * @param message
	 */
	public static void info(final String message) {
		sop(LOG_INFO, message);
	}

	/**
	 * Log warning message
	 * @param message
	 */
	public static void warn(final String message) {
		sop(LOG_WARN, message);
	}

	/**
	 * Log error message
	 * @param message
	 */
	public static void error(final String message) {
		sop(LOG_ERROR, message);
	}
	
	/**
	 * Log fatal message
	 * @param message
	 */
	public static void fatal(final String message) {
		sop(LOG_FATAL, message);
	}

	/**
	 * Printing using System.out.println
	 * @param level
	 * @param msg
	 */
	private static void sop(int level, final String msg) {
		Integer checkLevel = (logLevel != null ? logLevel : defaultLogLevel);
		if (level == LOG_OFF || level < checkLevel.intValue()) {
			return;
		}
		LogRecord record = buildLogRecord(level, msg);
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
		sb.append(sdf.format(new Date())).append(" ");
		sb.append("[").append(prettyLevel(level)).append("] ");
		sb.append("[").append(record.getCallerClass());
		sb.append(".").append(record.getMethod()).append("()");
		sb.append(":").append(record.getLine()).append("] ");
		sb.append(record.getMessage());
		System.out.println(sb);
		if (logFile != null) {
			appendable(logFile);
			logFileWriter.println(sb);
		}
	}
	
	/**
	 * Make log file appendable or start a new file if not yet existed
	 * @param fileName
	 */
	private static void appendable(String fileName){
		if (logFileWriter == null){
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
					if (element.getClassName() != null 
						&& !element.getClassName().equals(Lilog.class.getName())) {
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
	 * @param level
	 * @return StringBuffer of level pretty text
	 */
	private static StringBuffer prettyLevel(int level) {
		StringBuffer text = null;
		switch (level) {
		case LOG_ALL:   text = new StringBuffer("LOG"); break;
		case LOG_DEBUG: text = new StringBuffer("DEBUG"); break;
		case LOG_INFO:  text = new StringBuffer("INFO"); break;
		case LOG_WARN:  text = new StringBuffer("WARN"); break;
		case LOG_ERROR: text = new StringBuffer("ERROR"); break;
		case LOG_FATAL: text = new StringBuffer("FATAL"); break;
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
		String  callerClass;
		String  method;
		Integer line;
		String  message;

		// recycling object in the pool
		static LogRecord instance = null;
		static LogRecord getInstance(){
			if (instance == null){
				return new LogRecord();
			} else {
				return instance;
			}
		}
		
		LogRecord(){
			this.reset();
		}
		
		void reset(){
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

		public void setLine(int line) {
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