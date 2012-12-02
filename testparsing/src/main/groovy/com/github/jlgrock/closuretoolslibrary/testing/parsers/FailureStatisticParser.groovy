package com.github.jlgrock.closuretoolslibrary.testing.parsers

import java.util.regex.Pattern

import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.FailureStatisticParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed

/**
 * The line that reads something like the following:
 * 
 * 12 passed, 2 failed.
 *
 */
final class FailureStatisticParser implements IDivParser {
	/**
	 * pattern matches "# passed, # failed".
	 */
	static final String PASSED_FAILED_PATTERN = '\\s*([0-9]*)\\s*passed,\\s*([0-9]*)\\s*failed.*'

	@Override
	boolean matches(final Parsed testCase, final String divText) {
		boolean returnVal = false
		if (testCase != null && testCase.testCaseStart == null) {
			if (Pattern.matches(PASSED_FAILED_PATTERN, divText)) {
				returnVal = true
			}
		}
		returnVal
	}

	@Override
	IParsedDivObject parse(final String divText) {
		String[] parsedValues = ParseUtils.parseIntoGroups(PASSED_FAILED_PATTERN, divText)
		int parsedNumPassed = Integer.valueOf(parsedValues[1])
		int parsedNumFailed = Integer.valueOf(parsedValues[2])
		new FailureStatisticParsed(parsedNumPassed, parsedNumFailed)
	}
}
