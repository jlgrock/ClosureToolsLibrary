package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents


/**
 * The end timing div.
 * 
 * meant to parse the following:
 * 
 * 14:42:27.465 Done
 *
 */
final class EndParsed implements IParsedDivObject {
	/**
	 * Constructor.
	 * 
	 * @param completionTimeIn the time completed
	 */
	EndParsed(final Calendar completionTimeIn) {
		completionTime = completionTimeIn
	}
	
	/**
	 * The time completed.
	 */
	final Calendar completionTime

}
