package com.github.jlgrock.closuretoolslibrary.utils.logging

import org.apache.log4j.Level
import org.apache.log4j.Logger

/**
 * An output stream that ports output to log4j. Especially useful when porting
 * System.err and System.out streams.
 * 
 */
final class Log4jOutputStream extends OutputStream implements Closeable {
	/**
	 * The character used as the line separator constant.
	 */
	static final String LINE_SEPERATOR = System
			.getProperty('line.separator')

	/**
	 * The boolean to keep track if the output stream has been closed.
	 */
	boolean isClosed = false

	/**
	 * The internal buffer where data is stored.
	 */
	byte[] buffer

	/**
	 * The number of bytes in the buffer.
	 */
	int count

	/**
	 * The initial number of bytes in the buffer.
	 */
	static final int DEFAULT_BUFFER_LENGTH = 1024

	/**
	 * The logger to log to.
	 */
	final Logger logger

	/**
	 * The level to use when writing to the logger.
	 */
	final Level level

	/**
	 * Constructor.
	 * 
	 * @param loggerIn
	 *            the logger to log output to
	 * @param levelIn
	 *            the level at which to log messages at
	 */
	Log4jOutputStream(final Logger loggerIn, final Level levelIn) {
		if (loggerIn == null || levelIn == null) {
			throw new IllegalArgumentException(
					'nether logger nor level are allowed to be null')
		}
		logger = loggerIn
		level = levelIn
		count = 0
		buffer = new byte[DEFAULT_BUFFER_LENGTH]
	}

	@Override
	void close() {
		flush()
		isClosed = true
	}

	@Override
	void write(final int b) throws IOException {
		if (isClosed) {
			throw new IOException('The stream has been closed.')
		}

		if (b == 0) {
			return
		}

		if (count == buffer.length) {
			int newBufLength = buffer.length + DEFAULT_BUFFER_LENGTH
			byte[] newBuf = new byte[newBufLength]
			System.arraycopy(buffer, 0, newBuf, 0, buffer.length)
			buffer = newBuf
		}

		buffer[count] = (byte) b
		count++
	}

	@Override
	void flush() {
		if (count == 0) {
			return
		}

		if (count == LINE_SEPERATOR.length()) {
			if (((char) buffer[0]) == LINE_SEPERATOR.charAt(0)
					&& ((count == 1) || ((count == 2) && ((char) buffer[1]) == LINE_SEPERATOR
							.charAt(1)))) {
				count = 0
				return
			}
		}

		byte[] theBytes = new byte[count]
		System.arraycopy(buffer, 0, theBytes, 0, count)
		logger.log(level, new String(theBytes))
		count = 0
	}
}
