package com.github.jlgrock.closuretoolslibrary.testing.parsers

import java.util.regex.Pattern

import com.github.jlgrock.closuretoolslibrary.testing.ResultType
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IndividualFailureStatisticParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed

/**
 * The parser for the failure statistic for an individual test.
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
final class IndividualFailureStatisticParser implements IDivParser {
	/**
	 * The position of the hour.
	 */
	static final int HOUR_POSITION = 1

	/**
	 * The position of the minute.
	 */
	static final int MINUTE_POSITION = 2
	
	/**
	 * The position of the second.
	 */
	static final int SECOND_POSITION = 3

	/**
	 * The position of the millisecond.
	 */
	static final int MILLISECOND_POSITION = 4
	
	/**
	 * The position of the parsed name.
	 */
	static final int PARSED_NAME_POSITION = 5
	
	/**
	 * The position of the parsed result.
	 */
	static final int PARSED_RESULT_POSITION = 6
	
	/**
	 * pattern matches "[time] testname : PASSED/FAILED".
	 */
	static final String SINGLE_TEST_PATTERN = '\\s*([0-9]*):([0-9]*):([0-9]*)\\.([0-9]*)' +
		'\\s*(\\w*)\\s:\\s(PASSED|FAILED)\\s*.*'
	
	@Override
	boolean matches(final Parsed testCase, final String divText) {
		boolean returnVal = false
		if (testCase != null && testCase.testCaseStart != null && testCase.testCaseEnd == null) {
			if (Pattern.matches(SINGLE_TEST_PATTERN, divText)) {
				returnVal = true
			}
		}
		returnVal
	}
	
	@Override
	IParsedDivObject parse(final String divText) {
		String[] parsedValues = ParseUtils.parseIntoGroups(SINGLE_TEST_PATTERN, divText)
		Calendar parsedCompletionTime = new GregorianCalendar()
		Date currentDate = new Date()
		parsedCompletionTime{
			setTime(currentDate)
			set(Calendar.HOUR, Integer.valueOf(parsedValues[HOUR_POSITION]))
			set(Calendar.MINUTE, Integer.valueOf(parsedValues[MINUTE_POSITION]))
			set(Calendar.SECOND, Integer.valueOf(parsedValues[SECOND_POSITION]))
			set(Calendar.MILLISECOND, Integer.valueOf(parsedValues[MILLISECOND_POSITION]))
		}
		
		String parsedName = new String(parsedValues[PARSED_NAME_POSITION])
		ResultType parsedResult = ResultType.getByName(parsedValues[PARSED_RESULT_POSITION])
		
		new IndividualFailureStatisticParsed(parsedCompletionTime, parsedName, parsedResult)
	}
}
