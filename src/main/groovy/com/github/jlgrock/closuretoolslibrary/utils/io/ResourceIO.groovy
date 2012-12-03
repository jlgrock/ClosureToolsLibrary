package com.github.jlgrock.closuretoolslibrary.utils.io

import java.util.zip.ZipInputStream

import org.apache.log4j.Logger

/**
 * A general usage class for accessing and manipulating jar resources.
 */
final class ResourceIO {

	/**
	 * private constructor for util classes.
	 */
	private ResourceIO() {
	}

	/**
	 * The Logger.
	 */
	static final Logger LOGGER = Logger.getLogger(ResourceIO)

	/**
	 * Get an input stream for a resource file within the jar.
	 * 
	 * @param resourceLocation
	 *            the path in jndi to the resource
	 * @return the InputStream of that file
	 * @throws IOException
	 *             If the resource cannot be found or there was a problem
	 *             reading the file
	 */
	static InputStream getResourceAsStream(final String resourceLocation)
			throws IOException {
		ClassLoader cl = Thread.currentThread().contextClassLoader
		if (cl.getResource(resourceLocation) == null) {
			throw new IOException("Resource at location \"${resourceLocation}\" doesn't exist")
		}
		new BufferedInputStream(cl.getResourceAsStream(resourceLocation))
	}

	/**
	 * Get a zip input stream for a resource file within the jar.
	 * 
	 * @param resourceLocation
	 *            the path in jndi to the resource
	 * @return the ZipInputStream of that file
	 * @throws IOException
	 *             If the resource cannot be found or there was a problem
	 *             reading the file
	 */
	static ZipInputStream getResourceAsZipStream(
			final String resourceLocation) throws IOException {
		if (resourceLocation == null) {
			throw new IOException('resourceLocation cannot be null')
		}
		new ZipInputStream(getResourceAsStream(resourceLocation))
	}

	/**
	 * Copy any resource to an output location.
	 * 
	 * @param pathToResource
	 *            the path in jndi to the resource
	 * @param outputLocation
	 *            the location to copy the resource to
	 * @throws IOException
	 *             if there is a problem copying the resource
	 */
	static void copyResource(final String pathToResource,
			final File outputLocation) throws IOException {
		if (pathToResource == null) {
			throw new IOException('resourceStream cannot be null')
		}
		if (outputLocation == null) {
			throw new IOException('outputLocation cannot be null')
		}

		LOGGER.debug("copying resource from resource \"${pathToResource}\" to \"${outputLocation.absoluteFile}\".")
		try {
			InputStream resourceStream = ResourceIO
					.getResourceAsStream(pathToResource)
			DirectoryIO.genDir(outputLocation.parentFile)
			FileIO.copyStream(resourceStream, new FileOutputStream(
					outputLocation))
		} catch (IOException ioe) {
			LOGGER.error("There was a problem copying the resource from \"${pathToResource}\"" +
				" to location \"${outputLocation.absolutePath}\"")
			throw ioe
		}
	}
	
}
