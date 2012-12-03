package com.github.jlgrock.closuretoolslibrary.testing.parsers

import java.util.regex.Pattern

import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.StartParsed

/**
 * The start timing div.
 * 
 * meant to parse the following:
 * 
 * 14:42:27.465 Start
 *
 */
final class StartParser implements IDivParser {
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
	 * pattern matches "[time] Start".
	 */
	static final String START_TAG_PATTERN = '\\s*([0-9]*):([0-9]*):([0-9]*).([0-9]*)\\s*Start\\s*'

	@Override
	boolean matches(final Parsed testCase, final String divText) {
		Pattern.matches(START_TAG_PATTERN, divText)
	}

	@Override
	IParsedDivObject parse(final String divText) {
		String[] parsedValues = ParseUtils.parseIntoGroups(START_TAG_PATTERN, divText)
		Calendar parsedCompletionTime = new GregorianCalendar()
		Date currentDate = new Date()
		parsedCompletionTime.setTime(currentDate)
		
		parsedCompletionTime.set(Calendar.HOUR, Integer.valueOf(parsedValues[HOUR_POSITION]))
		parsedCompletionTime.set(Calendar.MINUTE, Integer.valueOf(parsedValues[MINUTE_POSITION]))
		parsedCompletionTime.set(Calendar.SECOND, Integer.valueOf(parsedValues[SECOND_POSITION]))
		parsedCompletionTime.set(Calendar.MILLISECOND, Integer.valueOf(parsedValues[MILLISECOND_POSITION]))
		new StartParsed(parsedCompletionTime)
	}
}
