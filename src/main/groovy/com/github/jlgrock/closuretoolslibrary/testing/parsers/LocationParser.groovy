package com.github.jlgrock.closuretoolslibrary.testing.parsers

import java.util.regex.Pattern

import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.LocationParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed

/**
 * Parser for the location.
 * 
 * The line that reads something like one of the following:
 * 
 * /C:/Workspaces/maven-plugins/testDependencyResolution/target/javascriptFramework/testSuite/test.html
 * 
 */
final class LocationParser implements IDivParser {
	/**
	 * pattern matches "/PATH".
	 */
	static final String TEST_CASE_PATTERN = '/(.*)\\s*'
	
	@Override
	boolean matches(final Parsed testCase, final String divText) {
		boolean returnVal = false
		if (testCase != null && testCase.location == null
				&& testCase.summary != null
				&& Pattern.matches(TEST_CASE_PATTERN, divText)) {
			returnVal = true
		}
		returnVal
	}

	@Override
	IParsedDivObject parse(final String divText) {
		String[] parsedValues = ParseUtils.parseIntoGroups(TEST_CASE_PATTERN, divText)
		String parsedTestCaseFilename = parsedValues[1]
		new LocationParsed(parsedTestCaseFilename)
	}
}
