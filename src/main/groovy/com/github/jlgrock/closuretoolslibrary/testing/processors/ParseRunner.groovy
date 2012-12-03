package com.github.jlgrock.closuretoolslibrary.testing.processors

import org.apache.log4j.Logger
import org.openqa.selenium.By
import org.openqa.selenium.NotFoundException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

import com.github.jlgrock.closuretoolslibrary.testing.parsers.Parser
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed


/**
 * Parse all of the test cases to test case objects.
 */
final class ParseRunner {
	/**
	 * The Logger.
	 */
	static final Logger LOGGER = Logger.getLogger(ParseRunner)

	/**
	 * The driver to use to execute the web pages.
	 */
	UnitDriver driver

	/**
	 * The configurable amount of time to test an individual file.
	 */
	long testTimeoutSeconds

	/**
	 * Constructor.
	 * 
	 * @param webDriver
	 *            the webdriver to execute the web pages
	 * @param testTimeoutSeconds 
	 */
	ParseRunner(final UnitDriver webDriver, long testTimeoutSeconds) {
		this.driver = webDriver
		this.testTimeoutSeconds = testTimeoutSeconds
	}

	/**
	 * Parse the files.
	 * 
	 * @param fileToParse
	 *            File to parse
	 * @return the set of parsed test cases
	 */
	Parsed parseFile(final File fileToParse) {
		Parsed testCase = null
		
		boolean encounteredError = false
		try {
			driver.setException(null)
			String uri = fileToParse.toURI().toString()
			LOGGER.debug("parsing file: ${uri}")
			driver.get(uri)
			(new WebDriverWait(driver, testTimeoutSeconds))
					.until(new ExpectedCondition<WebElement>() {
						@Override
						WebElement apply(final WebDriver d) {
							d.findElement(By
									.linkText('Run again without reloading'))
						}
					})
			if (driver.exception != null) {
				throw driver.exception
			}
		} catch (TimeoutException te) {
			testCase = new Parsed(fileToParse,
					ResultType.TIMED_OUT, te.message)
			encounteredError = true
		} catch (NotFoundException nfe) {
			testCase = new Parsed(fileToParse,
					ResultType.BAD_OUTPUT, nfe.message)
			encounteredError = true
		} catch (WebDriverException se) {
			testCase = new Parsed(fileToParse,
					ResultType.SCRIPT_ERROR, se.message)
			encounteredError = true
		} catch (ScriptException se) {
			testCase = new Parsed(fileToParse,
					ResultType.SCRIPT_ERROR, se.message)
			encounteredError = true
		} catch (MalformedURLException mue) {
			testCase = new Parsed(fileToParse,
					ResultType.SCRIPT_ERROR, mue.message)
			encounteredError = true
		} catch (Exception e) {
			testCase = new Parsed(fileToParse,
					ResultType.UNABLE_TO_EXECUTE, e.message)
			encounteredError = true
		}
		WebElement body = driver.findElement(By.tagName('body'))
		Parser testCaseParser = new Parser(fileToParse)

		// if the web driver was unable to execute the page, mark the test
		// case as a failure, otherwise parse results
		if (!encounteredError) {
			testCase = testCaseParser.parse(body)
		}
		testCase
	}
	
	/**
	 * Closes all test resources required by this parse runner.
	 */
	void quit() {
		driver.quit()
	}
}
