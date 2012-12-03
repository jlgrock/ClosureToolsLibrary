package com.github.jlgrock.closuretoolslibrary.testing


class TestingConfiguration {
	/**
	 * The test source directory containing test class sources.
	 */
	File testSourceDirectory

	/**
	 * Command line working directory.
	 */
	File frameworkTargetDirectory

	/**
	 * The location of the closure library.
	 */
	File closureLibraryLocation

	/**
	 * The file produced after running the dependencies and files through the
	 * compiler. This should match the name of the closure compiler.
	 */
	String compiledFilename

	/**
	 * Set this to "true" to skip running tests, but still compile them. Its use
	 * is NOT RECOMMENDED, but quite convenient on occasion.
	 */
	boolean skipTests = false

	/**
	 * If set to true, this forces the plug-in to generate and run the test
	 * cases on the compiled version of the code.
	 */
	boolean runTestsOnCompiled = false

	/**
	 * A list of <exclude> elements specifying the tests (by pattern) that
	 * should be included in testing. When not specified and when the test
	 * parameter is not specified, the default includes will be<br>
	 * &lt;excludes&gt;<br>
	 * &lt;exclude&gt;**\/Test*.java&lt;/exclude&gt;<br>
	 * &lt;exclude&gt;*\/*Test.java&lt;/exclude&gt;<br>
	 * &lt;exclude&gt;**\/*TestCase.java&lt;/exclude&gt;<br>
	 * &lt;/excludes&gt;<br>
	 */
	List<File> excludes

	/**
	 * A list of <include> elements specifying the tests (by pattern) that
	 * should be included in testing. When not specified and when the test
	 * parameter is not specified, the default includes will be<br>
	 * &lt;includes&gt;\<br\>
	 * &lt;include&gt;**\/Test*.java&lt;/include&gt;<br>
	 * &lt;include&gt;**\/*Test.java&lt;/include&gt;<br>
	 * &lt;include&gt;**\/*TestCase.java&lt;/include&gt;<br>
	 * &lt;/includes&gt;<br>
	 */
	List<File> includes

	/**
	 * The string for the {preamble} of the testing harness.
	 */
	String preamble = ''

	/**
	 * The string for the {prologue} of the testing harness.
	 */
	String prologue = ''

	/**
	 * The string for the {epilogue} of the testing harness.
	 */
	String epilogue = ''

	/**
	 * The maximum number of test case failures before failing the build. -1
	 * indicates unlimited.
	 */
	int maximumFailures = 5

	/**
	 * The maximum number of failures allowed before failing the build. By
	 * limiting this, it will speed up the build if there are many failures.
	 */
	long testTimeoutSeconds = 10

	/**
	 * The maximum number of threads to spawn for running test files. This
	 * parameter may be any value in the range 1 -
	 * <code>Runtime.getRuntime().availableProcessors() - 1</code>. Any value
	 * outside of this range will result in the default (processor count - 1)
	 * number of threads. Setting this property to 1 will disable
	 * multi-threading and run tests serially.
	 */
	int maxTestThreads = -1
}
