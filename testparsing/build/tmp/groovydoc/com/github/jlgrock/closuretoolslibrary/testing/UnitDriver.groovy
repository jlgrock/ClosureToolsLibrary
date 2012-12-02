package com.github.jlgrock.closuretoolslibrary.testing

import org.openqa.selenium.TimeoutException
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import com.gargoylesoftware.htmlunit.ScriptException
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener

/**
 * A specific version of the HTML Unit Driver that will store an exception for
 * later use.
 */
final class UnitDriver extends HtmlUnitDriver {
	/**
	 * Used to define the error to the user when there is a timeout.
	 */
	static final String TIMEOUT_PREFIX = 'execution time exceeded maximum amount of time of '

	/**
	 * A variable for storing the first exception encountered.
	 */
	Exception exception = null

	/**
	 * The default constructor, which just calls the HTMLUnitDriver constructor.
	 * @param enableJavascript whether or not to enable JavaScript in the driver
	 */
	UnitDriver(final boolean enableJavascript) {
		super(enableJavascript)
	}

	/**
	 * Override the default behavior of the unit driver to provide credentials
	 * or alternate behaviors.
	 * @param client the client to modify
	 * @return the modified client
	 */
	@Override
	protected final WebClient modifyWebClient(final WebClient client) {
		client.setThrowExceptionOnScriptError(true)
		client.setThrowExceptionOnFailingStatusCode(true)
		JavaScriptErrorListener javaScriptErrorListener = new JavaScriptErrorListener() {

					@Override
					void scriptException(final HtmlPage htmlPage,
					final com.gargoylesoftware.htmlunit.ScriptException scriptException) {
						setException(scriptException)
					}

					@Override
					void timeoutError(final HtmlPage htmlPage,
					final long allowedTime, final long executionTime) {
						setException(new TimeoutException(
								TIMEOUT_PREFIX + String.valueOf(allowedTime)))
					}

					@Override
					void malformedScriptURL(final HtmlPage htmlPage,
					final String url,
					final MalformedURLException malformedURLException) {
						setException(malformedURLException)
					}

					@Override
					void loadScriptError(final HtmlPage htmlPage,
					final URL scriptUrl, final Exception exceptionIn) {
						setException(exceptionIn)
					}
				}
		client.setJavaScriptErrorListener(javaScriptErrorListener)
		client
	}
}
