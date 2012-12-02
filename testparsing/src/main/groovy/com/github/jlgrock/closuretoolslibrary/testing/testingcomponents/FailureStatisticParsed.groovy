package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents


/**
 * The line that reads something like the following:
 * 
 * 12 passed, 2 failed.
 *
 */
final class FailureStatisticParsed implements IParsedDivObject {
	/**
	 * The number of test cases that passed.
	 */
	final int numPassed
	
	/**
	 * The number of test cases that failed.
	 */
	final int numFailed
	
	/**
	 * Constructor.
	 * 
	 * @param numPassedIn The number of test cases that passed
	 * @param numFailedIn The number of test cases that failed
	 */
	FailureStatisticParsed(final int numPassedIn, final int numFailedIn) {
		numPassed = numPassedIn
		numFailed = numFailedIn
	}
}
