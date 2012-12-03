package com.github.jlgrock.closuretoolslibrary.compiler

import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * A class that will scan all files provided and check for namespace,
 * goog.require, and goog.provides statements.
 */
final class AnnotationFileReader {

	/**
	 * Empty Constructor for utility class.
	 */
	private AnnotationFileReader() {}
	
	/**
	 * Regular expression to match the goog.requires statement.
	 */
	public static final Pattern REQ_REGEX = Pattern
			.compile('goog\\.require\\s*\\(\\s*[\\\'\\"]([^\\)]+)[\\\'\\"]\\s*\\)')

	/**
	 * Regular expression to match the goog.provides statement.
	 */
	public static final Pattern PROV_REGEX = Pattern
			.compile('goog\\.provide\\s*\\(\\s*[\\\'\\"]([^\\)]+)[\\\'\\"]\\s*\\)')

	/**
	 * Regular expression to match the namespace (ns) statement.
	 */
	public static final Pattern NS_REGEX = Pattern.compile('^ns:((\\w+\\.)*(\\w+))$')

	/**
	 * Parse a file for the google reqquires, provides, and namespace statements.
	 * 
	 * @param file
	 *            the file to scan
	 * @return the dependency info object, populated with the information
	 * @throws CompileException if file does not exist or parsing is not possible
	 */
	static DependencyInfo parseForDependencyInfo(final File file)
			throws CompileException {
		DependencyInfo dep = new DependencyInfo(file)
		FileInputStream filestream = null
		try {
			if (!file.exists() || !file.isFile()) {
				throw new CompileException("the File at location '${file.canonicalPath}' does not exist")
			}
			filestream = new FileInputStream(file)
			InputStream instream = new DataInputStream(filestream)
			BufferedReader br = new BufferedReader(new InputStreamReader(instream))

			String strLine
			while ((strLine = br.readLine()) != null) {
				Matcher matcher
				// goog.provides
				matcher = PROV_REGEX.matcher(strLine)
				if (matcher.lookingAt()) {
					for (int i = 0; i < matcher.groupCount(); i++) {
						String s = matcher.group(1)
						dep.addToProvides(s)
					}
				}
				// goog.requires
				matcher = REQ_REGEX.matcher(strLine)
				if (matcher.lookingAt()) {
					String s = matcher.group(1)
					dep.addToRequires(s)
				}

				// goog namespace
				matcher = NS_REGEX.matcher(strLine)
				if (matcher.lookingAt()) {
					String s = matcher.group(1)
					dep.addToNamespaces(s)
				}

			}
		} finally {
			if (filestream != null) {
				filestream.close()
			}
		}
		dep
	}
}
