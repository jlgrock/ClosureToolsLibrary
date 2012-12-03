package com.github.jlgrock.closuretoolslibrary.compiler

import org.apache.log4j.Level
import org.apache.log4j.Logger

import com.github.jlgrock.closuretoolslibrary.utils.logging.Log4jOutputStream
import com.github.jlgrock.closuretoolslibrary.utils.pathing.FileToFile
import com.github.jlgrock.closuretoolslibrary.utils.pathing.JsarRelativeLocations
import com.google.common.io.Closeables
import com.google.common.io.Files
import com.google.javascript.jscomp.CommandLineRunner
import com.google.javascript.jscomp.CompilationLevel
import com.google.javascript.jscomp.Compiler
import com.google.javascript.jscomp.CompilerOptions
import com.google.javascript.jscomp.JSError
import com.google.javascript.jscomp.Result
import com.google.javascript.jscomp.SourceFile
import com.google.javascript.jscomp.WarningLevel
import com.google.javascript.jscomp.SourceMap.DetailLevel
import com.google.javascript.jscomp.SourceMap.Format

/**
 * 
 * @goal js-closure-compile
 * @phase compile
 */
class JsClosureCompile {
	
	/**
	 * Newline constant.
	 */
	static final String NEWLINE = '\n'
	
	/**
	 * The configuration options to execute the compile.
	 */
	final CompilerConfiguration config
	
	/**
	 * Default constructor.
	 * @param configIn the configuration options to execute the compile.
	 */
	JsClosureCompile(final CompilerConfiguration configIn) {
		config = configIn
	}
	
	/**
	 * What extension to use for the source map file.
	 */
	static final String SOURCE_MAP_EXTENSION = '.smap'

	/**
	 * The Logger.
	 */
	static final Logger LOGGER = Logger.getLogger(JsClosureCompile)

	/**
	 * A simple util to convert a collection of files to a list of closure
	 * JSSourceFiles.
	 * 
	 * @param jsFiles
	 *            the collection of files to convert
	 * @return the list of google formatted objects
	 */
	private static List<SourceFile> convertToSourceFiles(
	final List<File> jsFiles) {
		List<SourceFile> jsSourceFiles = []
		for (File f : jsFiles) {
			jsSourceFiles.add(SourceFile.fromFile(f))
		}
		jsSourceFiles
	}

	/**
	 * Create the dependencies JS file.
	 * 
	 * @param baseLocation
	 *            the location of base.js
	 * @param src
	 *            the location of the source files
	 * @param interns
	 *            the internal dependencies
	 * @param depsFile
	 *            the location of the deps file
	 * @param requiresFile
	 *            the location of the requires file
	 * @return the list of dependencies, in dependency order
	 * @throws MojoExecutionException
	 *             if the dependency generator is not able to make a file
	 * @throws IOException
	 *             if there is a problem reading or writing to any of the files
	 */
	private static List<File> createDepsAndRequiresJS(final File baseLocation,
	final Collection<File> src, final Collection<File> interns,
	final File depsFile, final File requiresFile)
	throws CompileException {

		// TODO when they fix the visibility rules in the DepsGenerator, replace
		// it with Google's version
		LOGGER.with {
			debug("base location: ${baseLocation}")
			debug("src files: ${src}")
			debug("intern files: ${interns}")
			debug("deps file location: ${depsFile}")
		}

		CalcDeps.executeCalcDeps(baseLocation, src, interns, depsFile,
				requiresFile)
	}

	/**
	 * Get the location of base.js.
	 * 
	 * @param closureLibraryLocation
	 *            the location of the google library
	 * @return the base.js file reference
	 * @throws MojoExecutionException
	 *             If it couldn't find base.js
	 */
	private static File getBaseLocation(final File closureLibraryLocation)
	throws CompileException {
		def locStr = "${closureLibraryLocation.absoluteFile}${File.separator}" + 
			"closure${File.separator}goog${File.separator}base.js"
		File baseLocation = new File(locStr)
		if (!baseLocation.exists()) {
			throw new CompileException('Could not locate \"base.js\" at ' + 
				"location \"${baseLocation.parentFile.absolutePath}\"")
		}
		baseLocation
	}

