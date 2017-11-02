/*
 * 描述：字符串处理工具类
 * 创建人：junbin.zhou
 * 创建时间：2012-8-14
 */
package com.yunip.utils.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
* 字符串处理工具类
*
* @author leizhimin 2008-12-15 22:40:12
*/
public final class StringUtil {
    /**
    * 将一个字符串的首字母改为大写或者小写
    *
    * @param srcString 源字符串
    * @param flag     大小写标识，ture小写，false大些
    * @return 改写后的新字符串
    */
    public static String toLowerCaseInitial(String srcString, boolean flag) {
        StringBuilder sb = new StringBuilder();
        if (flag) {
            sb.append(Character.toLowerCase(srcString.charAt(0)));
        } else {
            sb.append(Character.toUpperCase(srcString.charAt(0)));
        }
        sb.append(srcString.substring(1));
        return sb.toString();
    }

    /**
    * 将一个字符串按照句点（.）分隔，返回最后一段
    *
    * @param clazzName 源字符串
    * @return 句点（.）分隔后的最后一段字符串
    */
    public static String getLastName(String clazzName) {
        String[] ls = clazzName.split("\\.");
        return ls[ls.length - 1];
    }
    
    /**
     * 将一个字符串按照句点（.）分隔，返回最后一段
     *
     * @param clazzName 源字符串
     * @return 句点（.）分隔后的最后一段字符串
     */
     public static String getAllLastName(String clazzName) {
         int beginIndex = clazzName.lastIndexOf(".");
         if(beginIndex == -1){
             return "";
         }
         return clazzName.substring(beginIndex,clazzName.length());
     }
     
     /**
      * 将一个字符串按照句点（.）分隔，返回最后一段
      *
      * @param clazzName 源字符串
      * @return 句点（.）分隔后的最后一段字符串
      */
      public static String getFirstName(String clazzName) {
          int beginIndex = clazzName.lastIndexOf(".");
          if(beginIndex == -1){
              return clazzName;
          }
          return clazzName.substring(0,beginIndex);
      }

