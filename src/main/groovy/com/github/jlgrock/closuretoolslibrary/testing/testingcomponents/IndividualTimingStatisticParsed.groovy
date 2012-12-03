package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents


/**
 * The object containing timing statistics for the Test Case.
 * 
 * Parses something like the following:
 * 
 * 2 of 2 tests run in 28ms.
 * 
 */
final class IndividualTimingStatisticParsed implements IParsedDivObject {
	/**
	 * the number of test cases run.
	 */
	int numberRun
	
	/**
	 * the number of total test cases (run, or not run).
	 */
	int numberTotal
	
	/**
	 * the time that it took to run.
	 */
	int timeValue
	
	/**
	 * the units applied to the time (ms, sec, etc.).
	 */
	String timeUnits

	/**
	 * Constructor.
	 * 
	 * @param numberRunIn the number of test cases run.
	 * @param numberTotalIn the number of total test cases (run, or not run).
	 * @param timeValueIn the time that it took to run.
	 * @param timeUnitsIn the units applied to the time (ms, sec, etc.).
	 */
	IndividualTimingStatisticParsed(final int numberRunIn, final int numberTotalIn,
			final int timeValueIn, final String timeUnitsIn) {
		numberRun = numberRunIn
		numberTotal = numberTotalIn
		timeValue = timeValueIn
		timeUnits = timeUnitsIn
	}
}
