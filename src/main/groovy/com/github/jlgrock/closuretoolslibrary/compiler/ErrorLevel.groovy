package com.github.jlgrock.closuretoolslibrary.compiler


/**
 * The enumerated Google warning levels.
 */
enum ErrorLevel {
	/**
	 * The possible error levels.
	 */
	SIMPLE('SIMPLE'), WARNING('WARNING'), STRICT('STRICT')
	
	/**
	 * The string that identifies the error level.
	 */
	private final String name
	
	/**
	 * A hashmap used for lookup.
	 */
	private static final Map<String, ErrorLevel> LEVELS
	
	static {
		LEVELS = [:]
		for (ErrorLevel level : values()) {
			LEVELS.put(level.name, level)
		}
	}
	/**
	 * The enum constructor.
	 * @param nameIn the name of the enumeration
	 */
	ErrorLevel(final String nameIn) {
		name = nameIn
	}
	
	/**
	 * Get the compile level by looking up via the name.
	 * @param nameIn the name
	 * @return the enumeration level
	 */
	static final ErrorLevel getCompileLevelByName(final String nameIn) {
		LEVELS.get(nameIn)
	}
}