	/**
	 * List the errors that google is providing from the compiler output.
	 * 
	 * @param result
	 *            the results from the compiler
	 */
	private static void listErrors(final Result result) {
		for (JSError warning : result.warnings) {
			LOGGER.warn("[Goog.WARN]: ${warning.toString()}")
		}

		for (JSError error : result.errors) {
			LOGGER.error("[Goog.ERROR]: ${error.toString()}")
		}
	}

	/**
	 * The string to match the code fragment in the outputWrapper parameter.
	 */
	static final String OUTPUT_WRAPPER_MARKER = '%output%'

	/**
	 * Extract external dependency libraries to the location specified in the
	 * settings.
	 * 
	 * @return the list of the files that are extracted
	 * @throws IOException
	 *             if unable to read the default externs
	 */
	private List<SourceFile> calculateExternFiles() throws IOException {
		List<File> externSourceFiles = listFiles(JsarRelativeLocations
				.getExternsLocation(frameworkTargetDirectory))
		List<SourceFile> externalSourceFiles = convertToSourceFiles(externSourceFiles)
		externalSourceFiles.addAll(CommandLineRunner.defaultExterns)
		LOGGER.debug("number of external files: ${externalSourceFiles.size()}")
		externalSourceFiles
	}

	/**
	 * Extract internal dependency libraries, source to the location specified
	 * in the settings. Then create the deps to be loaded first.
	 * 
	 * @param internsLocation
	 *            the location of the interns files
	 * @param source
	 *            the collection of source files
	 * @return the list of the files that are extracted (plus the generated deps
	 *         file)
	 * @throws MojoExecutionException
	 *             if there is a problem generating the dependency file
	 * @throws IOException
	 *             if there is a problem reading or extracting the files
	 */
	private Collection<File> calculateInternalFiles(final File internsLocation,
	final Collection<File> source) throws CompileException {
		List<File> internalSourceFiles = listFiles(internsLocation)
		LOGGER.debug("number of internal dependency files: ${internalSourceFiles.size()}")

		List<File> closureLibFiles = listFiles(closureLibraryLocation)
		LOGGER.debug("number of google lib files: ${closureLibFiles.size()}")

		Set<File> combinedInternal = [:] as Set

		combinedInternal.with {
			addAll(source)
			addAll(internalSourceFiles)
			addAll(closureLibFiles)
		}

		combinedInternal
	}

	/**
	 * Calculates the Source file collection.
	 * 
	 * @param sourceDir
	 *            the source directory to scan
	 * @param internsLocation
	 *            the internal dependency
	 * @return the set of files calculated
	 */
	private Set<File> calculateSourceFiles(final File sourceDir,
	final File internsLocation) {
		InclusionStrategy strategy = inclusionStrategy
		if (strategy == null) {
			strategy = InclusionStrategy.WHEN_IN_SRCS
		}
		LOGGER.info("Calculating source files using Inclusion strategy: $strategy")
		Set<File> listSourceFiles = [:] as Set
		if (strategy == InclusionStrategy.WHEN_IN_SRCS) {
			listSourceFiles.addAll(listFiles(sourceDir))
		} else {
			listSourceFiles.addAll(listFiles(sourceDir))
			listSourceFiles.addAll(listFiles(internsLocation))
		}
		LOGGER.debug("number of source files: ${listSourceFiles.size()}")
		listSourceFiles
	}

