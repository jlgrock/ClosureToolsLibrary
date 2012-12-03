package com.github.jlgrock.closuretoolslibrary.compiler

import com.github.jlgrock.closuretoolslibrary.utils.pathing.FileToFile

/**
 * A file that has been scanned for dependency information. This will keep
 * information about provides, requires, and namespace objects.
 * 
 */
final class DependencyInfo implements Comparable<DependencyInfo> {
	/**
	 * The file that was scanned.
	 */
	File file

	/**
	 * The provides statements found within the file.
	 */
	Set<String> provides

	/**
	 * The requires statements found within the file.
	 */
	Set<String> requires

	/**
	 * The namespace statements found within the file.
	 */
	Set<String> namespaces

	/**
	 * Constructor.
	 * 
	 * @param parsedFile the file to scan
	 */
	DependencyInfo(final File parsedFile) {
		this.file = parsedFile
		this.provides = [:] as Set
		this.requires = [:] as Set
	}

	/**
	 * the file, relative path from the base.
	 * 
	 * @param basePath
	 *            the base to build the relative string off of
	 * @return the relative path
	 * @throws IOException
	 *             if either of the files cannot be found
	 */
	String toDepsString(final File basePath) throws IOException {
		String filePath = ''
		if (basePath == null) {
			filePath = file.canonicalPath
		} else {
			try {
				filePath = FileToFile.getRelPathFromBase(basePath, file)
			} catch (IOException e) {
				// If there is an exception, set the file path to be blank
				filePath = '' 
			}
		}
		"goog.addDependency('${filePath}', ${providesString}, ${requiresString});"
	}

	/**
	 * the file, relative path from the base.
	 * 
	 * @param basePath
	 *            the base to build the relative string off of
	 * @return the relative path
	 * @throws IOException
	 *             if either of the files cannot be found
	 */
	String toRequiresString(final File basePath) throws IOException {
		StringBuilder requiresString = new StringBuilder()
		for (String provide : provides) {
			requiresString.append("goog.require(\"${provide}\");\n")
		}
		requiresString.toString()
	}

	/**
	 * Add a provides to the current file scan.
	 * 
	 * @param namespace
	 *            the provides namespace to add
	 */
	void addToProvides(final String namespace) {
		provides.add(namespace)
	}

	/**
	 * Add a requires to the current file scan.
	 * 
	 * @param namespace
	 *            the requires namespace to add
	 */
	void addToRequires(final String namespace) {
		requires.add(namespace)
	}

	/**
	 * Add a namespace to the current file scan.
	 *
	 * @param namespace
	 *            the namespace to add
	 */
	void addToNamespaces(final String namespace) {
		namespaces.add(namespace)
	}
	
	/**
	 * Return the filename of the file that is scanned.
	 * 
	 * @return the filename of the file that is scanned
	 */
	final String getFilename() {
		file.name
	}

	/**
	 * Build a string that can be used to show all requirements. This will match
	 * the google standard for dependency files.
	 * 
	 * @return the string of provides
	 */
	String getProvidesString() {
		convertArrayToGoogString(provides)
	}

	/**
	 * Build a string that can be used to show all requirements. This will match
	 * the google standard for dependency files.
	 * 
	 * @return the string of requirements
	 */
	String getRequiresString() {
		convertArrayToGoogString(requires)
	}

	/**
	 * Build a string that can be used to show all namespaces.
	 *
	 * @return the string of namespaces
	 */
	String getNamespaceString() {
		convertArrayToGoogString(namespaces)
	}
	
	static String convertArrayToGoogString(final Collection<String> elements) {
		StringBuilder elementsString = new StringBuilder()
		int i = 0
		elementsString.with {
			append('[')
			for (String element : elements) {
				if (i > 0) {
					append(', ')
				}
				append('\'')
				append(element)
				append('\'')
				i++
			}
			append(']')
		}
		elementsString.toString()
	}
	
	@Override
	int compareTo(final DependencyInfo o) {
		// if you require something of me, you are less than me
		int returnVal = 0
		for (String require : requires) {
			if (o.provides.contains(require)) {
				returnVal = -1
			}
		}
		for (String provide : provides) {
			if (o.requires.contains(provide)) {
				returnVal = 1
			}
		}
		// TODO we haven't factored this in yet...
		// for (String namespace : this.getNamespaces()) {
		// if (o.getRequires().contains(namespace)) {
		// returnVal = 1;
		// }
		// }
		returnVal
	}
}
