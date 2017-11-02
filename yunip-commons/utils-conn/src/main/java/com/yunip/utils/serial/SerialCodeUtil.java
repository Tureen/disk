package com.yunip.utils.serial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.yunip.utils.util.StringUtil;

public class SerialCodeUtil {
    
    
    /****
     * getSonFolderIds(h) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param parentId
     * @return  
     * List<String> 
     * @exception
     */
    private static List<String> getSonFolderCodes(String parentCode){
        List<String> allFolderCodes = new ArrayList<String>();
        for(int i = 1 ; i<=9999 ; i ++ ){
            StringBuffer idBuffer = new StringBuffer();
            idBuffer.append(parentCode);
            if(i < 10000 && i >= 1000 ){
                idBuffer.append(i);
            } else if(i < 1000 && i >= 100 ){
                idBuffer.append(0);
                idBuffer.append(i);
            } else if(i < 100 && i >= 10 ){
                idBuffer.append("00");
                idBuffer.append(i);
            }else if(i < 10 && i >= 0 ){
                idBuffer.append("000");
                idBuffer.append(i);
            }
            allFolderCodes.add(idBuffer.toString());
        }
        return allFolderCodes;
    }
 
    /**
     * 添加部门的最新ID
     * @param serialCode 序列号
     * @return
     */
    public static String getChildDeptId(String code,String version) {
        String beforeLastId = "";
        if(StringUtils.isNotBlank(code)){
            String lastId = code.substring(
                    code.length() - 2);
            Integer inLastId = Integer.parseInt(lastId);
            inLastId += 1;
            String lastDeptId = inLastId.toString();
            if (inLastId < 10) {
                lastDeptId = "0" + inLastId;
            }
            beforeLastId = version + lastDeptId;
        } else {
            beforeLastId = version + "01";
        }
        return beforeLastId;
    }
    
    /***
     * getFolderId(获取新建文件夹的ID) 
     * @param folderIds  当前父文件已经存在的子文件编号列表
     * @param parentId   当前父文件ID
     * @return  
     * String 
     * @exception
     */
    public static String getFolderCode(List<String> folderCodes, String parentCode){
        List<String> allSonFolderCodes = getSonFolderCodes(parentCode);
        allSonFolderCodes.removeAll(folderCodes);
        if(allSonFolderCodes.size() > 0){
           return allSonFolderCodes.get(0);
        }
        return null;   
    }
    
    
    /***
     * 根据文件后置版本号给出最新的版本号
     * @param fbVersions
     * @param isVersion   是否已经存在版本号（包含（））
     * @return  
     * Integer 
     * @exception
     */
    public static Integer getFbVerson(List<Integer> fbVersions ,boolean isVersion){
       Collections.sort(fbVersions);
       int maxFbVesions = fbVersions.get(fbVersions.size() - 1);
       List<Integer> allFbVersion = new ArrayList<Integer>();
       for(int i = 0; i < maxFbVesions ; i++){
           allFbVersion.add(i);
       }
       if(isVersion){
           fbVersions.add(0);
       }
       allFbVersion.removeAll(fbVersions);
       if(allFbVersion.size() == 0){
           return maxFbVesions + 1;
       }
       return allFbVersion.get(0);
    }
    
    
    public static String getRealName(String name, List<String> oldNames){
        int index = 0;
        for(String oldName : oldNames){
            if(name.equals(oldName)){
                index = index + 1;
                String newName = name + "("+ index +")";
                return toGetRealName(index ,name , newName, oldNames);
            }
        }
        return name;
    }
    
    public static String toGetRealName(int index, String name, String newName, List<String> oldNames){
        for(String oldName : oldNames){
            if(newName.equals(oldName)){
                index = index + 1;
                newName = name + "("+ index +")";
                return toGetRealName(index, name, newName, oldNames);
            }
        }
        return newName;
    }
    