	/**
	 * Run the compiler on the calculated dependencies, input files, and
	 * external files.
	 * 
	 * @param allSources
	 *            the source files to compile
	 * @param externs
	 *            the external dependency javascript files
	 * @return true if the compile works, false otherwise
	 * @throws MojoExecutionException
	 *             if the options are set incorrectly for the compiler
	 * @throws MojoFailureException
	 *             if there is a problem executing the dependency creation or
	 *             the compiler
	 * @throws IOException
	 *             if there is a problem reading or writing to the files
	 */
	private boolean compile(final List<SourceFile> allSources,
	final List<SourceFile> externs) throws CompileException {
		CompilationLevel compilationLevel = null
		try {
			compilationLevel = CompilationLevel.valueOf(compileLevel)
			LOGGER.info("Compiler set to optimization level \"$compileLevel\".")
		} catch (IllegalArgumentException e) {
			LOGGER.error('Compilation level invalid.  Aborting.')
			throw new CompileException('Compilation level invalid.  Aborting.')
		}

		CompilerOptions compilerOptions = new CompilerOptions()
		if (errorLevel == ErrorLevel.WARNING) {
			WarningLevel wLevel = WarningLevel.VERBOSE
			wLevel.setOptionsForWarningLevel(compilerOptions)
		} else if (errorLevel == ErrorLevel.STRICT) {
			StrictLevel sLevel = StrictLevel.VERBOSE
			sLevel.setOptionsForWarningLevel(compilerOptions)
		}
		compilationLevel.setOptionsForCompilationLevel(compilerOptions)
		compilerOptions.setGenerateExports(generateExports)

		File sourceMapFile = null
		if (generateSourceMap) {
			sourceMapFile = new File(
					JsarRelativeLocations
					.getCompileLocation(frameworkTargetDirectory),
					compiledFilename + SOURCE_MAP_EXTENSION)
			compilerOptions.sourceMapFormat = Format.V3
			compilerOptions.sourceMapDetailLevel = DetailLevel.ALL
			compilerOptions.sourceMapOutputPath = sourceMapFile.absolutePath
		}

		PrintStream ps = new PrintStream(new Log4jOutputStream(LOGGER,
				Level.DEBUG), true)
		Compiler compiler = new Compiler(ps)

		for (SourceFile jsf : allSources) {
			LOGGER.debug("source files: ${jsf.originalPath}")
		}

		Result result = null
		try {
			LOGGER.debug("externJSSourceFiles: ${externs}")
			LOGGER.debug("allSources: ${allSources}")
			result = compiler.compile(externs, allSources, compilerOptions)
		} catch (Exception e) {
			LOGGER.error('There was a problem with the compile.  Please review input.')
			throw new CompileException(e.message, e)
		}

		listErrors(result)

		if (!result.success) {
			return false
		}

		File compiledFile = new File(
				JsarRelativeLocations
				.getCompileLocation(frameworkTargetDirectory),
				compiledFilename)
		Files.createParentDirs(compiledFile)
		Files.touch(compiledFile)
		if (generateSourceMap) {
			String sourcemapLocation = FileToFile.getRelPathFromBase(
					sourceMapFile, JsarRelativeLocations
					.getDebugDepsLocation(frameworkTargetDirectory))

			JsClosureCompileMojo.writeOutput(compiledFile, compiler,
					outputWrapper, OUTPUT_WRAPPER_MARKER, sourcemapLocation,
					sourceMapFile)
			JsClosureCompileMojo.writeSourceMap(compiledFile, sourceMapFile,
					frameworkTargetDirectory, result, outputWrapper,
					OUTPUT_WRAPPER_MARKER)
		} else {
			JsClosureCompileMojo.writeOutput(compiledFile, compiler,
					outputWrapper, OUTPUT_WRAPPER_MARKER)
		}

		true
	}

