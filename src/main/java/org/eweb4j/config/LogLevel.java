package org.eweb4j.config;

public class LogLevel {
	public static String level(int level) {
		switch (level) {
		case 0:
			return "OFF";
		case 1:
			return "INFO";
		case 2:
			return "DEBUG";
		case 3:
			return "WARN";
		case 4:
			return "ERROR";
		case 5:
			return "FATAL";
		default:
			return "INFO";
		}
	}

	public static int level(String level) {
		if ("OFF".equalsIgnoreCase(level))
			return 0;
		if ("INFO".equalsIgnoreCase(level))
			return 1;
		if ("DEBUG".equalsIgnoreCase(level))
			return 2;
		if ("WARN".equalsIgnoreCase(level))
			return 3;
		if ("ERROR".equalsIgnoreCase(level))
			return 4;
		if ("FATAL".equalsIgnoreCase(level))
			return 5;

		return 1;
	}
}
