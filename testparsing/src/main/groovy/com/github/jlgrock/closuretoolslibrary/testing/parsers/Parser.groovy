package com.github.jlgrock.closuretoolslibrary.testing.parsers

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import com.github.jlgrock.closuretoolslibrary.testing.DivParseFactory
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.EndParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.FailureStatisticParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IParsedDivObject
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IndividualFailureStatisticParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.IndividualTimingStatisticParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.LocationParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.StartParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.SummaryParsed
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.TimingStatisticParsed


/**
 * The parser for parsed Google Test Case objects.
 */
final class Parser {
	/**
	 * The test case that will be created and parsed during use.
	 */
	Parsed testCase

	/**
	 * Constructor.
	 * @param fileBasedOn the file that the test case is run on.
	 */
	Parser(final File fileBasedOn) {
		testCase = new Parsed(fileBasedOn)
	}

	/**
	 * The parser for the test case. Will parse the body into java objects.
	 * 
	 * @param body
	 *            the body to parse
	 * @return the java objects representing the google test cases
	 */
	Parsed parse(final WebElement body) {
		List<WebElement> divs = parseBody(body)
		WebElement div = null
		int i = 0
		while (i < divs.size()) {
			div = divs.get(i)
			String divText = div.text
			IParsedDivObject divParser = DivParseFactory.factory(testCase,
					divText)
			setDivProperty(divText, divParser)
			i++
		}
		testCase
	}

	/**
	 * Parsing the body into divs.
	 * 
	 * @param body
	 *            the body to parse
	 * @return the list of div web elements
	 */
	private List<WebElement> parseBody(final WebElement body) {
		body.findElements(By.tagName('div'))
	}

	/**
	 * store a div to the appropriate place.
	 * 
	 * @param divText the raw div text
	 * @param divParsed
	 *            the div that was parsed
	 */
	private void setDivProperty(final String divText, final IParsedDivObject divParsed) {
		if (divParsed instanceof SummaryParsed) {
			testCase.setSummary((SummaryParsed) divParsed)
		}

		if (divParsed instanceof LocationParsed) {
			testCase.setLocation((LocationParsed) divParsed)
		}

		if (divParsed instanceof IndividualTimingStatisticParsed) {
			testCase.setTimingStatistic((IndividualTimingStatisticParsed) divParsed)
		}

		if (divParsed instanceof FailureStatisticParsed) {
			testCase.setFailureStatistic((FailureStatisticParsed) divParsed)
		}

		if (divParsed instanceof TimingStatisticParsed) {
			testCase.setTestTimingStatistic((TimingStatisticParsed) divParsed)
		}

		if (divParsed instanceof StartParsed) {
			testCase.setTestCaseStart((StartParsed) divParsed)
		}

		if (divParsed instanceof IndividualFailureStatisticParsed) {
			testCase.addToTestFailureStatistics((IndividualFailureStatisticParsed) divParsed)
		}
		
		if (testCase.testCaseStart != null && testCase.testCaseEnd == null 
				&& divParsed == null) {
			//add to last failure statistic
			testCase.addToLastTestFailureStatistic(divText)
		}
		if (divParsed instanceof EndParsed) {
			testCase.setTestCaseEnd((EndParsed) divParsed)
		}
		testCase.addToRawDivs(divText)
	}
}
