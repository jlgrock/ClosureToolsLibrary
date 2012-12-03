package com.github.jlgrock.closuretoolslibrary.testing.generators


/**
 * The object to store and write the test case.
 */
final class TCaseRef {
	/**
	 * Constant property to store preamble.
	 */
	static final String PREAMBLE_PROPERTY = 'jsframework.testing.preamble'

	/**
	 * Constant property to store epilogue.
	 */
	static final String EPILOGUE_PROPERTY = 'jsframework.testing.epilogue'

	/**
	 * The location of the closure base file.
	 */
	final File closureBaseLocation
	
	/**
	 * The location of the test case output file.
	 */
	final File testCaseFileLocation
	
	/**
	 * The location of the dependency file.
	 */
	final File dependencyLocation
	
	/**
	 * The location of the test file.
	 */
	final File testFileLocation

	/**
	 * A set of files that will serve as script tag includes to the test case.
	 */
	final List<File> testDeps

	/**
	 * The begin tag for html.
	 */
	static final String BEGIN_HTML = '<html>\n'

	/**
	 * The end tag for html.
	 */
	static final String END_HTML = '</html>'

	/**
	 * The begin tag for the head.
	 */
	static final String BEGIN_HEAD = '\t<head>\n'

	/**
	 * The beginning of the title.
	 */
	static final String BEGIN_TITLE = '\t\t<title>Test for '

	/**
	 * The end of the title.
	 */
	static final String END_TITLE = '</title>\n'

	/**
	 * The end tag for the head.
	 */
	static final String END_HEAD = '\t</head>\n'
	
	/**
	 * The begin tag for body.
	 */
	static final String BEGIN_BODY = '\t<body>\n'
	
	/**
	 * The end tag for body.
	 */
	static final String END_BODY = '\t</body>\n'
	
	/**
	 * The begin tag for script.
	 */
	static final String BEGIN_SCRIPT = '\t\t<script src=\"'
	
	/**
	 * The end tag for script.
	 */
	static final String END_SCRIPT = '\"></script>\n'

	/**
	 * The preamble for the test.
	 */
	final String preamble

	/**
	 * The prologue for the test.
	 */
	final String prologue

	/**
	 * The epilogue for the test.
	 */
	final String epilogue

	/**
	 * The Constructor.
	 * 
	 * @param closureLocation
	 *            the location of the closure library
	 * @param depsLocation
	 *            the location of the dependency file
	 * @param testFile
	 *            the location of the test file
	 * @param testCase
	 *            the file to be written
	 * @param testDepsIn
	 *            the set of files that will serve as script tag includes to the test case
	 * @param preambleIn
	 * 			  the preamble to the test case
	 * @param prologueIn
	 * 			  the prologue to the test case
	 * @param epilogueIn
	 * 			  the epilogue to the test case
	 */
	TCaseRef(final File closureLocation, final File depsLocation,
			final File testFile, final File testCase, final List<File> testDepsIn,
			final String preambleIn, final String prologueIn, final String epilogueIn) {
		closureBaseLocation = closureLocation
		testCaseFileLocation = testCase
		dependencyLocation = depsLocation
		testFileLocation = testFile
		testDeps = testDepsIn
		preamble = preambleIn
		prologue = prologueIn
		epilogue = epilogueIn
	}

	/**
	 * The method that will begin writing to the file.
	 * 
	 * @throws IOException
	 *             if there is a problem writing to the file
	 */
	final void writeToFile() throws IOException {
		// create the directories if they haven't been made already
		// TODO what if this is at the root? side case to eventually handle
		testCaseFileLocation.parentFile.mkdirs()

		FileWriter fileWriter = new FileWriter(testCaseFileLocation)
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
		try {
			bufferedWriter.with {
				write(BEGIN_HTML)
				write(BEGIN_HEAD)

				// preamble
				write(this.preamble)

				// name of the test case
				write(BEGIN_TITLE)
				String title = StringEscapeUtils
					.escapeHtml(RelativePath.getRelPathFromBase(
							testCaseFileLocation, testFileLocation))
				write(title)
				write(END_TITLE)

				write(END_HEAD)
				write(BEGIN_BODY)

				// test requires
				for (File testDep : testDeps) {
					write(BEGIN_SCRIPT)
					write(RelativePath.getRelPathFromBase(
						testCaseFileLocation, testDep))
					write(END_SCRIPT)
				}
			
				// goog.base script
				write(BEGIN_SCRIPT)
				write(RelativePath.getRelPathFromBase(
					testCaseFileLocation, closureBaseLocation))
				write(END_SCRIPT)
		
				// deps script
				write(BEGIN_SCRIPT)
				write(RelativePath.getRelPathFromBase(
					testCaseFileLocation, dependencyLocation))
				write(END_SCRIPT)
			
				// prologue
				write(this.prologue)


				// test case
				write(BEGIN_SCRIPT)
				write(RelativePath.getRelPathFromBase(
					testCaseFileLocation, testFileLocation))
				write(END_SCRIPT)

				// epilogue 
				write(this.epilogue)

				write(END_BODY)
				write(END_HTML)
			}
		} finally {
			IOUtils.closeQuietly(bufferedWriter)
		}
	}
}
