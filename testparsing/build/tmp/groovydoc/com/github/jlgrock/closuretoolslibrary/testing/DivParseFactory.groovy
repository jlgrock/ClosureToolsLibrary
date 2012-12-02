package com.github.jlgrock.closuretoolslibrary.testing

import com.github.jlgrock.closuretoolslibrary.testing.parsers.EndParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.FailureStatisticParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.IDivParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.IndividualFailureStatisticParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.LocationParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.StartParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.StatisticParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.SummaryParser
import com.github.jlgrock.closuretoolslibrary.testing.parsers.TimingStatisticParser
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed



/**
 * Factory class for determining the correct parser for the testing output
 * section.
 * 
 */
final class DivParseFactory {
	/**
	 * Private constructor for a utility class.
	 */
	private DivParseFactory() {
	}

	/**
	 * The registered classes for matching against. This is hard coded for now,
	 * but should likely be dynamic in the future.
	 */
	private static Set<IDivParser> registeredClasses

	// TODO this could be more dynamic/elegant
	static {
		registeredClasses = [:]
		registeredClasses {
			add(new EndParser())
			add(new FailureStatisticParser())
			add(new LocationParser())
			add(new StartParser())
			add(new SummaryParser())
			add(new TimingStatisticParser())
			add(new IndividualFailureStatisticParser())
			add(new StatisticParser())
		}
	}

	/**
	 * Will search through the registered patterns to find a div parser to
	 * provide for parsing.
	 * 
	 * @param testCase
	 *            the testCase referred to in the factory, used for determining
	 *            whether certain sections have been hit yet in the matcher.
	 * @param divText
	 *            the text to match
	 * @return Returns the parsed DivParser object
	 */
	static IParsedDivObject factory(final Parsed testCase,
			final String divText) {
		for (IDivParser registeredClass : registeredClasses) {
			if (registeredClass.matches(testCase, divText)) {
				return registeredClass.parse(divText)
			}
		}
		null
	}
}
