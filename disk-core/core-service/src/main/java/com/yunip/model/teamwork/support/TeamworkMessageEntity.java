package com.yunip.model.teamwork.support;

import java.util.List;
import java.util.Map;

import com.yunip.model.teamwork.TeamworkEmployee;
import com.yunip.model.teamwork.TeamworkMessage;
import com.yunip.model.teamwork.query.TeamworkMessageQuery;

public class TeamworkMessageEntity {

    private List<TeamworkEmployee>              employeeAddObjs;

    private TeamworkMessageQuery                teamworkMessageQuery;

    private Map<Integer, List<TeamworkMessage>> map;

    private List<Integer>                       mapKey;

    /**头像路径**/
    private String                              portraitPath;

    public Map<Integer, List<TeamworkMessage>> getMap() {
        return map;
    }

    public void setMap(Map<Integer, List<TeamworkMessage>> map) {
        this.map = map;
    }

    public List<TeamworkEmployee> getEmployeeAddObjs() {
        return employeeAddObjs;
    }

    public void setEmployeeAddObjs(List<TeamworkEmployee> employeeAddObjs) {
        this.employeeAddObjs = employeeAddObjs;
    }

    public TeamworkMessageQuery getTeamworkMessageQuery() {
        return teamworkMessageQuery;
    }

    public void setTeamworkMessageQuery(
            TeamworkMessageQuery teamworkMessageQuery) {
        this.teamworkMessageQuery = teamworkMessageQuery;
    }

    public List<Integer> getMapKey() {
        return mapKey;
    }

    public void setMapKey(List<Integer> mapKey) {
        this.mapKey = mapKey;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

}
