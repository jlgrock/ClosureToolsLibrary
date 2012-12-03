package com.github.jlgrock.closuretoolslibrary.testing.processors

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import org.apache.log4j.Logger

import com.github.jlgrock.closuretoolslibrary.testing.TestingConfiguration
import com.github.jlgrock.closuretoolslibrary.testing.generators.SuiteGenerator
import com.github.jlgrock.closuretoolslibrary.testing.testingcomponents.Parsed
import com.github.jlgrock.closuretoolslibrary.utils.io.DirectoryIO
import com.github.jlgrock.closuretoolslibrary.utils.pathing.JsarRelativeLocations

class FileHandler {
	/**
	 * The Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(FileHandler)

	final TestingConfiguration testingConfiguration;
	
	FileHandler(final TestingConfiguration testingConfigurationIn) {
		testingConfiguration = testingConfigurationIn;
	}
	/**
	 * Will generate the files used in the google testing.
	 *
	 * @return the set of files that were created
	 * @throws IOException
	 *             if there is a problem reading or writing to the files
	 */
	List<File> generateFiles() throws IOException {
		File testOutputDir = JsarRelativeLocations
				.getTestSuiteLocation(testingConfiguration.frameworkTargetDirectory)
		File testDepsDir = JsarRelativeLocations
				.getTestLocation(testingConfiguration.frameworkTargetDirectory)
		File depsFileLocation = JsarRelativeLocations
				.getTestDepsLocation(testingConfiguration.frameworkTargetDirectory)
		List<File> returnFiles = []

		DirectoryIO.recursivelyDeleteDirectory(testOutputDir)
		def fileLoc = "closureLibrarylocation.absoluteFile${File.separator}"+
			"closure${File.separator}goog${File.separator}base.js"
		File baseLocation = new File(fileLoc)

		LOGGER.debug('Generating Test Suite...')
		List<File> fileSet = calculateFileSet()
		List<File> testDeps = null//FileListBuilder.buildFilteredList(testDepsDir, "js")
		List<File> depsFileSet = null//FileListBuilder.buildFilteredList(depsFileLocation, "js")
		File depsFile = null
		if (depsFileSet.size() == 1) {
			depsFile = depsFileSet.toArray(new File[depsFileSet.size()])[0]
		} else {
			throw new IOException(
			"Could not find debug/deps file (or found more than one) at location '$depsFileLocation'.")
		}

		LOGGER {
			if (isDebugEnabled()) {
				debug("Files that will be included in testing: ${fileSet}")
				debug("Base Location: ${baseLocation}")
				debug("calc deps Location: ${depsFile}")
				debug("Testing Dependency Location: ${testDepsDir}")
				debug("Testing Source Directory: ${testSourceDirectory}")
			}
		}

		SuiteGenerator suite = new SuiteGenerator(testingConfiguration)

		returnFiles.addAll(suite.generateTestFiles(testingConfiguration.testSourceDirectory,
				testingConfiguration.testOutputDir))

		if (testingConfiguration.runTestsOnCompiled) {
			File testCompiledOutputDir = JsarRelativeLocations
					.getCompiledTestSuiteLocation(testingConfiguration.frameworkTargetDirectory)
			File compiledFile = new File(
					JsarRelativeLocations
					.getCompileLocation(testingConfiguration.frameworkTargetDirectory,
					testingConfiguration.compiledFilename))

			SuiteGenerator suiteCompiled = new SuiteGenerator(testingConfiguration)
			returnFiles.addAll(suiteCompiled.generateTestFiles(
					testingConfiguration.testSourceDirectory, testCompiledOutputDir))
		}

		LOGGER.with {
			if (isDebugEnabled()) {
				for (File file : returnFiles) {
					debug("filename: ${file.absolutePath}")
				}
				debug("baseLocation: ${baseLocation.absolutePath}")
				debug("testOutputDir: ${testOutputDir.absolutePath}")
			}
		}

		returnFiles
	}
	
