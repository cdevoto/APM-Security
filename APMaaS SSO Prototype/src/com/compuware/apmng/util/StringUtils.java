package com.compuware.apmng.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {
	private static String fpRegex;
	private static String intRegex;
	
	static {
		initIntRegEx();
		initFpRegEx();
	}

	private static void initIntRegEx() {
	    final String Digits     = "(\\p{Digit}+)";
	    final String HexDigits  = "(\\p{XDigit}+)";
	
	    intRegex    =
	            ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
	             "[+-]?(" + // Optional sign character
	             "NaN|" +           // "NaN" string
	             "Infinity|" +      // "Infinity" string
	    
	         // Digits
	         Digits + "|" +
	         "0[xX]"+HexDigits+")" +
	         "[\\x00-\\x20]*");// Optional trailing "whitespace"
	        
	}

	private static void initFpRegEx() {
	    final String Digits     = "(\\p{Digit}+)";
	    final String HexDigits  = "(\\p{XDigit}+)";
	    // an exponent is 'e' or 'E' followed by an optionally 
	    // signed decimal integer.
	    final String Exp        = "[eE][+-]?"+Digits;
	    fpRegex    =
	        ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
	         "[+-]?(" + // Optional sign character
	         "NaN|" +           // "NaN" string
	         "Infinity|" +      // "Infinity" string
	
	         // A decimal floating-point string representing a finite positive
	         // number without a leading sign has at most five basic pieces:
	         // Digits . Digits ExponentPart FloatTypeSuffix
	         // 
	         // Since this method allows integer-only strings as input
	         // in addition to strings of floating-point literals, the
	         // two sub-patterns below are simplifications of the grammar
	         // productions from the Java Language Specification, 2nd 
	         // edition, section 3.10.2.
	
	         // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
	         "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
	
	         // . Digits ExponentPart_opt FloatTypeSuffix_opt
	         "(\\.("+Digits+")("+Exp+")?)|"+
	
	   // Hexadecimal strings
	   "((" +
	    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
	    "(0[xX]" + HexDigits + "(\\.)?)|" +
	
	    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
	    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
	
	    ")[pP][+-]?" + Digits + "))" +
	         "[fFdD]?))" +
	         "[\\x00-\\x20]*");// Optional trailing "whitespace"
	}

	public static List<String> split (String s, String delim) {
		List<String> result = new ArrayList<String>();
		StringBuilder buf = new StringBuilder();
	    int length = s.length();
	    int wordLength = 0;
	    for (int i = 0; i < length; i++) {
	    	char c = s.charAt(i);
	    	if (delim.indexOf(c) != -1) {
	    		if (wordLength > 0) {
	    		    result.add(buf.toString().trim());
	    		    wordLength = 0;
	    		    buf = new StringBuilder();
	    		}
	    	} else {
	    		wordLength++;
	    		buf.append(c);
	    	}
	    }
	    result.add(buf.toString().trim());
	    return result;
	}
	
	public static Object parseValue (String value) {
		if ("true".equals(value) || "false".equals(value)) {
			return Boolean.valueOf(value);
		}
            
		if (Pattern.matches(intRegex, value)) {
            return Long.valueOf(value); 
        }

		if (Pattern.matches(fpRegex, value)) {
            return Double.valueOf(value); 
        }

		return value;
	}
		
	private StringUtils () {}
	
}
