package com.yunip.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourcesSetting {
	
	private static String properpath  = "yunipconfig.properties";
	private static String xmlpath  = "yunipconfig.xml";
	private static Properties props;

	static {
		props = new Properties();
		InputStream propIn = Thread.currentThread().getContextClassLoader().getResourceAsStream(properpath);
		try {
			props.load(propIn);
		} catch (IOException e) {				
			e.printStackTrace();
		}
		//readProperties(propFile);
	}
	public static String getSetting(String key) {
		return props.getProperty(key);
	}

	public static String getSetting(String key, String defVal) {
		String rtn = props.getProperty(key);
		if (rtn == null || rtn.length() == 0) {
			return defVal;
		}
		return rtn;
	}
	
	public static String getXmlPath(){
		return xmlpath;
	}
}
