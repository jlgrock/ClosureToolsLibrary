package com.github.jlgrock.closuretoolslibrary.testing.generators


/**
 * Will generate a set of test case files. This expects that all source files
 * have been verified as JavaScript prior to usage in this Suite.
 * 
 */
final class SuiteGenerator {
	/**
	 * The set of the source files to convert.
	 */
	final List<File> sourceFiles

	/**
	 * The location of the closure library base.js.
	 */
	final File closureLibraryBaseLocation

	/**
	 * The location of the generated dependency file.
	 */
	final File depsLocation

	/**
	 * The location of the generated dependency files.
	 */
	final List<File> testDependencies

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
	 * Constructor.
	 * 
	 * @param sourceFilesIn
	 *            the source files to process as part of the suite. Assumed all
	 *            are javascript
	 * @param closureLibraryBaseLocationIn
	 *            the location of the closure library (which contains base.js)
	 * @param dependencyLocationIn
	 *            the location of the generated dependency file
	 * @param testDependenciesIn
	 *            the set of test dependencies
	 * @param preambleIn
	 *            the preamble to the test case
	 * @param prologueIn
	 *            the prologue to the test case
	 * @param epilogueIn
	 *            the epilogue to the test case
	 */
	SuiteGenerator(final List<File> sourceFilesIn,
			final File closureLibraryBaseLocationIn,
			final File dependencyLocationIn,
			final List<File> testDependenciesIn, final String preambleIn,
			final String prologueIn, final String epilogueIn) {
		sourceFiles = sourceFilesIn
		closureLibraryBaseLocation = closureLibraryBaseLocationIn
		depsLocation = dependencyLocationIn
		testDependencies = testDependenciesIn
		preamble = preambleIn
		prologue = prologueIn
		epilogue = epilogueIn
	}

	/**
	 * Generates the test case objects from the Source Files provided.
	 * 
	 * @return the test case objects
	 */
	private Set<TCaseGenerator> generateTestCases() {
		Set<TCaseGenerator> testCaseGenerators = [:]
		for (File sourceFile : sourceFiles) {
			testCaseGenerators.add(new TCaseGenerator(
					closureLibraryBaseLocation, depsLocation, sourceFile,
					testDependencies, preamble, prologue, epilogue))
		}
		testCaseGenerators
	}

	/**
	 * Creates the set of test files.
	 * 
	 * @param sourceLocation
	 *            the location of the testing source, used for relative pathing
	 * @param outputDirectory
	 *            the output directory to generate the files to
	 * @return the set of files created
	 * @throws IOException
	 *             if unable to create one or more test cases
	 */
	final Set<File> generateTestFiles(final File sourceLocation,
			final File outputDirectory) throws IOException {
		Set<File> outputFiles = [:]
		Set<TCaseGenerator> generatedTestCases = generateTestCases()
		for (TCaseGenerator testCaseGenerator : generatedTestCases) {
			File testCase = testCaseGenerator.mkTestCase(sourceLocation,
					outputDirectory)
			outputFiles.add(testCase)
		}
		outputFiles
	}
}
