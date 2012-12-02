package com.github.jlgrock.closuretoolslibrary.testing.generators

import com.github.jlgrock.closuretoolslibrary.utils.pathing.FileNameSeparator
import com.github.jlgrock.closuretoolslibrary.utils.pathing.FileToFile


/**
 * A Class that will generate a test case output container and write to file.
 */
final class TCaseGenerator {

	/**
	 * the location of the sourcefile to generate the test off of.
	 */
	final File generatorSourceFile

	/**
	 * the location of the closure library.
	 */
	final File generatorClosureLocation

	/**
	 * the location of the generated dependency file.
	 */
	final File generatorDepsLocation

	/**
	 * the location of the external dependency files.
	 */
	final List<File> testDeps

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
	 * @param closureLocation
	 *            the location of the closure library
	 * @param depsLocation
	 *            the location of the dependency file
	 * @param sourceFile
	 *            the location of the sourcefile to generate the test off of
	 * @param testDepsIn
	 *            the set of external dependencies for testing
	 * @param preambleIn
	 *            the preamble to the test case
	 * @param prologueIn
	 *            the prologue to the test case
	 * @param epilogueIn
	 *            the epilogue to the test case
	 */
	TCaseGenerator(final File closureLocation,
			final File depsLocation, final File sourceFile,
			final List<File> testDepsIn, final String preambleIn,
			final String prologueIn, final String epilogueIn) {
		generatorClosureLocation = closureLocation
		generatorSourceFile = sourceFile
		generatorDepsLocation = depsLocation
		testDeps = testDepsIn
		preamble = preambleIn
		prologue = prologueIn
		epilogue = epilogueIn
	}

	/**
	 * Create the test case at the output directory specified.
	 * 
	 * @param sourceLocation
	 *            the location of the test source, used for relative pathing
	 * @param outputDirectory
	 *            the output directory to store the file
	 * @return the test case file
	 * @throws IOException
	 *             if unable to create the directory or write to the test case
	 *             file
	 */
	File mkTestCase(final File sourceLocation,
			final File outputDirectory) throws IOException {
		FileNameSeparator fns = new FileNameSeparator(generatorSourceFile)
		String relPath = FileToFile.getRelPathFromBase(sourceLocation,
				new File(fns.path))
		String outputFilePath = outputDirectory.absoluteFile
				+ File.separator + relPath + File.separator + fns.name
				+ '.html'
		File testCase = new File(outputFilePath)
		new TCaseRef(generatorClosureLocation, generatorDepsLocation,
				generatorSourceFile, testCase, testDeps, preamble, prologue,
				epilogue).writeToFile()
		testCase
	}
}
