package com.github.jlgrock.closuretoolslibrary.utils.pathing


/**
 * A centralized class for calculating relative pathing for the output.
 */
final class JsarRelativeLocations {
	/**
	 * The location of the closure library files when extracted.
	 */
	public static final String CLOSURE_LIBRARY_LOCATION = 'closure-library'

	/**
	 * The location of the extern files in the jsar.
	 */
	public static final String JSAR_TEST_LOCATION = 'testDependencies'

	/**
	 * The location of the output of the test generation in the jsar.
	 */
	public static final String JSAR_TEST_SUITE_LOCATION = 'testSuite'

	/**
	 * The location of the output of the test generation in the jsar.
	 */
	public static final String JSAR_COMPILED_TEST_SUITE_LOCATION = 'testSuiteCompiled'

	/**
	 * The location of the extern files in the jsar.
	 */
	public static final String JSAR_EXTERN_LOCATION = 'externDependencies'

	/**
	 * The location of the assertion source files in the jsar.
	 */
	public static final String JSAR_ASSERTION_SOURCE_LOCATION = 'assertionSource'
	
	/**
	 * The location of the source files (with assertions removed) in the jsar.
	 */
	public static final String JSAR_PROCESSED_SOURCE_LOCATION = 'debugSource'
	
	/**
	 * The location of the intern files in the jsar.
	 */
	public static final String JSAR_INTERN_LOCATION = 'internDependencies'
	
	/**
	 * The location of the source files in the jsar.
	 */
	public static final String JSAR_OUTPUT_LOCATION = 'output'
	
	/**
	 * The location of the source files in the jsar.
	 */
	public static final String JSAR_COMPILE_LOCATION = 'compiled'
	
	/**
	 * The location of the generated assert deps file.
	 */
	public static final String JSAR_ASSERT_LOCATION = 'assert'
	
	
	/**
	 * The location of the generated debug deps file.
	 */
	public static final String JSAR_DEBUG_LOCATION = 'debug'
	
	/**
	 * The location of the generated debug deps file.
	 */
	public static final String JSAR_TEST_DEPS_LOCATION = 'testDeps'
	
	/**
	 * The location of the generated assert deps file.
	 */
	public static final String JSAR_ASSERT_REQUIRES_LOCATION = 'assertRequires'
	
	
	/**
	 * The location of the generated debug deps file.
	 */
	public static final String JSAR_DEBUG_REQUIRES_LOCATION = 'debugRequires'

	/**
	 * private Constructor for utility class.
	 */
	private JsarRelativeLocations() {
	}

	/**
	 * Build the path to the debug deps relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the debug deps folder
	 */
	static final File getClosureLibraryLocation(final File frameworkLocation) {
		new File(frameworkLocation, CLOSURE_LIBRARY_LOCATION)
	}
	
	/**
	 * Build the path to the debug deps relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the debug deps folder
	 */
	static final File getDebugDepsLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_DEBUG_LOCATION)
	}
	
	/**
	 * Build the path to the assert deps relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the assert deps folder
	 */
	static final File getAssertDepsLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_ASSERT_LOCATION)
	}
	
	/**
	 * Build the path to the debug requires relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the debug requires folder
	 */
	static final File getDebugRequiresLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_DEBUG_REQUIRES_LOCATION)
	}
	
	/**
	 * Build the path to the assert requires relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the assert requires folder
	 */
	static final File getAssertRequiresLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_ASSERT_REQUIRES_LOCATION)
	}
	
	/**
	 * Build the path to the test deps relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the test deps folder
	 */
	static final File getTestDepsLocation(final File frameworkLocation) {
		new File(frameworkLocation, JSAR_TEST_DEPS_LOCATION)
	}

	/**
	 * Build the path to the test suite relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the test deps folder
	 */
	static final File getTestLocation(final File frameworkLocation) {
		new File(frameworkLocation, JSAR_TEST_LOCATION)
	}
	
	/**
	 * Build the path to the test suite relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the test suite folder
	 */
	static final File getTestSuiteLocation(final File frameworkLocation) {
		new File(frameworkLocation, JSAR_TEST_SUITE_LOCATION)
	}
	
	/**
	 * Build the path to the compiled test suite relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the compiled test suite folder
	 */
	static final File getCompiledTestSuiteLocation(final File frameworkLocation) {
		new File(frameworkLocation, JSAR_COMPILED_TEST_SUITE_LOCATION)
	}
	
	/**
	 * Build the path to the assertion source relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the assertion source folder
	 */
	static final File getAssertionSourceLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_ASSERTION_SOURCE_LOCATION)
	}
	
	/**
	 * Build the path to the debug source relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the debug source folder
	 */
	static final File getDebugSourceLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_PROCESSED_SOURCE_LOCATION)
	}

	/**
	 * Build the path to the externs relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the externs folder
	 */
	static final File getExternsLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_EXTERN_LOCATION)
	}
	
	/**
	 * Build the path to the interns (internal dependencies) relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the interns (internal dependencies) folder
	 */
	static final File getInternsLocation(final File frameworkLocation) {
		new File(frameworkLocation, JSAR_INTERN_LOCATION)
	}
	
	/**
	 * Build the path to the interns (internal dependencies) relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the interns (internal dependencies) folder
	 */
	static final File getInternsDebugLocation(final File frameworkLocation) {
		new File(getInternsLocation(frameworkLocation), JSAR_PROCESSED_SOURCE_LOCATION)
	}
	
	/**
	 * Build the path to the interns (internal dependencies) relative to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the interns (internal dependencies) folder
	 */
	static final File getInternsAssertLocation(final File frameworkLocation) {
		new File(getInternsLocation(frameworkLocation), JSAR_ASSERTION_SOURCE_LOCATION)
	}
	/**
	 * Build the path to the output location to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the output location folder
	 */
	static final File getOutputLocation(final File frameworkLocation) {
		new File(frameworkLocation, JSAR_OUTPUT_LOCATION)
	}
	
	/**
	 * Build the path to the compiled output location to the frameworkLocation.
	 * @param frameworkLocation the folder to be relative from
	 * @return the path to the compiled output location folder
	 */
	static final File getCompileLocation(final File frameworkLocation) {
		new File(getOutputLocation(frameworkLocation), JSAR_COMPILE_LOCATION)
	}
}
