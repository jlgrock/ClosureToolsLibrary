package com.github.jlgrock.closuretoolslibrary.compiler

/**
 * The possible inclusion strategies for this plugin.
 */
enum InclusionStrategy {
	/**
	 * The possible inclusion strategies.
	 */
    ALL('all'), WHEN_IN_SRCS('when_in_srcs')
    
    /**
     * The type string.
     */
    String type
    
    /**
     * The hashmap used for lookups.
     */
    static final Map<String, InclusionStrategy> TYPES
    
    static {
    	TYPES = [:]
    	for (InclusionStrategy t : values()) {
    		TYPES.put(t.type, t)
    	}
    }
    
    /**
     * The private constructor.
     * @param typeIn the type used for lookups
     */
    InclusionStrategy(final String typeIn) {
    	this.type = typeIn
    }
    
    /**
     * Lookup the inclusion strategy by the type.
     * @param key the type to look for
     * @return the appropriate inclusion strategy
     */
    static InclusionStrategy getByType(final String key) {
    	TYPES.get(key.toLowerCase())
    }
  }
