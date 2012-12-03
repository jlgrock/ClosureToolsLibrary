package com.github.jlgrock.closuretoolslibrary.testing.generators

import com.github.jlgrock.closuretoolslibrary.testing.TestingConfiguration
import com.github.jlgrock.closuretoolslibrary.utils.pathing.FileNameSeparator
import com.github.jlgrock.closuretoolslibrary.utils.pathing.FileToFile


/**
 * A Class that will generate a test case output container and write to file.
 */
final class TCaseGenerator {

	final TestingConfiguration testingConfiguration
	
	/**
	 * Constructor.
	 */
	TCaseGenerator(final TestingConfiguration testingConfigurationIn) {
		testingConfiguration = testingConfigurationIn 
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
