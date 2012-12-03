package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents

import com.github.jlgrock.closuretoolslibrary.testing.processors.ResultType




/**
 * The object storing the summary.
 * 
 * The line that reads something like one of the following:
 * 
 * Test for ../../../src/test/javascript/test.js [FAILED]
 * Test for ../../../src/test/javascript/test.js [PASSED]
 *
 */
class SummaryParsed implements IParsedDivObject {
	/**
	 * The relative location of the test file.
	 */
	final String relativeLocation
	
	/**
	 * The result of the overall test case.
	 */
	final ResultType result
	
	/**
	 * Constructor.
	 * 
	 * @param relativeLocationIn The relative location of the test file.
	 * @param resultIn The result of the overall test case.
	 */
	SummaryParsed(final String relativeLocationIn, final ResultType resultIn) {
		relativeLocation = relativeLocationIn
		result = resultIn
	}

}
