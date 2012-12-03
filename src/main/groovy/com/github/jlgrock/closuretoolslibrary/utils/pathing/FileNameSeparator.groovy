package com.github.jlgrock.closuretoolslibrary.utils.pathing

/**
 * A class used to separate the file into its component parts.
 *
 */
class FileNameSeparator {
	/**
	 * The path to the file.
	 */
	String path
	
	/**
	 * The file name.
	 */
	String name
	
	/**
	 * The file extension.
	 */
	String extension

	/**
	 * Constructor for files.
	 * 
	 * @param fileIn the file 
	 */
	FileNameSeparator(final File fileIn) {
		if (fileIn == null) {
			path = ''
			name = ''
			extension = ''
		} else {
			path = fileIn.parentFile.absolutePath
			parseFilename(fileIn.name)
		}
	}

	/**
	 * Constructor for string representation of a file. This should be the
	 * absolute path
	 * 
	 * @param absoluteFile
	 *            the absolute file path to parse
	 */
	FileNameSeparator(final String absoluteFile) {
		this(new File((absoluteFile == null) ? '' : absoluteFile))
	}

	/**
	 * Parse the filename into its sub-parts.
	 * 
	 * @param filename
	 *            the filename to parse
	 */
	void parseFilename(final String filename) {
		int index = filename.lastIndexOf('.')
		int length = filename.size()
		if (index == length || index == -1) {
			name = filename
			extension = ''
		} else {
			name = filename[0..index]
			extension = filename[index+1..length]
		}
	}
}
