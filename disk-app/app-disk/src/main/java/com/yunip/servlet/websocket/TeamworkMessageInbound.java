/*package com.yunip.servlet.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONArray;

import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.teamwork.ITeamworkMessageDao;
import com.yunip.model.company.Employee;
import com.yunip.model.teamwork.TeamworkMessage;
import com.yunip.util.SpringContextUtil;
import com.yunip.utils.json.JsonUtils;

@ServerEndpoint(value="/myecho/{teamworkId}/{employeeId}")
public class TeamworkMessageInbound{
    
    private IEmployeeDao employeeDao = SpringContextUtil.getBean("iEmployeeDao");
    
    private ITeamworkMessageDao teamworkMessageDao = SpringContextUtil.getBean("iTeamworkMessageDao");
    
    private String employeeId = null ; 
    private String teamworkId = null ; 
    
    private Session session;
    
    private static Map<String , Map<String, TeamworkMessageInbound>> mmiList =new ConcurrentHashMap<String , Map<String, TeamworkMessageInbound>>();
    
    @OnMessage
    public void wsTextMessage(String message, Session session) throws IOException {
        Integer teamworkMessageId = Integer.parseInt(message);
        List<TeamworkMessage> teamworkMessages = teamworkMessageDao.selectObjById(teamworkMessageId);
        //转换
        Map<Integer, List<TeamworkMessage>> map = new HashMap<Integer, List<TeamworkMessage>>();
        List<Integer> mapKey = new ArrayList<Integer>();
        if(teamworkMessages != null && teamworkMessages.size() > 0){
            for(TeamworkMessage teamworkMessage : teamworkMessages){
                if(!mapKey.contains(teamworkMessage.getZid())){
                    mapKey.add(teamworkMessage.getZid());
                }
                if(map.get(teamworkMessage.getZid()) == null){
                    map.put(teamworkMessage.getZid(), new ArrayList<TeamworkMessage>());
                }
                map.get(teamworkMessage.getZid()).add(teamworkMessage);
            }
        }
        String portraitPath = SysContant.ROOTPATH + SystemContant.HEADERPATH;
        //拼接
        JSONArray jsonArray = new JSONArray();
        String mapJson = JsonUtils.map2json(map);
        String mapKeyJson = JsonUtils.list2json(mapKey);
        String portraitPathJson = JsonUtils.string2json(portraitPath);
        jsonArray.add(mapJson);
        jsonArray.add(mapKeyJson);
        jsonArray.add(portraitPathJson);
        
        broadcast(teamworkId, jsonArray.toString());
    }
    
    @OnOpen
    public void wsOpen(@PathParam("teamworkId") String teamworkId, @PathParam("employeeId") String employeeId, Session session) {
        this.teamworkId = teamworkId;
        this.employeeId = employeeId;
        this.session = session;
        Employee employee = employeeDao.selectById(Integer.parseInt(employeeId));
        System.out.println("新玩家进入:"+employee.getEmployeeName());
        
        Map<String, TeamworkMessageInbound> teamworkEmpoyeeMap = mmiList.get(teamworkId);
        if(teamworkEmpoyeeMap != null){
            //将员工与该webSocket对象放入map中
            teamworkEmpoyeeMap.put(employeeId, this);
        }else{
            teamworkEmpoyeeMap = new ConcurrentHashMap<String, TeamworkMessageInbound>();
            teamworkEmpoyeeMap.put(employeeId, this);
            mmiList.put(teamworkId, teamworkEmpoyeeMap);
        }
        System.out.println("mmiList:"+mmiList);
    }
    
    @OnClose
    public void wsClose() {
        Map<String, TeamworkMessageInbound> map =  mmiList.get(this.teamworkId);
        if(map != null){
            map.remove(this.employeeId);
        }
        System.out.println("mmiList:"+mmiList);
    }
    
    //websocket错误的时候丢出一个异常
    @OnError
    public void wsError(Session session, Throwable throwable) {
        throw new IllegalArgumentException(throwable);
    }

    //将消息发送给所有指定协作组用户
    private void broadcast(String teamworkId, String message) {
        Map<String, TeamworkMessageInbound> teamworkEmpoyeeMap = mmiList.get(teamworkId);
        for ( String key : teamworkEmpoyeeMap.keySet() ) {
            try {
                ((TeamworkMessageInbound)teamworkEmpoyeeMap.get(key)).session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
*/