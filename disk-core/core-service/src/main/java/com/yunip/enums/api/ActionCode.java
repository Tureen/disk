/**
 * 
 */
package com.yunip.enums.api;

/**
 * @author can.du
 * 数据方法请求
 */
public enum ActionCode {
    
    /**用户登陆*/
    LOGIN(1000,"用户登陆"),
    
    /**修改密码*/
    UPDATEPWD(1001,"修改密码"),
    
    /**获取基本资料*/
    BASICDATA(1002,"获取基本资料"),
    
    /**修改个人基本资料*/
    UPDATEBASIC(1003,"修改个人基本资料"),
    
    /**部门数据列表*/
    DEPTDATA(1004,"部门数据列表"),
    
    /**员工数据列表*/
    EMPLOYEEDATA(1005,"员工数据列表"),
    
    /**个人空间列表*/
    PERSONALSPACES(1006,"个人空间列表"),
    
    /**文件夹树*/
    FOLDERTREE(1007,"文件夹树"),
    
    /**创建文件夹*/
    CREATEFOLDER(1008,"创建文件夹"),
    
    /**重命名*/
    RENAME(1009,"重命名"),
    
    /**同名文件夹或者文件*/
    SAMENAME(1010,"同名文件夹或者文件"),
    
    /**复制或者移动*/
    COPYORMOVE(1011,"复制或者移动"),
    
    /**删除文件夹或者文件*/
    DELETE(1012,"删除文件夹或者文件"),
    
    /**共享空间列表*/
    SHARESPACES(1013,"公共空间or共享空间"),
    
    /**上传头像*/
    UPLPAD(1016,"上传头像"),
    
    /**获取系统信息*/
    SYSINFO(1017,"获取系统信息"),
    
    /**个人云盘剩余大小**/
    DISKREMAINSIZE(1018,"个人云盘剩余大小"),
    
    /**复制与移动限制**/
    CHECKCOPYORMOVE(1019,"复制与移动限制"),
    
    /**分享权限查看**/
    AUTHORITYSHOW(1020,"分享权限查看"),
    
    /**权限分享添加**/
    SHAREAUTHORITY(1021,"权限分享添加"),
    
    /**消息列表**/
    MESSAGEINDEX(1022,"消息列表"),
    
    /**用户未读消息**/
    UNREADMESSAGENUM(1023,"用户未读消息"),
    
    /**消息删除**/
    DELMESSAGE(1024,"消息删除"),
    
    /**标记消息已读**/
    MARKMESSAGE(1025,"标记消息已读"),
    
    /**拉取历史版本**/
    SHOWFILEVERSION(1026,"获得历史版本"),
    
    /**替换旧的历史版本**/
    REPLACEFILEVERSION(1027,"替换历史版本"),
    
    /**扫码登录**/
    LOGINQRCODE(1028,"扫码登录"),
    
    /**设置提取码**/
    CREATETAKECODE(1029,"设置提取码"),
    
    /**工作组数据列表**/
    WORKGROUPDATA(1030,"工作组数据列表"),
    
    /**工作组对应员工列表**/
    WORKGROUPUSER(1031,"工作组对应员工列表"),
    
    /**获取基本信息*/
    BASICINFO(1032,"获取系统信息"),
    
    /**协作组数据列表**/
    TEAMWORKDATA(1033,"协作组数据列表"),
    
    /**协作查看用户**/
    TEAMWORKUSER(1034,"查看用户"),
    
    /**创建协作**/
    TEAMWORKCREATE(1035,"创建协作"),
    
    /**解散协作**/
    TEAMWORKDELETE(1036,"解散协作"),
    
    /**退出协作**/
    TEAMWORKQUIT(1037,"退出协作"),
    
    /**协作空间**/
    TEAMWORKSPACES(1038,"协作空间"),
    
    /**创建协作文件夹*/
    CREATETEAMWORKFOLDER(1039,"创建协作文件夹"),
    
    /**协作文件重命名**/
    TEAMWORKRENAME(1040,"协作文件重命名"),
    
    /**协作文件删除**/
    TEAMWORKFILEDELETE(1041,"协作文件删除"),
    
    /**协作复制与移动限制**/
    TEAMWORKCHECKCOPYORMOVE(1042,"协作复制与移动限制"),
    
    /**协作移动同名文件夹或者文件*/
    TEAMWORKSAMENAME(1043,"协作移动同名文件夹或者文件"),
    
    /**移动文件夹或者文件*/
    TEAMWORKMOVE(1044,"移动文件夹或者文件"),
    
    /**协作导出同名文件夹或者文件*/
    TEAMWORKEXPORTSAMENAME(1045,"协作导出同名文件夹或者文件"),
    
    /**导出文件夹或者文件**/
    TEAMWORKEXPORT(1046,"导出文件夹或者文件"),
    
    /**获得协作历史版本**/
    TEAMWORKSHOWFILEVERSION(1047,"获得协作历史版本"),
    
    /**替换旧的协作历史版本**/
    REPLACETEAMWORKFILEVERSION(1048,"替换协作历史版本"),
    
    /**个人协作云盘剩余大小**/
    TEAMWORKDISKREMAINSIZE(1049,"个人协作云盘剩余大小"),
    
    /**协作信息**/
    TEAMWORKMESSAGE(1050,"协作信息"),
    
    /**发送协作信息**/
    SENDTEAMWORKMESSAGE(1051,"发送协作信息");

    private int code;
    
    private String desc;

    private ActionCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