	//TODO this needs to be moved out and to use the
	@Override
	public final void execute() throws CompileException {
		LOGGER.info('Compiling source files and internal dependencies to location "'
				+ JsarRelativeLocations.getCompileLocation(
				frameworkTargetDirectory).absolutePath + '".')
		// gather externs for both asserts and debug
		List<SourceFile> externs = calculateExternFiles()

		// get base location for closure library
		File baseLocation = getBaseLocation(closureLibraryLocation)

		// create assert file
		Collection<File> assertSourceFiles = calculateSourceFiles(
				JsarRelativeLocations
				.getAssertionSourceLocation(frameworkTargetDirectory),
				JsarRelativeLocations
				.getInternsAssertLocation(frameworkTargetDirectory))
		File assertFile = generatedAssertJS
		File assertRequiresFile = generatedAssertRequiresJS
		Collection<File> assertInternFiles = calculateInternalFiles(
				JsarRelativeLocations
				.getInternsAssertLocation(frameworkTargetDirectory),
				assertSourceFiles)
		createDepsAndRequiresJS(baseLocation, assertSourceFiles,
				assertInternFiles, assertFile, assertRequiresFile)

		// create debug file
		File debugFile = generatedDebugJS
		File debugRequiresFile = generatedDebugRequiresJS
		Collection<File> sourceFiles = calculateSourceFiles(
				JsarRelativeLocations
				.getDebugSourceLocation(frameworkTargetDirectory),
				JsarRelativeLocations
				.getInternsDebugLocation(frameworkTargetDirectory))
		Collection<File> debugInternFiles = calculateInternalFiles(
				JsarRelativeLocations
				.getInternsDebugLocation(frameworkTargetDirectory),
				sourceFiles)
		List<File> debugDepsFiles = createDepsAndRequiresJS(baseLocation,
				sourceFiles, debugInternFiles, debugFile, debugRequiresFile)

		// create testing file
		File testDepsFile = generatedTestJS
		Set<File> srcAndTest = [:] as Set
		srcAndTest.addAll(assertSourceFiles)
		srcAndTest.addAll(FileListBuilder.buildFilteredList(
				testSourceDirectory, 'js'))
		createDepsAndRequiresJS(baseLocation, srcAndTest,
				assertInternFiles, testDepsFile, null)

		// create file collection for compilation
		List<File> debugFiles = []
		debugFiles.add(getBaseLocation(closureLibraryLocation))
		debugFiles.add(debugFile)
		debugFiles.addAll(debugDepsFiles)

		// compile debug into compiled dir
		boolean result = compile(convertToSourceFiles(debugFiles), externs)

		if (!result) {
			String message = 'Google Closure Compilation failure.  Please review errors to continue.'
			LOGGER.error(message)
			throw new CompileException(message)
		}
	}

	/**
	 * Will write the output file, including the wrapper around the code, if any
	 * exist.
	 * 
	 * @param outFile
	 *            The file to write to
	 * @param compiler
	 *            The google compiler
	 * @param wrapper
	 *            the string to wrap around the code (using the codePlaceholder)
	 * @param codePlaceholder
	 *            the identifier for the code
	 * @throws IOException
	 *             when the file cannot be written to.
	 */
	static void writeOutput(final File outFile, final Compiler compiler,
	final String wrapper, final String codePlaceholder)
	throws IOException {
		writeOutput(outFile, compiler, wrapper, codePlaceholder, null, null)
	}

	/**
	 * Will write the output file, including the wrapper around the code, if any
	 * exist.
	 * 
	 * @param outFile
	 *            The file to write to
	 * @param compiler
	 *            The google compiler
	 * @param wrapper
	 *            the string to wrap around the code (using the codePlaceholder)
	 * @param codePlaceholder
	 *            the identifier for the code
	 * @param sourceMapFile
	 *            The file containing the source map information, can be null
	 * @throws IOException
	 *             when the file cannot be written to.
	 */
	static void writeOutput(final File outFile, final Compiler compiler,
	final String wrapper, final String codePlaceholder,
	final File sourceMapFile)
	throws CompileException {
		FileWriter out = new FileWriter(outFile)
		String code = compiler.toSource()
		boolean threw = true
		try {
			int pos = wrapper.indexOf(codePlaceholder)
			LOGGER.debug("wrapper = ${wrapper}")
			if (pos != -1) {
				String prefix = ''

				if (pos > 0) {
					prefix = wrapper[0..pos]
					LOGGER.debug("prefix = ${prefix}")
					out.append(prefix)
				}

				out.append(code)

				int suffixStart = pos + codePlaceholder.size()
				if (suffixStart != wrapper.size()) {
					LOGGER.debug("suffix${wrapper[suffixStart]}")
					// Something after placeholder?
					out.append(wrapper[suffixStart])
				}
				// Make sure we always end output with a line feed.
			} else {
				out.append(code)
			}
			if (sourceMapFile != null) {
				out.append(NEWLINE)
				out.append("//@ sourceMappingURL=${sourceMapFile.name}")
			}
			out.append(NEWLINE)
			threw = false
		} finally {
			Closeables.close(out, threw)
		}
	}

