package com.github.jlgrock.closuretoolslibrary.utils.io

import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger

import com.github.jlgrock.closuretoolslibrary.utils.io.readers.FilteredReader

/**
 * A general usage class for doing File manipulations.
 */
final class FileIO {
	/**
	 * The Logger.
	 */
	static final Logger LOGGER = Logger.getLogger(FileIO)

	/**
	 * The character representing a period.
	 */
	static final PERIOD = '.' as char
	
	/**
	 * Should not use constructor for utility class.
	 */
	private FileIO() {}
	
	/**
	 * Will copy content from an inputstream to an output stream, closing the files when it has completed.
	 * 
	 * @param from The expected file to read in from
	 * @param to The expected file to write to
	 * @throws IOException if there was any issues copying the resource
	 */
	static void copyStream(final InputStream from, final OutputStream to) throws IOException {
		LOGGER.debug('copying stream...')
		try {
		    IOUtils.copy(from, to)
		}  catch (IOException ioe) {
			LOGGER.error('There was a problem copying the resource.')
			throw ioe
		} finally {
		    IOUtils.closeQuietly(from)
		    IOUtils.closeQuietly(to)
		}
	}

	/**
	 * Take a bunch of readers (files or otherwise) and concatenate 
	 * their input together into one output file.
	 * 
	 * @param readers the readers to concatenate together
	 * @param outputFile the output file to write to
	 * @throws IOException if any problems occur during reading, writing, or concatenation
	 */
    void concatenateStreams(final Reader[] readers, final File outputFile) throws IOException {
    	if (outputFile == null) {
    		throw new IOException('outputFile is required for concatenation of files.')
    	}

    	LOGGER.debug('Begining stream concatenation')
    	List<Reader> readerList = Arrays.asList(readers)
    	FileWriter outWriter = null
    	
    	//create the output dir
    	DirectoryIO.genDir(outputFile.parentFile)
    	outputFile.createNewFile()
    	
    	//create the output file
    	try {
    		outWriter = new FileWriter(outputFile)
    	
	        for (Reader listItem : readerList) {
	        	//replace tabs with 4 spaces for linting
	        	FilteredReader fr = new FilteredReader(listItem, '\t', '    ')
	        	String line
	        	while (fr.readLine() != null) {
		    		line = fr.readLine()
		    		if (line != null) {
		    			outWriter.append("${line}\n")
		    		}
	        	}
	        	listItem.close()
	        }
    	} finally {
    		try {
    			outWriter.close()
    		} catch(Exception e) {
    			LOGGER.error('Unable to close file.  This should not matter.')
    		}
    	}
    }
    
    /**
     * Parses the extension of a file.
     * 
     * @param file the file to parse
     * @return the extension of the file
     */
    static String getFileExtension(final File file) {
    	file.name[file.name.lastIndexOf('.')..-1]
    }
    
    /**
     * Remove the extension from a file.
     * 
     * @param file the file to parse
     * @return the filename with the extension removed
     */
    static String removeFileExtension(final File file) {
    	String name = file.name
		String retVal = null
		if (PERIOD in name) {
			int pos = name.lastIndexOf(PERIOD as char)
			retVal = name[0..pos]
    	} else {
			retVal = name
    	}
    	retVal
    }
    
    /**
     * Change the extension of a file, based off of the period extension notation.
     * 
     * @param file the file that you would like to change the extension of
     * @param newExtension The extension that you would like to change it to
     * @throws IOException If the system is unable to rename the file
     */
    static void changeExtension(final File file, final String newExtension) throws IOException {
    	String newFilename = "${removeFileExtension(file)}${PERIOD}${newExtension}"
    	File file2 = new File(newFilename)
    	boolean success = file.renameTo(file2)
    	if (!success) {
    		throw new IOException("Could not rename file from \"${file.name}\" to \"${file2.name}\".")
    	}
    }
}
