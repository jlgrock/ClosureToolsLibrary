package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents


/**
 * The location.
 * 
 * The line that reads something like one of the following:
 * 
 * /C:/Workspaces/maven-plugins/testDependencyResolution/target/javascriptFramework/testSuite/test.html
 * 
 */
final class LocationParsed implements IParsedDivObject {
	
	/**
	 * Constructor.
	 * 
	 * @param testCaseFilenameIn the filename of the test case
	 */
	LocationParsed(final String testCaseFilenameIn) {
		this.testCaseFilename = testCaseFilenameIn
	}

	/**
	 * The absolute filename for the test case.
	 */
	final String testCaseFilename
}
