/*
 * 描述：文件服务器异常编码类
 * 创建人：jian.xiong
 * 创建时间：2016-5-11
 */
package com.yunip.enums.fileservers;


/**
 * 文件服务器异常编码类
 */
public enum FileServersException{
    
    /** 服务器数据正常 **/
    SUCCESS(1000, "服务器数据正常"),
    
    /** 服务器数据异常 **/
    ERROR(2000, "服务器数据异常"),
    
    /** 身份验证失效 **/
    SFYZSX(2001,"身份验证失效"),
    
    /** 参数错误 **/
    PARAMERROR(2002,"非法请求"),
    
    /** 文件不存在 **/
    FILENOEXISTS(2003,"文件不存在"),
    
    /** 该文件类型不允许上传 **/
    FILETYPENOTUPLOAD(2004,"禁止上传"),
    
    /** 提取码无效 **/
    TAKECODEINVALID(2005,"提取码无效"),
    
    /** 暂不支持该文件类型的预览 **/
    NOTSUPPORTFILEPRIVIEW(2006,"暂不支持该文件类型的预览"),
    
    /** 预览错误 **/
    PRIVIEWERROR(2007,"预览错误"),
    
    /** 权限不足 **/
    NOTAUTHORITY(2008,"您的权限不足,无法操作！"),
    
    /** 该格式文件无法在线编辑 **/
    NOTSUPPORTONLINEEDIT(2009,"该格式文件无法在线编辑"),
    
    /** 该格式文件暂无法在线解压 **/
    NOTSUPPORTONLINEDEP(2010,"该格式文件暂无法在线解压"),
    
    /** 该格式文件暂无法在线解压 **/
    DECOMPRESSFAIL(2011,"解压失败！"),
    
    /** 解压文件超出1G,暂时无法解压 **/
    ZYWJCCDX(2012,"解压文件超出1G,暂时无法解压"),
    
    /**用户取消文件解压**/
    CANCELFILEDEC(2013,"用户取消文件解压"),
    
    /**RAR文件内部文件不能超过500个**/
    RARNUMBERERROE(2014,"RAR文件内部文件不能超过500个"),
    
    /** 权限不足或文件已删除 **/
    NOTAUTHORITYORFILENOTEXISTS(2015,"您的权限不足或文件已删除！"),
    
    /** 分布式部署配置错误 **/
    CLUSTERCONFIGERROR(2016,"分布式部署配置错误！"),
    
    /** 分布式部署暂不支持在线解压 **/
    CLUSTER_NOT_DECOMPRESS(2017,"暂不支持在线解压！"),
    
    /** 所有服务器均不满足上传最低要求 **/
    NO_ALLOW_FILE_SPACE(2018,"没有可使用的上传服务器！");
    
    private Integer code;
     
    private String msg;
     
    private FileServersException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
     
     
}
