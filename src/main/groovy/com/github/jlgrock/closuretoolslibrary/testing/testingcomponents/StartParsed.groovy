package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents


/**
 * The start timing div.
 * 
 * meant to parse the following:
 * 
 * 14:42:27.465 Done
 *
 */
final class StartParsed implements IParsedDivObject {
	/**
	 * Constructor.
	 * @param startTimeIn the time of start
	 */
	StartParsed(final Calendar startTimeIn) {
		startTime = startTimeIn
	}

	/**
	 * The time when the test case was started.
	 */
	final Calendar startTime

}
