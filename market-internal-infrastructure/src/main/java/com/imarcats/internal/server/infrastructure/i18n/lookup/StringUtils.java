package com.imarcats.internal.server.infrastructure.i18n.lookup;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.interfaces.client.v100.util.DataUtils;

/** 
 * Utility Class for splitting and handling Strings
 * @author Adam 
 * 
 * Based on: http://penlets.com/tutorials/using_properties_files/
 */
public class StringUtils {
	
	private StringUtils() { /* utility class */ }
	
	public static String format(String str_, Object[] args_) {	
		String[] strs = split(str_, new char[] { '{', '}' }, 0);
		
		if(args_ != null) {
			for (int i = 0; i < args_.length; i++) {
				int index = (i + 1) * 2 - 1;
				if(index < strs.length) {
					strs[index] = args_[i].toString();
				}
			}
		} 
		
		String result = "";
		for (int i = 0; i < strs.length; i++) {
			result += strs[i];
		}
		
	    return result;
	}

	/**
	 * The  split  method presented here takes a string value, 
	 * a single character separator, and a maximum number of 
	 * values to return. This is similar to 
	 * the split method in the JRE, so it should look 
	 * pretty familiar.
	 * @param str
	 * @param sep
	 * @param maxNum
	 * @return
	 */
	public static String[] split (String str, char sep, int maxNum) {
		return split (str, new char[] { sep }, maxNum);
	}
	
	/**
	 * The  split  method presented here takes a string value, 
	 * a single character separator, and a maximum number of 
	 * values to return. This is similar to 
	 * the split method in the JRE, so it should look 
	 * pretty familiar.
	 * @param str
	 * @param seps
	 * @param maxNum
	 * @return
	 */
	public static String[] split (String str, char[] seps, int maxNum) {  
      if (str == null || str.length() == 0) {  /* [1] */  
          return new String[0];  
      }  

      /* [2] */  
      List<String> results = maxNum == 0 ? new ArrayList<String>() : new ArrayList<String>(maxNum);  
        
      StringBuffer buf = new StringBuffer();  
      for (int i = 0; i < str.length(); i++) { /* [3] */  
          char c = str.charAt(i);  

          if (find(c, seps)) {  
              if (maxNum != 0) { /* [4] */  
                  if ((results.size() + 1) == maxNum) {  
                      for (; i < str.length(); i++) {  
                          buf.append(str.charAt(i));  
                      }  
                      break;  
                  }  
              }  

              results.add(buf.toString());  
              buf.setLength(0);  
          }  
          else {  
              buf.append(c);  
          }  
      }  

      if (buf.length() > 0) {  
          results.add(buf.toString());  
      }  

      return results.toArray(new String[results.size()]); /* [5] */  	  
	}  

	private static boolean find(char sep, char[] seps) {
		boolean found = false; 
		for (int i = 0; i < seps.length; i++) {
			if(sep == seps[i]) {
				found = true;
				break;
			}
		}
		return found;
	}

	/**
	 * chomp  method examines the input string and removes 
	 * any training newline ("\n") or return-newline ("\r\n")
	 * @param inStr
	 * @return
	 */
    public static String chomp (String inStr) {  
         if (!DataUtils.isValidString(inStr)) {  
             return inStr;  
         }  
   
         char lastChar = inStr.charAt(inStr.length() - 1);  
         if (lastChar == 13) {  
             return inStr.substring(0, inStr.length() - 1);  
         }  
         else if (lastChar == 10) {  
             String tmp = inStr.substring(0, inStr.length() - 1);  
             if ("".equals(tmp)) {  
                 return tmp;  
             }  
             lastChar = tmp.charAt(tmp.length() - 1);  
             if (lastChar == 13) {  
                 return tmp.substring(0, tmp.length() - 1);  
             }  
             else {  
                 return tmp;  
             }  
         }  
         else {  
             return inStr;  
         }  
     }  
       
}
