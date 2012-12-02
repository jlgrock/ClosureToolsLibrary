package com.github.jlgrock.closuretoolslibrary.utils.pathing


/**
 * Stores the relative path of a file, while keeping the reference to the File
 * object.
 */
class RelativeFile {
	/**
	 * The original File reference.
	 */
	File file
	
	/**
	 * The relative path reference.
	 */
	String relPath

	/**
	 * The Constructor.
	 * 
	 * @param fileIn the original file reference
	 * @param relPathIn the relative path storage
	 */
	RelativeFile(final File fileIn, final String relPathIn) {
		this.file = fileIn
		if (relPathIn.startsWith(File.separator)) {
			this.relPath = relPathIn[1..relPathIn.size()]
		} else {
			this.relPath = relPathIn
		}
	}
}
