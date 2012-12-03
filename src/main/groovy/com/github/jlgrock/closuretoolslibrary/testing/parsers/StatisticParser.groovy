package com.github.jlgrock.closuretoolslibrary.testing.parsers

import java.util.regex.Pattern

import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.TimingStatisticParsed

/**
 * The parsing for the object representing the average time per test that it took to execute the test cases.
 * 
 * This would be something like the following:
 * 
 * 2 ms/test. 13 files loaded.
 *
 */
final class StatisticParser implements IDivParser {
	/**
	 * The position of the time.
	 */
	static final int TIME_POSITION = 1

	/**
	 * The position of the measurement.
	 */
	static final int MEASUREMENT_POSITION = 2
	
	/**
	 * The position of the number of files.
	 */
	static final int NUM_FILES_POSITION = 3
	
	/**
	 * pattern matches "# passed, # failed".
	 */
	static final String PASSED_FAILED_PATTERN = '\\s*([0-9]*)\\s*(\\w*)/test,\\s*([0-9]*)\\s*files loaded.\\s*'

	@Override
	final boolean matches(final Parsed testCase, final String divText) {
		boolean returnVal = false
		if (testCase != null && testCase.testCaseStart == null
				&& testCase.failureStatistic != null
				&& Pattern.matches(PASSED_FAILED_PATTERN, divText)) {
			returnVal = true
		}
		returnVal
	}

	@Override
	final IParsedDivObject parse(final String divText) {
		String[] parsedValues = ParseUtils.parseIntoGroups(
				PASSED_FAILED_PATTERN, divText)
		int parsedTime = Integer.valueOf(parsedValues[TIME_POSITION])
		String parsedUnitOfMeasurement = parsedValues[MEASUREMENT_POSITION]
		int parsedNumFiles = Integer.valueOf(parsedValues[NUM_FILES_POSITION])
		new TimingStatisticParsed(parsedTime, parsedUnitOfMeasurement,
				parsedNumFiles)
	}
}
