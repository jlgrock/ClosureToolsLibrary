package com.github.jlgrock.closuretoolslibrary.utils.io

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.FileFileFilter
import org.apache.commons.io.filefilter.FileFilterUtils
import org.apache.commons.io.filefilter.IOFileFilter
import org.apache.log4j.Logger

/**
 * A general usage class for doing Directory manipulations.
 */
final class DirectoryIO {

	/**
	 * The Logger.
	 */
	static final Logger LOGGER = Logger.getLogger(DirectoryIO);

	/**
	 * Copy a directory, including only those that are visible.
	 * 
	 * @param srcDir
	 *            the directory to copy from
	 * @param destDir
	 *            the directory to copy to
	 * @throws IOException
	 *             if there are any problems copying the directory
	 */
	static void copyDirectory(final File srcDir, final File destDir)
			throws IOException {
		LOGGER.debug("Begin copy of source directory \"${srcDir.absolutePath}\" to destination \"${destDir.absoluteFile}\".")
		if (!srcDir.exists()) {
			throw new IOException("Directory at location \"${srcDir.absolutePath}\" does not exist.")
		}
		IOFileFilter filter = FileFileFilter.FILE
		filter = FileFilterUtils.or(DirectoryFileFilter.DIRECTORY, filter)
		FileUtils.copyDirectory(srcDir, destDir, filter)
	}

	/**
	 * Create a directory. If it is a file, it creates the parent directory. If
	 * the directory doesn't exist, create all directories until you reach that
	 * directory.
	 * 
	 * @param dir
	 *            the directory that you want to create.
	 * @throws IOException
	 *             If the file exists or the system is unable to make the
	 *             directories
	 */
	static void genDir(final File dir) throws IOException {
		if (dir.isFile()) {
			throw new IOException("Cannot create directory because the following is a file: '${dir}'")
		}
		
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IOException("Can not create dir '${dir}")
			}
		}
	}

	/**
	 * Will do a recursive deletion of all files and folders based off of a
	 * given directory.
	 * 
	 * @param dir
	 *            The directory to start the deleting
	 * @throws IOException
	 *             If exception occurs during the actual delete or if it is not
	 *             a directory
	 */
	static void recursivelyDeleteDirectory(final File dir)
			throws IOException {
		LOGGER.debug("starting delete of directory \"${dir.absoluteFile}\".")
		if (!dir.exists()) {
			LOGGER.debug('deletion of directory ignored as it does not exist.')
			return
		}
		if (!dir.isDirectory()) {
			throw new IOException("The path \"${dir.absolutePath}\" is not a valid directory and cannot be deleted.")
		}
		if (dir.exists()) {
			File[] files = dir.listFiles()
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					recursivelyDeleteDirectory(files[i])
				} else {
					files[i].delete()
				}
			}
		}
		boolean noProblems = dir.delete()
		if (!noProblems) {
			throw new IOException(
					"There was a problem deleting the directory \"${dir.absolutePath}\"")
		}
	}

	/**
	 * Should not use constructor for utility class.
	 */
	private DirectoryIO() {
	}
}
