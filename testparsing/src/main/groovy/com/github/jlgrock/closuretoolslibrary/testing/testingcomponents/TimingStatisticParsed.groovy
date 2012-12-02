package com.github.jlgrock.closuretoolslibrary.testing.testingcomponents


/**
 * The object representing the average time per test that it took to execute the test cases.
 * 
 * This would be something like the following:
 * 
 * 2 ms/test. 13 files loaded.
 *
 */
final class TimingStatisticParsed implements IParsedDivObject {
	/**
	 * the time that it took to execute the test.
	 */
	final int time
	
	/**
	 * the unit of measurement (ms, sec, etc.).
	 */
	final String unitOfMeasurement
	
	/**
	 * the number of files loaded.
	 */
	final int numFiles
	
	/**
	 * Constructor.
	 * 
	 * @param timeIn the time that it took to execute the test.
	 * @param unitOfMeasurementIn the unit of measurement (ms, sec, etc.).
	 * @param numFilesIn the number of files loaded.
	 */
	TimingStatisticParsed(final int timeIn, final String unitOfMeasurementIn, final int numFilesIn) {
		time = timeIn
		unitOfMeasurement = unitOfMeasurementIn
		numFiles = numFilesIn
	}

}
