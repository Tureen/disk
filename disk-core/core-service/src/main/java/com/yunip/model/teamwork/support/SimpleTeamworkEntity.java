package com.yunip.model.teamwork.support;

import java.util.List;

import com.yunip.model.teamwork.SimpleTeamworkData;

public class SimpleTeamworkEntity {

    /**简易列表数据**/
    private List<SimpleTeamworkData> simpleTeamworkDatas;

    /**员工加入协作**/
    private List<Integer>            joinIds;

    /**员工拥有协作**/
    private List<Integer>            hasIds;

    public List<SimpleTeamworkData> getSimpleTeamworkDatas() {
        return simpleTeamworkDatas;
    }

    public void setSimpleTeamworkDatas(
            List<SimpleTeamworkData> simpleTeamworkDatas) {
        this.simpleTeamworkDatas = simpleTeamworkDatas;
    }

    public List<Integer> getJoinIds() {
        return joinIds;
    }

    public void setJoinIds(List<Integer> joinIds) {
        this.joinIds = joinIds;
    }

    public List<Integer> getHasIds() {
        return hasIds;
    }

    public void setHasIds(List<Integer> hasIds) {
        this.hasIds = hasIds;
    }

}