	/**
	 * Gets the maximum number of configured test threads. If the configured
	 * value is &lt; 1, this method returns one less than the number of
	 * available processors, as returned by
	 * <code>Runtime.getRuntime().availableProcessors()</code>. Note that this
	 * calculation may change if this method is called multiple times as
	 * processors are made (un)available to this VM. This method will never
	 * return a value less than 1.
	 *
	 * @return the maximum number of test threads
	 */
	final int getMaxTestThreads() {
		int max
		int restrictedMax = Math.max(1, Runtime.runtime
				.availableProcessors() - 1)
		if (maxTestThreads < 1) {
			max = restrictedMax
		} else if (maxTestThreads > restrictedMax) {
			LOGGER.warn("A maximum of ${restrictedMax} test threads may be used on this system.  (${maxTestThreads} requested)")
			max = restrictedMax
		} else {
			max = maxTestThreads
		}
		max
	}

	/**
	 * Parse the files created.
	 *
	 * @param files
	 *            the files to parse
	 * @param maxThreads
	 *            the maximum number of threads to spawn for test execution
	 * @param maxFailures
	 *            the maximum number of failures to allow during the parsing.
	 * @param testTimeoutSeconds
	 *            the maximum number of seconds to execute before deciding that
	 *            a test case has failed.
	 * @return the set of test cases received from parsing
	 */
	static List<Parsed> parseFiles(final List<File> files,
	final int maxFailures, final long testTimeoutSeconds,
	final int maxThreads) {
		List<Parsed> failures = []
		int fileCount = (files != null ? files.size() : 0)
		int threadCount = Math.min(fileCount, maxThreads)
		LOGGER.info("Parsing ${fileCount} Test Files (${threadCount} threads)...")

		if (fileCount > 0) {
			// create a synchronized list so test threads can determine whether
			// or not
			// the maximum failure count has reached and threads do not attempt
			// simultaneous
			// writes to the underlying failures list; we do this separately so
			// we don't return
			// the synchronized list to the calling method
			List<Parsed> syncFailures = Collections
					.synchronizedList(failures)
			// initialize the thread pool for test execution, using a fixed-size
			// thread pool if multiple threads are specified and
			// a single-threaded pool if running in serial mode
			ExecutorService threadPool = (maxThreads > 1 ? Executors
					.newFixedThreadPool(threadCount) : Executors
					.newSingleThreadExecutor())
			// initialize the ParseRunner queue; one ParseRunner per thread
			BlockingQueue<ParseRunner> runnerQueue = new ArrayBlockingQueue<ParseRunner>(
					threadCount)
			for (int idx = 0; idx < threadCount; idx++) {
				runnerQueue.add(new ParseRunner(new UnitDriver(true),
						testTimeoutSeconds))
			}

			// the latch that will be used as the control gate to indicate tests
			// have completed
			CountDownLatch latch = new CountDownLatch(fileCount)

			for (File file : files) {
				Callable<Void> testTask = new Callable<Void>() {
							@Override
							Void call() throws Exception {
								// if we have reached the maximum number of failures,
								// return without testing
								if (maxFailures > 0) {
									synchronized (syncFailures) {
										if (syncFailures.size() > maxFailures) {
											latch.countDown()
											return null
										}
									}
								}

								// get the next available ParseRunner
								ParseRunner runner = runnerQueue.take()
								try {
									Parsed testCase = runner.parseFile(file)
									if (ResultType.PASSED != testCase.result) {
										synchronized (syncFailures) {
											syncFailures.add(testCase)
										}
									}
								} finally {
									if (runner != null) {
										runnerQueue.put(runner)
									}
									latch.countDown()
								}
								null
							}
						}
				threadPool.submit(testTask)
			}

			// stop the thread pool, preventing additional tasks from being
			// submitted
			threadPool.shutdown()
			// wait for all test cases to complete execution
			try {
				latch.await()
			} catch (InterruptedException ie) {
				// attempt to stop execution gracefully
				threadPool.shutdownNow()
			} finally {
				// clean up test resources
				if (runnerQueue.size() != threadCount) {
					throw new IllegalStateException('ParseRunners were not properly returned to the queue.')
				}
				while (!runnerQueue.isEmpty()) {
					runnerQueue.remove().quit()
				}
			}
		}
		failures
	}

	/**
	 * Will calculate the set of files.
	 *
	 * @return the file set
	 */
	private List<File> calculateFileSet() {
		LOGGER.info('Calculating File Set...')
		List<File> files = []
		files.addAll(FileListBuilder.buildFilteredList(
				testSourceDirectory, 'js'))
		if (testingConfiguration.includes != null) {
			files.addAll(testingConfiguration.includes)
		}
		if (testingConfiguration.excludes != null) {
			files.removeAll(testingConfiguration.excludes)
		}
		files
	}
}