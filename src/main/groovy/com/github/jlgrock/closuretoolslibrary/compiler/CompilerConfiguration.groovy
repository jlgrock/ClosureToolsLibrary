package com.github.jlgrock.closuretoolslibrary.compiler


class CompilerConfiguration {
	/**
	 * The location of the closure library. By default, this is expected in the
	 * ${project.build.directory}${file.separator}javascriptFramework${file.
	 * separator}closure-library${file.separator}closure${file.separator}goog
	 * directory, as this is where the jsdependency plugin will put it by
	 * default. If you would like to override this and use your own location,
	 * this can be done by changing this path.
	 */
	File closureLibraryLocation

	/**
	 * The file produced after running the dependencies and files through the
	 * compiler.
	 */
	String compiledFilename

	/**
	 * Specifies the compiler level to use.
	 *
	 * Possible values are:
	 * <ul>
	 * <li>WHITESPACE_ONLY
	 * <li>SIMPLE_OPTIMIZATIONS
	 * <li>ADVANCED_OPTIMIZATIONS
	 * </ul>
	 * <br/>
	 *
	 * Please see the <a href=
	 * "http://code.google.com/closure/compiler/docs/compilation_levels.html"
	 * >Google Compiler levels page</a> for more details.
	 */
	String compileLevel = 'ADVANCED_OPTIMIZATIONS' //TODO change this to an enum

	/**
	 * Specifies how strict the compiler is at following the rules. The compiler
	 * is set to parameters matching SIMPLE by default, however, if you don't
	 * use WARNING or better, this isn't very useful. STRICT is set by default
	 * for this mojo.
	 *
	 * Possible values are:
	 * <ul>
	 * <li>SIMPLE
	 * <li>WARNING
	 * <li>STRICT
	 * </ul>
	 * <br/>
	 */
	ErrorLevel errorLevel = ErrorLevel.STRICT

	/**
	 * The file produced after running the dependencies and files through the
	 * compiler.
	 */
	File frameworkTargetDirectory

	/**
	 * The file produced after running the dependencies and files through the
	 * compiler.
	 */
	String generatedAssertJS

	/**
	 * The file produced that allows inclusion of assert into non-closure based
	 * systems.
	 */
	String generatedAssertRequiresJS

	/**
	 * The file produced after running the dependencies and files through the
	 * compiler.
	 */
	String generatedDebugJS

	/**
	 * The file produced that allows inclusion of debug into non-closure based
	 * systems.
	 */
	String generatedDebugRequiresJS

	/**
	 * Whether to generate the exports.
	 */
	boolean generateExports = true

	/**
	 * What to include based off of maven includes. If you are creating an API
	 * with an empty src folder, or you expect to use all of the maven
	 * dependencies as source, set this to ALL.
	 *
	 * Possible values are:
	 * <ul>
	 * <li>ALL
	 * <li>WHEN_IN_SRCS
	 * </ul>
	 * <br/>
	 */
	InclusionStrategy inclusionStrategy = InclusionStrategy.WHEN_IN_SRCS

	/**
	 * The default directory to extract files to. This likely shouldn't be
	 * changed unless there is a conflict with another plugin.
	 */
	File testSourceDirectory

	/**
	 * Will wrap the code in whatever you put in here. It uses '%output%' to
	 * define what your code is. An example of this would be
	 * "(function() {%output% window['my']['namespace'] = my.namespace;})();",
	 * which would wrap the entire code in an anonymous function.
	 */
	String outputWrapper = ''

	/**
	 * Whether to generate a SourceMap file or not.  A SourceMap file will help debug
	 * compiled code by relating it to the positions on the uncompiled code.
	 */
	boolean generateSourceMap = true
}
