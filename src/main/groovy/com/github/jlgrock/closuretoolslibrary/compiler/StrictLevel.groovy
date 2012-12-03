package com.github.jlgrock.closuretoolslibrary.compiler

import com.google.javascript.jscomp.CheckLevel
import com.google.javascript.jscomp.CompilerOptions
import com.google.javascript.jscomp.DiagnosticGroups


/**
 * Convert the warnings level to an Options object.
 * 
 */
enum StrictLevel {
	/**
	 * The predefined option that is verbose.
	 */
	VERBOSE

	/**
	 * Set one of the predefined Warning levels.
	 * @param options the predefined option
	 */
	void setOptionsForWarningLevel(final CompilerOptions options) {
		switch (this) {
		case VERBOSE:
			addVerboseWarnings(options)
			break
		default:
			throw new RuntimeException('Unknown warning level.')
		}
	}

	/**
	 * Add all the check pass that are possibly relevant to a non googler.
	 * 
	 * @param options
	 *            The CompilerOptions object to set the options on.
	 */
	private static void addVerboseWarnings(final CompilerOptions options) {
		options.with {
			checkTypes = true
			checkSuspiciousCode = true
			checkUnreachableCode = CheckLevel.ERROR
			checkControlStructures = true
			checkSuspiciousCode = true
			checkSymbols = true
			
			aggressiveVarCheck = CheckLevel.ERROR
			brokenClosureRequiresLevel = CheckLevel.ERROR
			checkGlobalNamesLevel = CheckLevel.ERROR
			checkGlobalThisLevel = CheckLevel.ERROR
			//options.setCheckMissingGetCssNameLevel(CheckLevel.ERROR);
			checkMissingReturn = CheckLevel.ERROR
			checkProvides = CheckLevel.ERROR
			checkRequires = CheckLevel.ERROR
			checkUnreachableCode = CheckLevel.ERROR
			
			setWarningLevel(DiagnosticGroups.MISSING_PROPERTIES,
					CheckLevel.ERROR)
			
			/**
			 * Important Warning Level Settings
			 */
			//Warnings when deprecated, private, or protected are violated.
			setWarningLevel(DiagnosticGroups.ACCESS_CONTROLS,
					CheckLevel.ERROR)
			
			//Warnings when private and protected are violated (seems to work the same as access controls).
			setWarningLevel(DiagnosticGroups.VISIBILITY, CheckLevel.ERROR)
			
			//Warnings when non-deprecated code accesses code that's marked deprecated
			setWarningLevel(DiagnosticGroups.DEPRECATED, CheckLevel.ERROR)
	
			/**
			 * All other Warning Level Settings
			 */
			setWarningLevel(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.CHECK_PROVIDES,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.CHECK_REGEXP, CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.CHECK_TYPES, CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.CHECK_USELESS_CODE,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.CHECK_VARIABLES,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.CONST, CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.CONSTANT_PROPERTY,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.DEBUGGER_STATEMENT_PRESENT,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.DUPLICATE_VARS,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.ES5_STRICT, CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.EXTERNS_VALIDATION,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.FILEOVERVIEW_JSDOC,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.GLOBAL_THIS, CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.INTERNET_EXPLORER_CHECKS,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.INVALID_CASTS,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.MISSING_PROPERTIES,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.NON_STANDARD_JSDOC,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.STRICT_MODULE_DEP_CHECK,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.TWEAKS, CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.TYPE_INVALIDATION,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.UNDEFINED_NAMES,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.UNDEFINED_VARIABLES,
					CheckLevel.ERROR)
			setWarningLevel(DiagnosticGroups.UNKNOWN_DEFINES,
					CheckLevel.ERROR)
		}
	}
}
