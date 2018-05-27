package com.imarcats.internal.server.infrastructure.i18n.lookup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.imarcats.interfaces.client.v100.util.DataUtils;

/**
 * Represents a (Language) Property File  
 * @author Adam
 *  
 *  
 *  TODO: Fix parameter definitions Email I18N to be like GWT I18N Parameter '{0}' instead of {0}
 */
public class Properties {

    private Map<String, String> _propTable = new HashMap<String, String>();  
    
    private Properties() { /* can only be created loadProperties(...) */ }
    
    /**
     * Loads a Property file   
     * @param relativePropertyFilePath_
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Properties loadProperties (Class forClassLoader_, String relativePropertyFilePath_, String charsetName_) throws IOException  
    {  
        Properties result = new Properties();  
  
        InputStream stream = forClassLoader_.getResourceAsStream(relativePropertyFilePath_);  
        InputStreamReader reader = new InputStreamReader(stream, charsetName_);  
  
        StringBuffer sBuf = new StringBuffer();  
        char[] buff = new char[1024];  
  
        /* [2] */  
        int pos = reader.read(buff, 0, 1024);  
        while (pos != -1) {  
            sBuf.append(buff, 0, pos);  
            pos = reader.read(buff, 0, 1024);  
        }  
  
        /* [3] */  
        String[] lines = sBuf.toString().split("\n", 0); 
        for (int i = 0; i < lines.length; i++) {  
            String[] kv = chomp(lines[i]).split("=", 2);  
            if (kv.length == 1) {  
                result.setProperty(kv[0].trim(), "");  
            }  
            if (kv.length == 2) {  
                result.setProperty(kv[0].trim(), kv[1].trim());  
            }  
        }  
          
        return result;  
    }  
  
  
    public void setProperty (String key, String val)  
    {  
        this._propTable.put(key, val);  
    }  
  
    public String getProperty (String key)  
    {  
        return (String) this._propTable.get(key);  
    }  
    
    public String getPropertyFormated (String key, Object[] args_)  
    {  
   	 String str = this._propTable.get(key);  
   	 String strFormated = null;	
   	 if(str != null) {
   		 strFormated = format(str, args_);
   	 }
   	 
   	 return strFormated;
    } 
    
    public int getPropertyCount ()  
    {  
        return this._propTable.size();  
    }   
  
    public Set<String> getEnumeratedNames ()  
    {  
        return this._propTable.keySet();  
    }  
  
    public String[] getPropertyNames ()  
    {  
        String[] result = new String[this._propTable.size()];  
        int c = 0;  
        for (String key : _propTable.keySet()) {
            result[c] = key;  
            c++;  
        }  
        return result;  
    }  
	    
	/**
	 * chomp  method examines the input string and removes 
	 * any training newline ("\n") or return-newline ("\r\n")
	 * @param inStr
	 * @return
	 */
    private static String chomp (String inStr) {  
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
    
	private static String format(String str_, Object[] args_) {	
		String[] strs = StringUtils.split(str_, new char[] { '{', '}' }, 0);
		
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
}
