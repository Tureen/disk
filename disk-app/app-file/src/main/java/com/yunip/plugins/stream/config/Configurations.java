package com.yunip.plugins.stream.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.yunip.constant.Constant;
import com.yunip.plugins.stream.constant.StreamConstant;

/**
 * read the configurations from file `config.properties`.
 */
public class Configurations {
	/*static final String CONFIG_FILE = "stream-config.properties";
	private static Properties properties = null;
	private static final String REPOSITORY = System.getProperty(
			"java.io.tmpdir", File.separator + "tmp" + File.separator
					+ "upload-repository");

	static {
		new Configurations();
	}

	private Configurations() {
		init();
		System.out.println("[NOTICE] File Repository Path ≥≥≥ " + getFileRepository());
	}

	void init() {
		try {
			ClassLoader loader = Configurations.class.getClassLoader();
			InputStream in = loader.getResourceAsStream(CONFIG_FILE);
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			System.err.println("reading `" + CONFIG_FILE + "` error!" + e);
		}
	}

	public static String getConfig(String key) {
		return getConfig(key, null);
	}

	public static String getConfig(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static int getConfig(String key, int defaultValue) {
		String val = getConfig(key);
		int setting = 0;
		try {
			setting = Integer.parseInt(val);
		} catch (NumberFormatException e) {
			setting = defaultValue;
		}
		return setting;
	}*/

	/**
	 * 文件上传存放目录
	 */
	/*public static String getFileRepository() {
		String val = getConfig("STREAM_FILE_REPOSITORY");
		if (val == null || val.isEmpty())
			val = REPOSITORY;
		return val;
	    return Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
	}*/
	
	/**
     * 文件上传存放目录
     */
    public static String getFileRepository(String token) {
        if(token != null && !token.equals("")){
            String[] tempToken = token.split("_");
            if(tempToken != null && tempToken.length == 2){
                String temp = tempToken[0];
                if(temp != null && !temp.equals("")){
                    return Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + temp;
                }
            }
        }
        return Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
    }

	/**
	 * 跨域地址
	 */
	public static String getCrossServer() {
		//return getConfig("STREAM_CROSS_SERVER");
	    return "";
	}
	
	/**
	 * 允许跨域规则
	 */
	public static String getCrossOrigins() {
		//return getConfig("STREAM_CROSS_ORIGIN");
	    return "";
	}
	
	/*public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(getConfig(key));
	}*/
	
	/**
	 * 文件上传完成后，是否删除
	 */
	public static boolean isDeleteFinished() {
	    return true;
		//return getBoolean("STREAM_DELETE_FINISH");
	}
	
	/**
	 * 是否跨域
	 */
	public static boolean isCrossed() {
	    return false;
		//return getBoolean("STREAM_IS_CROSS");
	}
}
