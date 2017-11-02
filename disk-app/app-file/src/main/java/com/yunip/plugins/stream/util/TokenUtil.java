package com.yunip.plugins.stream.util;

import java.io.IOException;

import com.yunip.utils.pwd.Md5;

/**
 * Key Util: 1> according file name|size ..., generate a key;
 * 			 2> the key should be unique.
 */
public class TokenUtil {

	/**
	 * 生成Token， A(hashcode>0)|B + |name的Hash值| +_+size的值
	 * @param name
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public static String generateToken(String name, String size)
			throws IOException {
		if (name == null || size == null)
			return "";
		int code = name.hashCode();
		try {
			String token = (code > 0 ? "A" : "B") + Math.abs(code) + "_" + size.trim();
			/** TODO: store your token, here just create a file */
			IoUtil.storeToken(token);
			
			return token;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * 根据文件属性生成Token
	 * (这里描述这个方法适用条件 – 可选) 
	 * @param name 文件名
	 * @param id 操作人ID
	 * @param lastModifiedDate 文件最后修改时间
	 * @param size 文件大小
	 */
	public static String generateToken(String name, String size, String operateId, String lastModifiedDate)
            throws IOException {
        if (name == null || size == null || operateId == null || lastModifiedDate == null)
            return "";
        //int code = name.hashCode();
        try {
            //String token = (code > 0 ? "A" : "B") + Math.abs(code) + operateId + lastModifiedDate + "_" + size.trim();
            String token = Md5.encodeByMd5(operateId + name + lastModifiedDate) + "_" + size.trim();
            /** TODO: store your token, here just create a file */
            IoUtil.storeToken(token);
            
            return token;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