    /**获取同名的数据**/
    public static String getName(String name, List<String> oldNames){
        return getRealName(name, oldNames);
        //判断是否已经包含保本号
     /*   List<Integer> versions = new ArrayList<Integer>(); 
        if(oldNames != null && oldNames.size() > 0){
            for(String oldName : oldNames){
                String syFolderName = oldName.substring(name.length());
                if(syFolderName.length() == 0 || syFolderName.substring(0,1).equals("(")){
                    int beginIndex = syFolderName.indexOf("(") +1 ;
                    int endIndex = syFolderName.lastIndexOf(")") ;
                    if(beginIndex > 0 && endIndex > 0){
                        String version = syFolderName.substring(beginIndex, endIndex);
                        if(StringUtil.isNumber(version)){
                            versions.add(Integer.parseInt(version));
                        } 
                    } else {
                        versions.add(0);
                    }
                }
                
            }
        }
        if(versions.size() > 0){
            //判断是否已经包含保本号
            int beginIndex = name.lastIndexOf("(") +1 ;
            int endIndex = name.lastIndexOf(")") ;
            String version = "";
            if(beginIndex > 0 && endIndex > 0 ){
                version = name.substring(beginIndex, endIndex);
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append(name);
            int lastVersion = 0 ;
            if(StringUtil.isNumber(version)){
                lastVersion = getFbVerson(versions, true);
            } else {
                lastVersion = getFbVerson(versions, false);
            }
            if(lastVersion > 0){
                buffer.append("(");
                buffer.append(lastVersion);
                buffer.append(")");
            }
            //已经包含
            return buffer.toString();
        } else{
            return name;
        }*/
    } 
    
    /***
     * 获取新生成文件的名称
     * @param fileName
     * @param oldFileNames
     * @return  
     * String 
     * @exception
     */
    public static String getFileName(String fileName,List<String> oldFileNames){
        List<String> fileNames = new ArrayList<String>();
        String fileSuffix = StringUtil.getAllLastName(fileName);
        String realFileName = fileName.replace(fileSuffix, "");
        if(oldFileNames != null && oldFileNames.size() > 0) {
            for(String oldFileName : oldFileNames){
                String oldFileSuffix = StringUtil.getAllLastName(oldFileName);
                if(fileSuffix.equals(oldFileSuffix)){
                    fileNames.add(oldFileName.replace(oldFileSuffix,""));
                }
            }
        }
        String name = getName(realFileName, fileNames);
        return name + fileSuffix;
    }
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String a= "新建文件夹(1)(1)";
        
        String a2= "新建文件夹(1)";
        String name = "新建文件夹(1)";
        List<String> oldNames =new ArrayList<String>();
        oldNames.add(a2);
        oldNames.add(a);
        System.out.println(getName(name, oldNames));
        /*String a= "aaaaa(1).jpg";
        String a1= "aaaaa(1)(2).png";
        String a2= "aaaaa(1)(1).jpg";
        List<String> oldNames =new ArrayList<String>();

        String name = "aaaaa(1).jpg";
        name = getFileName(name, oldNames);*/
        //List<String> oldNames =new ArrayList<String>();
        //String a = "Jenkins管理员操作手册(1).docx";
        //oldNames.add(a);
        //oldNames.add(a);
        String b = "Jenkins管理员操作手册.docx";
       // System.out.println(getFileName(b, oldNames));
       // System.out.println(StringUtil.getFirstName("aaaaa(1)"));
        /*  System.out.println(StringUtil.getAllLastName(a));*/
   /*     System.out.println(a1.substring(b.length()));
        String c = a1.substring(b.length() +1,a1.length());
        System.out.println(c.substring(c.indexOf("(") +1,c.lastIndexOf(")")));
        System.out.println(a.substring(a.indexOf("(") +1,a.lastIndexOf(")")));
        //System.out.println(a1.substring(b.length() +1,a1.length()));
         */
    }
}
