package com.github.jlgrock.closuretoolslibrary.testing.processors

/**
 * Possible results of tests.
 */
enum ResultType {
	
	/**
	 * Possible results of tests.
	 */
	PASSED('PASSED'), FAILED('FAILED'), TIMED_OUT, UNABLE_TO_EXECUTE, BAD_OUTPUT, SCRIPT_ERROR
	
	/**
	 * The string equivalent.
	 */
	String name

	
	/**
	 * A map that allows for searching by name.
	 */
	private static final Map<String, ResultType> MAP_BY_NAME

	static {
		MAP_BY_NAME = [:]
		for (ResultType type : values()) {
			if (type.name != null) {
				MAP_BY_NAME.put(type.name, type)
			}
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param nameIn the string to match the enumeration type
	 */
	ResultType(final String nameIn) {
		this.name = nameIn
	}
	
	/**
	 * Constructor.  Used for objects that don't take strings.
	 */
	ResultType() {
	}

	/**
	 * Do a lookup of the enumeration by the name string.
	 * 
	 * @param name
	 *            the name string value
	 * @return the enumerated packaging type
	 */
	static ResultType getByName(final String name) {
		MAP_BY_NAME.get(name)
	}

}
