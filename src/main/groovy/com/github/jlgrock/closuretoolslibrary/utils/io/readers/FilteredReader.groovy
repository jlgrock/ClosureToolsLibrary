package com.github.jlgrock.closuretoolslibrary.utils.io.readers

/**
 * A reader for parsing through a stream and replacing one thing with another.
 */
class FilteredReader extends BufferedReader {
	/**
	 * The string to replace.
	 */
	final String originalString
	
	/**
	 * The string replacing the original string.
	 */
	final String replacementString

	/**
	 * The default buffer if none is provided.
	 */
	static final int DEFAULT_BUFFER_SIZE = 8192

	/**
	 * Constructor.
	 * 
	 * @param readerIn the reader to read from
	 * @param original the string to replace
	 * @param replacement the string replacing the original string
	 */
	FilteredReader(final Reader readerIn, final String original, final String replacement) {
		this(readerIn, original, replacement, DEFAULT_BUFFER_SIZE)
	}

	/**
	 * Constructor.
	 * 
	 * @param readerIn the reader to read from
	 * @param original the string to replace
	 * @param replacement the string replacing the original string
	 * @param size the size of the buffer, should the default not meet your needs
	 */
	FilteredReader(final Reader readerIn, final String original, final String replacement, final int size) {
		super(readerIn, size)
		originalString = original
		replacementString = replacement
	}

	@Override
	String readLine() throws IOException {
		def unfiltered = super.readLine()
		if (unfiltered != null) {
			unfiltered.replaceAll(originalString, replacementString)
		}
		unfiltered
	}
}
