package com.github.jlgrock.closuretoolslibrary.testing.parsers

import java.util.regex.Pattern

import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IndividualTimingStatisticParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed

/**
 * The parser for the object containing timing statistics for the Test Case.
 * 
 * Parses something like the following:
 * 
 * 2 of 2 tests run in 28ms.
 * 
 */
final class TimingStatisticParser implements IDivParser {
	/**
	 * The position of the hour.
	 */
	static final int NUMBER_RUN_POSITION = 1

	/**
	 * The position of the minute.
	 */
	static final int NUMBER_TOTAL_POSITION = 2
	
	/**
	 * The position of the time value.
	 */
	static final int TIME_VALUE_POSITION = 3

	/**
	 * The position of the time units.
	 */
	static final int TIME_UNITS_POSITION = 4
	
	/**
	 * pattern matches "# ms/test.  # files loaded."
	 */
	static final String TEST_CASE_TIMING_PATTERN = '\\s*([0-9]*)\\s*of\\s*([0-9]*)\\s*tests run in\\s*([0-9]*)(\\w)*\\.'

	@Override
	IParsedDivObject parse(final String divText) {
		String[] parsedValues = ParseUtils.parseIntoGroups(TEST_CASE_TIMING_PATTERN, divText)
		int parsedNumberRun = Integer.valueOf(parsedValues[NUMBER_RUN_POSITION])
		int parsedNumberTotal = Integer.valueOf(parsedValues[NUMBER_TOTAL_POSITION])
		int parsedTimeValue = Integer.valueOf(parsedValues[TIME_VALUE_POSITION])
		String parsedTimeUnits = parsedValues[TIME_UNITS_POSITION]
		new IndividualTimingStatisticParsed(parsedNumberRun, parsedNumberTotal, parsedTimeValue, parsedTimeUnits)
	}

	@Override
	boolean matches(final Parsed testCase, final String divText) {
		boolean retVal = false
		if (testCase != null && testCase.location != null
				&& testCase.timingStatistic == null
				&& Pattern.matches(TEST_CASE_TIMING_PATTERN, divText)) {
			retVal = true
		}
		retVal
	}
}
