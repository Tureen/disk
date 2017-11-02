package com.yunip.model.sys.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.sys.Backup;
import com.yunip.utils.page.PageQuery;

@Alias("TBackupQuery")
public class BackupQuery extends PageQuery<Backup> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 5402015315892911087L;

    /**备份名称**/
    private String            sqlName;

    /**备份路径**/
    private String            sqlPath;

    /**起始日期**/
    private String            startDate;

    /**结束提起**/
    private String            endDate;

    /**创建人**/
    private String            createAdmin;

    public String getSqlPath() {
        return sqlPath;
    }

    public void setSqlPath(String sqlPath) {
        this.sqlPath = sqlPath;
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

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(String createAdmin) {
        this.createAdmin = createAdmin;
    }

}
