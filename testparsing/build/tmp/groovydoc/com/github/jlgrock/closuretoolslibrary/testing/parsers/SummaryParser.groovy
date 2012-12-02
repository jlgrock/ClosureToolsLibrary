package com.github.jlgrock.closuretoolslibrary.testing.parsers

import java.util.regex.Pattern

import com.github.jlgrock.closuretoolslibrary.testing.ResultType
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.SummaryParsed

/**
 * The object used for parsing the summary.
 * 
 * The line that reads something like one of the following:
 * 
 * Test for ../../../src/test/javascript/test.js [FAILED]
 * Test for ../../../src/test/javascript/test.js [PASSED]
 *
 */
final class SummaryParser implements IDivParser {
	/**
	 * The pattern used to match the summary line.
	 */
	static final String TEST_CASE_PATTERN = '(?:Test for )?(.*) \\[(PASSED|FAILED)\\]'

	@Override
	boolean matches(final Parsed testCase, final String divText) {
		boolean returnVal = false
		if (testCase != null && testCase.summary == null) {
			if (Pattern.matches(TEST_CASE_PATTERN, divText)) {
				returnVal = true
			}
		}
		returnVal
	}

	@Override
	IParsedDivObject parse(final String divText) {
		String[] parsedValues = ParseUtils.parseIntoGroups(TEST_CASE_PATTERN, divText)
		String parsedRelativeLocation = parsedValues[1]
		ResultType parsedResult = ResultType.getByName(parsedValues[2])
		new SummaryParsed(parsedRelativeLocation, parsedResult)
	}
}
