package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents

import com.github.jlgrock.closuretoolslibrary.testing.ResultType


/**
 * The container for parsed Google Test Case objects.
 */
final class Parsed {
	/**
	 * The prefix to the test case name for the test case error.
	 */
	static final String TEST_CASE_PREFIX = 'There was a problem with the \''
	
	/**
	 * The suffix to the test case name for the test case error.
	 */
	static final String TEST_CASE_SUFFIX = '\' Test Case.'

	/**
	 * An additional suffix to the test case name for the test cases where
	 * unable to parse.
	 */
	static final String TEST_CASE_TIMEOUT = '''
	Timed out before completion of test case.
	Make sure there is not an infinite loop and that you include 
	'goog.require(\"goog.testing.jsunit\");' at the top of your javascript file.'''

	/**
	 * An additional suffix to the test case name for the test cases where
	 * unable to parse.
	 */
	static final String TEST_CASE_BAD_OUTPUT = '''
	Completed execution of test case but was not able to find standardized test results.
	Make sure there is not an infinite loop and that you include 
	'goog.require(\"goog.testing.jsunit\");' at the top of your javascript file.'''

	/**
	 * An additional suffix to the test case name for the test cases where
	 * unable to parse.
	 */
	static final String TEST_SCRIPT_ERROR = '\n\tScript Error: '
	
	/**
	 * The error message when there is no parseable test cases in the file.
	 */
	static final String NO_TEST_CASES = '\n\tUnable to parse any test cases. ' +
			'Please add test cases or remove the test case file.'
	
	/**
	 * An additional suffix to the test case name for the test cases where
	 * unable to parse.
	 */
	static final String TEST_UNKNOWN_ERROR = '\n\tUnknown Error: '

	/**
	 * Simple newline representation.
	 */
	static final String NEWLINE = '\n'

	/**
	 * Simple tab representation.
	 */
	static final String TAB = '\t'

	/**
	 * the prefix for a test for the test case error output.
	 */
	static final String TEST_NAME_PREFIX = '* Test = '
	
	/**
	 * the suffix for a test for the test case error output.
	 */
	static final String RESULT_PREFIX = ' : '

	/**
	 * The result of the overall test case.
	 */
	ResultType result

	/**
	 * The summary section div, detailing the overall status.
	 */
	SummaryParsed summary

	/**
	 * The absolute location div.
	 */
	LocationParsed location

	/**
	 * The timing statistics div that details how long the entire test took.
	 */
	IndividualTimingStatisticParsed timingStatistic

	/**
	 * The number of successes and failures for the entire test case.
	 */
	FailureStatisticParsed failureStatistic

	/**
	 * the timing for the entire test case.
	 */
	TimingStatisticParsed testTimingStatistic

	/**
	 * The start information for the tests.
	 */
	StartParsed testCaseStart

	/**
	 * The set of information of all tests.
	 */
	List<IndividualFailureStatisticParsed> testFailureStatistics

	/**
	 * The end information for the tests.
	 */
	EndParsed testCaseEnd

	/**
	 * The div text for every div, even if it is not parsed into an object.
	 */
	Set<String> rawDivs

	/**
	 * The file that the test case results are based off of.
	 */
	final File fileBasedOn

	/**
	 * The message to be used when reporting an error of the entire test case
	 * (only used when there is a processing error).
	 */
	String errorMessage

	/**
	 * Error Constructor. This should only be used when constructing an error
	 * with processing.
	 * 
	 * @param fileBasedOnIn
	 *            the file that the test case results are based off of.
	 * @param resultIn
	 *            the initial value to set the Test case to. This is true by
	 *            default and will be overwritten if test cases properties fail.
	 * @param errorMessageIn
	 *            The error message to be used in error reporting.
	 */
	 Parsed(final File fileBasedOnIn, final ResultType resultIn,
			final String errorMessageIn) {
		this(fileBasedOnIn)
		result = resultIn
		errorMessage = errorMessageIn
	}

	/**
	 * Constructor.
	 * 
	 * @param fileBasedOnIn
	 *            the file that the test case results are based off of.
	 */
	Parsed(final File fileBasedOnIn) {
		testFailureStatistics = [];
		rawDivs = [:]
		fileBasedOn = fileBasedOnIn

		// It is assumed that the test case passed until something indicates
		// otherwise
		result = TestResultType.PASSED
	}


	/**
	 * @param testFailureStatisticsIn
	 *            the testFailureStatistics to set
	 */
	void setTestFailureStatistics(
			final List<IndividualFailureStatisticParsed> testFailureStatisticsIn) {
		// if any of the statistics are marked as a failure, mark the test case
		// as a failure
		for (IndividualFailureStatisticParsed stat : testFailureStatisticsIn) {
			if (stat == null || stat.result == ResultType.FAILED) {
				result = ResultType.FAILED
			}
		}
		this.testFailureStatistics = testFailureStatisticsIn
	}

	@Override
	String toString() {
		StringBuffer sb = new StringBuffer()
		sb.append(Parsed.NEWLINE)
		sb.append(Parsed.TEST_CASE_PREFIX)
		sb.append(fileBasedOn.absolutePath)
		sb.append(Parsed.TEST_CASE_SUFFIX)
		if (result == ResultType.TIMED_OUT) {
			sb.append(TEST_CASE_TIMEOUT)
			sb.append(Parsed.NEWLINE)
		} else if (result == ResultType.BAD_OUTPUT) {
			sb.append(TEST_CASE_BAD_OUTPUT)
			sb.append(Parsed.NEWLINE)
		} else if (result == ResultType.SCRIPT_ERROR) {
			sb.append(TEST_SCRIPT_ERROR)
			sb.append(Parsed.NEWLINE)
			sb.append(errorMessage)
			sb.append(Parsed.NEWLINE)
		} else if (result == ResultType.UNABLE_TO_EXECUTE) {
			sb.append(TEST_UNKNOWN_ERROR)
			sb.append(Parsed.NEWLINE)
			sb.append(errorMessage)
			sb.append(Parsed.NEWLINE)
		} else if (testFailureStatistics.size() == 0) {
			sb.append(NO_TEST_CASES)
			sb.append(Parsed.NEWLINE)
		} else {
			sb.append(Parsed.NEWLINE)
			for (IndividualFailureStatisticParsed singleTest : testFailureStatistics) {
				sb.append(Parsed.TEST_NAME_PREFIX)
				sb.append(singleTest.nameOfTest)
				sb.append(Parsed.RESULT_PREFIX)
				sb.append(singleTest.result)
				sb.append(Parsed.NEWLINE)
				for (String reason : singleTest.failureReasons) {
					sb.append(Parsed.TAB)
					sb.append(reason)
					sb.append(Parsed.NEWLINE)
				}
			}
		}
		sb.toString()
	}

	/**
	 * Will add a testFailureStatistic to the set.
	 * 
	 * @param testFailureStatistic
	 *            the object to add
	 */
	void addToTestFailureStatistics(
			final IndividualFailureStatisticParsed testFailureStatistic) {
		this.testFailureStatistics.add(testFailureStatistic)
	}

	/**
	 * Will add a lastTestFailureStatistic to the set.
	 * 
	 * @param divText
	 *            the text of the div
	 */
	void addToLastTestFailureStatistic(final String divText) {
		if (testFailureStatistics.size() > 0) {
			IndividualFailureStatisticParsed tfs = testFailureStatistics.last()
			tfs.addToFailureReasons(divText)
		}
	}
}