    /**
    * 格式化文件路径，将其中不规范的分隔转换为标准的分隔符,并且去掉末尾的"/"符号。
    *
    * @param path 文件路径
    * @return 格式化后的文件路径
    */
    public static String formatPath(String path) {
        String reg0 = "\\\\＋";
        String reg = "\\\\＋|/＋";
        String temp = path.trim().replaceAll(reg0, "/");
        temp = temp.replaceAll(reg, "/");
        if (temp.endsWith("/")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        if (System.getProperty("file.separator").equals("\\")) {
            temp = temp.replace('/', '\\');
        }
        return temp;
    }

    /**
    * 格式化文件路径，将其中不规范的分隔转换为标准的分隔符,并且去掉末尾的"/"符号(适用于FTP远程文件路径或者Web资源的相对路径)。
    *
    * @param path 文件路径
    * @return 格式化后的文件路径
    */
    public static String formatPath4Ftp(String path) {
        String reg0 = "\\\\＋";
        String reg = "\\\\＋|/＋";
        String temp = path.trim().replaceAll(reg0, "/");
        temp = temp.replaceAll(reg, "/");
        if (temp.endsWith("/")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        return temp;
    }

    /**
    * 获取文件父路径
    *
    * @param path 文件路径
    * @return 文件父路径
    */
    public static String getParentPath(String path) {
        return new File(path).getParent();
    }

    /**
    * 获取相对路径
    *
    * @param fullPath 全路径
    * @param rootPath 根路径
    * @return 相对根路径的相对路径
    */
    public static String getRelativeRootPath(String fullPath, String rootPath) {
        String relativeRootPath = null;
        String _fullPath = formatPath(fullPath);
        String _rootPath = formatPath(rootPath);

        if (_fullPath.startsWith(_rootPath)) {
            relativeRootPath = fullPath.substring(_rootPath.length());
        } else {
            throw new RuntimeException("要处理的两个字符串没有包含关系，处理失败！");
        }
        if (relativeRootPath == null)
            return null;
        else return formatPath(relativeRootPath);
    }

    /**
    * 获取当前系统换行符
    *
    * @return 系统换行符
    */
    public static String getSystemLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
    * 将用“|”分隔的字符串转换为字符串集合列表，剔除分隔后各个字符串前后的空格
    *
    * @param series 将用“|”分隔的字符串
    * @return 字符串集合列表
    */
    public static List<String> series2List(String series) {
        return series2List(series, "\\|");
    }

    /**
    * 将用正则表达式regex分隔的字符串转换为字符串集合列表，剔除分隔后各个字符串前后的空格
    *
    * @param series 用正则表达式分隔的字符串
    * @param regex 分隔串联串的正则表达式
    * @return 字符串集合列表
    */
    private static List<String> series2List(String series, String regex) {
        List<String> result = new ArrayList<String>();
        if (series != null && regex != null) {
            for (String s : series.split(regex)) {
                if (s.trim() != null && !s.trim().equals(""))
                    result.add(s.trim());
            }
        }
        return result;
    }

    /**
    * @param strList 字符串集合列表
    * @return 通过“|”串联为一个字符串
    */
    public static String list2series(List<String> strList) {
        StringBuffer series = new StringBuffer();
        for (String s : strList) {
            series.append(s).append("|");
        }
        return series.toString();
    }
    
    /**
     * 将List字符串集合转化为通过固定字符组成的字符串
     * @param strList 字符串集合列表
     * @param fixedChar 固定字符串串(如：“|”, “,”)
     * @return 通过“|”串联为一个字符串
     */
    public static String list2series(List<String> strList, String fixedChar) {
        String result = "";
        if(strList == null){
            return result;
        }
        StringBuffer series = new StringBuffer();
        for (String s : strList) {
            series.append(s).append(fixedChar);
        }
        if(!series.equals("") && series.length() > 1){
            result = series.substring(0, series.length() - 1);
        }
        return result;
    }

    /**
    * 将字符串的首字母转为小写
    *
    * @param resStr 源字符串
    * @return 首字母转为小写后的字符串
    */
    public static String firstToLowerCase(String resStr) {
        if (resStr == null) {
            return null;
        } else if ("".equals(resStr.trim())) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            Character c = resStr.charAt(0);
            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c))
                    c = Character.toLowerCase(c);
                sb.append(resStr);
                sb.setCharAt(0, c);
                return sb.toString();
            }
        }
        return resStr;
    }

    /**
    * 将字符串的首字母转为大写
    *
    * @param resStr 源字符串
    * @return 首字母转为大写后的字符串
    */
    public static String firstToUpperCase(String resStr) {
        if (resStr == null) {
            return null;
        } else if ("".equals(resStr.trim())) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            Character c = resStr.charAt(0);
            if (Character.isLetter(c)) {
                if (Character.isLowerCase(c))
                    c = Character.toUpperCase(c);
                sb.append(resStr);
                sb.setCharAt(0, c);
                return sb.toString();
            }
        }
        return resStr;
    }

    /**
     * 去除标签
     * HtmlText(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param inputString
     * @return  
     * String 
     * @exception
     */
    public static String HtmlText(String inputString) {
        String htmlStr = inputString; //含html标签的字符串 
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> } 
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> } 
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式 

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签 

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签 

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签 

            /* 空格 ——   */
            // p_html = Pattern.compile("\\ ", Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = htmlStr.replaceAll(" ", " ");

            textStr = htmlStr;

        } catch (Exception e) {}
        return textStr;
    }

    /**
     * Check a string null or blank.
     * @param param string to check
     * @return boolean
     */
    public static boolean nullOrBlank(String param) {
        return (param == null || param.trim().equals("")) ? true : false;
    }

    public static String notNull(String param) {
        return param == null ? "" : param.trim();
    }
    
    /***
     * 判断该字符是否为数字
     * @param param
     * @return  
     * String 
     * @exception
     */
    public static boolean isNumber(String param) {
        return param.matches("[0-9]+"); 
    }
    
    /**
     * 首字母大写
     * @param name
     * @return  
     * String 
     * @exception
     */
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    public static void main(String[] args) {
        /*List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println(list2series(list, "|"));*/
        
        //System.out.println(System.getProperty("file.separator"));
        //System.getProperties();
        //Properties p = System.getProperties();
        //System.out.println(formatPath("C:///\\xxxx\\\\\\\\\\///\\\\R5555555.txt"));

        // List<String> result = series2List("asdf | sdf|siii|sapp|aaat| ", "\\|");
        // System.out.println(result.size());
        // for (String s : result) {
        //     System.out.println(s);
        // }
        String expressionFri = "if(true){if(!path.contains(\"license\")&&CheckFilterUtil.isVerificationJs(path)){saveMap.put(\"firstKey\",\"qwedcvgh\");saveMap.put(\"diskException\",License.checkLicenseCode());if(saveMap.get(\"diskException\")!=null){request.setAttribute(\"exception\", diskException);request.getRequestDispatcher(\"/WEB-INF/view/license/index.jsp\").forward(request,response);saveMap.put(\"licenseIndex\",true);}else{saveMap.put(\"licenseIndex\",false);}}else{saveMap.put(\"firstKey\",\"qwedcvgh\");saveMap.put(\"licenseIndex\",false);}if(!saveMap.get(\"licenseIndex\")){if(path.equals(\"/\")){response.sendRedirect(contextPath+\"/login/index\");saveMap.put(\"chainBoolean\",false);}else{saveMap.put(\"secondIndex\", true);if(StringUtil.contrast(BasicsBool.YES.getBool(),SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENAD.getKey()))&&CheckFilterUtil.isVerification(path)){saveMap.put(\"employee\",request.getSession().getAttribute(SysContant.EMPLOYEE_IN_SESSION));request.getSession().getAttribute(SysContant.EMPLOYEE_IN_SESSION);if(saveMap.get(\"employee\")==null){saveMap.put(\"loginFlag\",false);saveMap.put(\"loginFlag\",(ntlm=loginService.negotiate(request,response,false))!=null);if(saveMap.get(\"loginFlag\") == null){logger.info(\"域账户：\" + ntlm.getUsername() + \"已登录!\");adminQuery.setAdName(ntlm.getUsername());admin = adminService.getLoginAdmin(adminQuery);if(admin.getId() == null){admin.setAdName(ntlm.getUsername());adminService.saveOrUpdate(admin);request.setAttribute(\"admin\", admin);request.getRequestDispatcher(\"/WEB-INF/view/adyu/register.jsp\").forward(request,response);}else{if(employeeService.getEmployeeById(admin.getId()) == null){request.setAttribute(\"admin\", admin);request.getRequestDispatcher(\"/WEB-INF/view/adyu/register.jsp\").forward(request,response);}else{CheckFilterUtil.checkNameSpace(path, request);request.getSession().setAttribute(SysContant.EMPLOYEE_IN_SESSION, employee);}}}else{saveMap.put(\"chainBoolean\",false);saveMap.put(\"chainOpenBoolean\",false);}}if(saveMap.get(\"chainOpenBoolean\")==null?true:saveMap.get(\"chainOpenBoolean\")){CheckFilterUtil.checkNameSpace(path, request);chain.doFilter(CheckFilterUtil.regetNum(request, ntlm),response);}}else{if(CheckFilterUtil.isVerification(path)){CheckFilterUtil.checkNameSpace(path, request);saveMap.put(\"employee\",request.getSession().getAttribute(SysContant.EMPLOYEE_IN_SESSION));saveMap.put(\"requestType\",request.getHeader(\"X-Requested-With\")==null?\"\":request.getHeader(\"X-Requested-With\"));saveMap.put(\"accept\",request.getHeader(\"Accept\")==null?\"\":request.getHeader(\"Accept\"));if(saveMap.get(\"employee\") == null){if(StringUtil.contrast(\"XMLHttpRequest\",saveMap.get(\"requestType\")) && (StringUtil.contains(saveMap.get(\"accept\"),\"json\"))){error.put(\"code\", DiskException.LOGINFAILURE.getCode());error.put(\"codeInfo\", DiskException.LOGINFAILURE.getMsg());errorJson = JsonUtils.getJSONString(error);response.getOutputStream().write(errorJson.getBytes(\"UTF-8\"));response.setContentType(\"text/json; charset=UTF-8\");}else if(StringUtil.contrast(\"XMLHttpRequest\",saveMap.get(\"requestType\")) && !(StringUtil.contains(saveMap.get(\"accept\"),\"json\"))){request.getRequestDispatcher(\"/WEB-INF/view/common/ajaxsessionout.jsp\").forward(request,response);} else {response.sendRedirect(contextPath + \"/login/index\");}saveMap.put(\"chainBoolean\",false);}else{if(StringUtil.contrast(BasicsBool.YES.getBool(),SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.SINGLELOGIN.getKey()))){if(OnlineUserService.getOnlineUserBySessionId(request.getSession().getId()) == null){if(StringUtil.contrast(\"XMLHttpRequest\",saveMap.get(\"requestType\")) && (StringUtil.contains(saveMap.get(\"accept\"),\"json\"))){error.put(\"code\", DiskException.OFFLINENOTIFICATION.getCode());error.put(\"codeInfo\", DiskException.OFFLINENOTIFICATION.getMsg());errorJson = JsonUtils.getJSONString(error);response.getOutputStream().write(errorJson.getBytes(\"UTF-8\"));response.setContentType(\"text/json; charset=UTF-8\");}}else{response.sendRedirect(contextPath + \"/login/singleOutLogin\");}saveMap.put(\"chainBoolean\",false);}}}}}if(saveMap.get(\"chainBoolean\")==null?true:saveMap.get(\"chainBoolean\")){chain.doFilter(request, response);}}}";
        System.out.println(expressionFri.replace(" ", "") );
    }
    
    public static boolean contrast(String a, String b){
        return a.equals(b);
    }
    
    public static boolean contains(String a, String b){
        return a.contains(b);
    }
    
    
}
