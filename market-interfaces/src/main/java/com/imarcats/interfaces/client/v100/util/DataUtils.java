package com.imarcats.interfaces.client.v100.util;

/**
 * Data Related Utilities 
 * @author Adam
 *
 */
public class DataUtils {

	private static final String VALID_USER_ID_REGEXP = "[A-Za-z_0-9]*";
	public static final String VALID_USER_ID = "A..Z a..z _ 0..9";
	
	private static final String VALID_ID_REGEXP = "[A-Z_0-9]*";
	public static final String VALID_ID = "A..Z _ 0..9";
	
	private static final String VALID_MARKET_ID_REGEXP = "[A-Z_0-9\\.]*";
	public static final String VALID_MARKET_ID = "A..Z _ 0..9 .";
	
	private static final String VALID_STRING_REGEXP = "[0-9a-zA-Z\\-_\\.:\\,@\\s\\+/\\p{L}]*";
	public static final String VALID_STRINGS = "A..Z a..z 0..9 - _ . : , @ + /";

	protected DataUtils() { /* static utility class */}

	// if string contains any of these strings Market Security Exception will be raised
	// TODO: Add more !
	public static final String[] XSS_BLACKLIST = {
		"<script>"
	};
	
	public static final String XSS = "XSS";
	
	private static void checkForXss(String str_) {
		String strLower = str_.toLowerCase();
		for (int i = 0; i < XSS_BLACKLIST.length; i++) {
			if(strLower.contains(XSS_BLACKLIST[i])) {
				throw new IllegalArgumentException(XSS);
			}
		}
	}
	
	/**
	 * Tests if the String has only VALID_STRINGS
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 * 
	 * Reference: Based on https://www.owasp.org/index.php/XSS_%28Cross_Site_Scripting%29_Prevention_Cheat_Sheet
	 */
	public static boolean isValidOptionalString(String str_) {
		boolean valid = true;
		if(str_ != null) {
			checkForXss(str_);
			valid = str_.matches(VALID_STRING_REGEXP);
		}
		return valid;
	}
	
	/**
	 * Tests if the String is not null and not "" or not "    " and
	 * String has only VALID_STRINGS
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 */
	public static boolean isValidString(String str_) {
		return isNonNullString(str_) && isValidOptionalString(str_); 
	}
	
	/**
	 * Tests if the String is a valid email address
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 */
	public static boolean isValidEmail(String str_) {
		return isNonNullString(str_) && isValidOptionalString(str_) && str_.contains("@") && str_.contains(".");  
	}

	/**
	 * Tests if the String is not null and not "" or not "    "
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 */
	public static boolean isNonNullString(String str_) {
		return str_ != null && !"".equals(str_.trim());
	}
	
	/**
	 * Tests if the String a valid Market ID, it is not null and not "" or not "    " and 
	 * does not contain spaces, starts or ends with space like "ABC DEF", "ABC " or " ABC"
	 * it only contains Numbers (0..9), Capital Letters (A..Z), Underscore (_) or Dot (.)
	 * see 
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 */
	public static boolean isValidMarketIdString(String idStr_) {
		return isValidIdString(idStr_) && 
				idStr_.matches(VALID_MARKET_ID_REGEXP);
	}
	
	/**
	 * Tests if the String a valid Object ID, it is not null and not "" or not "    " and 
	 * does not contain spaces, starts or ends with space like "ABC DEF", "ABC " or " ABC"
	 * it only contains Numbers (0..9), Capital Letters (A..Z) or Underscore (_)
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 */
	public static boolean isValidObjectIdString(String idStr_) {
		return isValidIdString(idStr_) && 
				idStr_.matches(VALID_ID_REGEXP);
	}
	
	/**
	 * Tests if the String a valid User ID, it is not null and not "" or not "    " and 
	 * does not contain spaces, starts or ends with space like "ABC DEF", "ABC " or " ABC"
	 * it only contains Numbers (0..9), Capital Letters (A..Z), Letters (a..z) or Underscore (_)
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 */
	public static boolean isValidUserIdString(String idStr_) {
		return isValidIdString(idStr_) && 
				idStr_.matches(VALID_USER_ID_REGEXP);
	}
	
	/**
	 * Tests if the String a valid ID, it is not null and not "" or not "    " and 
	 * does not contain spaces, starts or ends with space like "ABC DEF", "ABC " or " ABC"
	 * @param str_ String to be Tested
	 * @return if string is valid 
	 */
	public static boolean isValidIdString(String idStr_) {
		return isValidString(idStr_) && 
				!idStr_.endsWith(" ") && 
				!idStr_.startsWith(" ") && 
				!idStr_.contains(" ");
	}
	
	/**
	 * Adjust Code to Market System Standard, Capitalizes and Trims it
	 * @param code_ Code 
	 * @return Adjusted Code
	 */
	public static String adjustCodeToStandard(String code_) {
		return code_.toUpperCase().trim();
	}
	
	/**
	 * Tells if 2 string are equals
	 * @param value1_
	 * @param value2_
	 * @return if they are equals
	 */
	public static boolean equals(String value1_, String value2_) {
		return value1_ != value2_ 
					? value1_ != null ? value1_.equals(value2_) : value2_ == null
					: true;
	}
}
