package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents

import com.github.jlgrock.closuretoolslibrary.testing.ResultType



/**
 * The failure statistic for an individual test.
 * 
 * This would be something like the following:
 * 
 * 14:42:28.346 testSomething3 : PASSED
 * 
 * or 
 * 
 * 14:42:28.346 testSomething3 : FAILED (run single test)
 * 
 */
final class IndividualFailureStatisticParsed implements IParsedDivObject {
	/**
	 * the time of completion of the test.
	 */
	final Calendar completionTime

	/**
	 * the name of the test.
	 */
	final String nameOfTest

	/**
	 * the result of the test.
	 */
	final ResultType result
	
	/**
	 * The reasons behind the failure, if there was one.
	 */
	final List<String> failureReasons
	
	/**
	 * Constructor.
	 * 
	 * @param completionTimeIn the time of completion of the test
	 * @param nameOfTestIn the name of the test
	 * @param resultIn the result of the test
	 */
	IndividualFailureStatisticParsed(final Calendar completionTimeIn, 
			final String nameOfTestIn, final ResultType resultIn) {
		completionTime = completionTimeIn
		nameOfTest = nameOfTestIn
		result = resultIn
		failureReasons = []
	}
	
	/**
	 * Adds to the failureReasons ArrayList.
	 * 
	 * @param reason the string to add to the array
	 */
	void addToFailureReasons(final String reason) {
		failureReasons.add(reason)
	}
}
