/*
 * 描述：协作记录信息类
 * 创建人：ming.zhu
 * 创建时间：2017-03-16
 */
package com.yunip.model.teamwork.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.teamwork.TeamworkMessage;
import com.yunip.utils.page.PageQuery;

@Alias("TTeamworkMessageQuery")
public class TeamworkMessageQuery extends PageQuery<TeamworkMessage> implements
        Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = -6316234837358406954L;

    /**组id**/
    private Integer           zid;

    /**消息类型 1:上传  2:删除  3:下载  4:新建文件夹  5:导出  6:留言**/
    private Integer           msgType;

    /**员工id**/
    private Integer           employeeId;

    /**协作id**/
    private Integer           teamworkId;                               ;

    /******************ADMIN*************************/
    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    /**操作日期**/
    private String            operationDate;

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTeamworkId() {
        return teamworkId;
    }

    public void setTeamworkId(Integer teamworkId) {
        this.teamworkId = teamworkId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

}