	/**
	 * Will write the sourceMap to a output file, will also change the prefix in
	 * the source map if needed.
	 * 
	 * @param originalFile
	 *            The source file, just used to determine the path and name of
	 *            the source map file
	 * @param outputFile
	 *            The output file where to place the source map content
	 * @param frameworkTargetDirectory
	 *            The base directory that contains the output files
	 * @param result
	 *            The google compiler result
	 * @param wrapper
	 *            the string to wrap around the code (using the codePlaceholder)
	 * @param codePlaceholder
	 *            the identifier for the code
	 * @throws IOException
	 *             when the file cannot be written to.
	 */
	static void writeSourceMap(final File originalFile, final File outputFile,
	final File frameworkTargetDirectory, final Result result,
	final String wrapper, final String codePlaceholder)
	throws IOException {
		if (result.sourceMap != null) {
			boolean threw = true
			Files.touch(outputFile)
			StringWriter out = new StringWriter()
			FileWriter fOut = new FileWriter(outputFile)
			try {
				int pos = wrapper.indexOf(codePlaceholder)
				LOGGER.debug("wrapper = $wrapper")
				if (pos != -1) {
					String prefix = ''
					if (pos > 0) {
						prefix = wrapper[0..pos]
						LOGGER.debug("prefix = ${prefix}")
					}
					if (result != null && result.sourceMap != null) {
						result.sourceMap.setWrapperPrefix(prefix)
					}
				}

				// SourceMap relativeMap = Collections.result.sourceMap;

				result.sourceMap.appendTo(out, originalFile.name)

				String sourceMap = normalizeFilePaths(
						out,
						frameworkTargetDirectory)

				fOut.append(sourceMap)
				fOut.append(NEWLINE)
				threw = false
			} finally {
				Closeables.close(out, threw)
				Closeables.close(fOut, threw)
			}
		} else {
			LOGGER.warn('There is no source map present in the result!')
		}
	}

	/**
	 * Replaces the file paths in the source map with the actual paths.
	 * 
	 * @param out
	 *            The writer containing the source map.
	 * @param frameworkTargetDirectory
	 *            The current path to replace.
	 * @return the normalized source map
	 * @throws IOException if there is a problem reading the files 
	 * 
	 */
	private static String normalizeFilePaths(final StringWriter out,
	final File frameworkTargetDirectory) throws CompileException {
		StringBuffer sourceBuffer = out.buffer
		//Don't you just have to love windows!
		String sourceMap = sourceBuffer.toString().replace('\\\\', '\\')
		String relPath = FileToFile
				.getRelPathFromBase(
				JsarRelativeLocations
				.getCompileLocation(frameworkTargetDirectory),
				frameworkTargetDirectory)
		sourceMap = sourceMap
				.replace("${frameworkTargetDirectory.absolutePath}/",
				relPath)
		sourceMap
	}

	//	/**
	//	 * @return the generated assert javascript file
	//	 */
	//	private File getGeneratedAssertJS() {
	//		return new File(
	//		JsarRelativeLocations
	//		.getAssertDepsLocation(frameworkTargetDirectory),
	//		generatedAssertJS);
	//	}
	//
	//	/**
	//	 * @return the generated debug javascript file
	//	 */
	//	private File getGeneratedDebugJS() {
	//		return new File(
	//		JsarRelativeLocations
	//		.getDebugDepsLocation(frameworkTargetDirectory),
	//		generatedDebugJS);
	//	}
	//
	//	/**
	//	 * @return the generated test javascript file
	//	 */
	//	private File getGeneratedTestJS() {
	//		return new File(
	//		JsarRelativeLocations
	//		.getTestDepsLocation(frameworkTargetDirectory),
	//		generatedAssertJS);
	//	}
	//
	//	/**
	//	 * @return the generated assert requires javascript file
	//	 */
	//	private File getGeneratedAssertRequiresJS() {
	//		return new File(
	//		JsarRelativeLocations
	//		.getAssertRequiresLocation(frameworkTargetDirectory),
	//		generatedAssertRequiresJS);
	//	}
	//
	//	/**
	//	 * @return the generated debug requires javascript file
	//	 */
	//	private File getGeneratedDebugRequiresJS() {
	//		return new File(
	//		JsarRelativeLocations
	//		.getDebugRequiresLocation(frameworkTargetDirectory),
	//		generatedDebugRequiresJS);
	//	}
}
