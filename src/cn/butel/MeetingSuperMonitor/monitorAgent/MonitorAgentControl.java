package cn.butel.MeetingSuperMonitor.monitorAgent;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;



import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorServiceServerDAOImpl;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorServiceServerStatusDAOImpl;
import cn.butel.MeetingSuperMonitor.common.CommStats;
import cn.butel.MeetingSuperMonitor.entity.ServiceServerInfo;

import meetingMonitorAgent.monitorAgentCBInterface;
import meetingMonitorAgent.monitorAgent_jni;

public class MonitorAgentControl {
	
	public Logger logger = Logger.getLogger(MonitorAgentControl.class);
	
	//成功被监控的服务器信息Map key:被监控服务器Ip value:被监控服务器信息
	private Map<String, ServiceServerInfo> serverInfoMap;
	
	//通过sid，查询服务器信息 key:sid,value:服务器信息
	private Map<Integer,ServiceServerInfo> sidServerInfoMap;
	
	//被监控的服务器信息拷贝map
	private Map<String, ServiceServerInfo> serverInfoCloneMap;
	
	//最外层map key 句柄 value命令队列
	private Map<Integer, Map<Long, Object>> cmdResultList; 

	//初始化状态码
	 private class StatusCode {
		private static final int NOT_INIT = 0;

		private static final int INIT = 1;

	 }
	 
	 private int mInitStatus = StatusCode.NOT_INIT;
	 
	 private static MonitorAgentControl control;
	 
	 private MonitorBaseDAO dataDao;
	 private MonitorBaseDAO statusDao;
	 private Gson                    gson;
	 
	 private MonitorAgentControl(){
		 serverInfoCloneMap = new HashMap<String, ServiceServerInfo>();
		 serverInfoMap          = new HashMap<String, ServiceServerInfo>();
		 sidServerInfoMap     = new HashMap<Integer, ServiceServerInfo>();
		 dataDao                   = new MonitorServiceServerDAOImpl();
		 statusDao                 = new MonitorServiceServerStatusDAOImpl();
	 }
	 
	 public static MonitorAgentControl getInstance(){
		if (control == null) {
			control = new MonitorAgentControl();
		}
		return control;
	}
	 
	 private monitorAgentCBInterface monitorAgentCBInterface = new monitorAgentCBInterface() {
		 
		 @Override
		 public void monitorAgent_CallBack(int handle, int notifyType,
				String cmdReq, String cmdResp) {
			 logger.info("接收到底层回调 handle:"+handle + " notifyType:"+notifyType + " cmdReq:"+cmdReq +"cmdResp:" +cmdResp);
			insertResutTocmdList(handle,notifyType,cmdReq,cmdResp);
			 
		}
	};
	
	/**
	 * 初始化MonitorAgent
	 * @return 0 成功 小于0失败
	 */
	public int initMonitorAgent(){
		int ret = monitorAgent_jni.Init(monitorAgentCBInterface);
		logger.info("初始化MonitorAgent状态:"+ret);
		if (ret == 0) {
			mInitStatus = StatusCode.INIT;
			return 0;
		}else {
			mInitStatus = StatusCode.NOT_INIT;
			return -1;
		}
		
	}
	
	/**
	 * 功能 添加监控服务器
	 * @param serverIp 被监控服务器的ip
	 * @param type       被监控服务器的类型
	 * @return 0成功 -1失败
	 */
	public int addMonitor(String serverIp,int type){
		if (!ipisValid(serverIp)) {
			logger.info("addMonitor添加监控服务器失败，服务器地址:"+serverIp+"不合法，服务器类型:"+type);
			return -1;
		}
		synchronized (control) {
			//正在监控 就不在监控
			if (serverInfoMap.get(serverIp) != null) {
				return serverInfoMap.get(serverIp).getSid();
			}else {
				//未被监控 调用Agent的addMonitor接口
				//ret大于0表示监控成功
				String server[] = serverIp.split(":");
				int ret = monitorAgent_jni.addMonitor(CommStats.monitorAgentIp,CommStats.monitorAgentPort,server[0],Integer.parseInt(server[1]));
				logger.info("添加监控服务器返回状态:"+ret+" serverIp:"+server[0]+" serverPort:"+server[1]+" localIp:"+CommStats.monitorAgentIp+" localPort:"+CommStats.monitorAgentPort);
				if (ret > 0) {
					ServiceServerInfo info = new ServiceServerInfo(serverIp,type,ret);
					serverInfoMap.put(serverIp, info);
					sidServerInfoMap.put(ret, info);
					//启动监控服务器
					ret = monitorAgent_jni.Start(ret);
					logger.info("start监控服务器ip:"+info.getIp()+" 类型:"+info.getType()+" sessionId:"+info.getSid());
					return 0;
				}
				return -1;
			}
		}
		
	}
	/**
	 * 删除被监控服务器
	 * @param serverIp 服务器ip
	 * @return
	 */
	public  int deleteMonitor(String serverIp){
		if (!ipisValid(serverIp)) {
			logger.info("deleteMonitor删除监控服务器失败，服务器地址:"+serverIp+"不合法");
			return -1;
		}else {
			synchronized (control) {
				if (!serverInfoMap.containsKey(serverIp)) {
					logger.info("deleteMonitor删除监控服务器 server:"+serverIp+"不在监控列表中，返回删除成功");
					return 0;
				}else {
					ServiceServerInfo info = serverInfoMap.get(serverIp);
					monitorAgent_jni.delMonitor(info.getSid());
					serverInfoMap.remove(info.getSid());
					serverInfoMap.remove(serverIp);
					logger.info("deleteMonitor删除监控服务器 server:"+serverIp+"在监控列表中，删除成功");
					return 0;
				}
			}	
		}
	}
	
	private Boolean ipisValid(String ip){
		if (ip == "" || ip.lastIndexOf(":") == -1 || ip.lastIndexOf(".") == -1) {
			return false;
		}else {
			return true;
		}
	}
	
	public void getMonitorServerStatusAndData(){
		if(cmdResultList == null)
			cmdResultList = new HashMap<Integer, Map<Long,Object>>();
		long packetId = System.currentTimeMillis();
		JsonObject cmd = null;
		cloneServerInfoMap();
		for (String key : serverInfoCloneMap.keySet()) {  
		    ServiceServerInfo info = serverInfoCloneMap.get(key); 
		    statusDao.add((Object)monitorAgent_jni.getWorkState(info.getSid()), (Object)key);
		    cmd = preCmd(info,packetId);
		    //cmd组装成功且数据包包号大于上次处理包号才发送
		    if (cmd != null && packetId > info.getLastHandlePacketId()) {
				int ret = monitorAgent_jni.sendCmd(info.getSid(), cmd.toString(), cmd.toString().length());
				logger.info("定时获取业务数据 返回:" + ret + " 服务器:" +info.getIp()+"  句柄："+ info.getSid() + " 服务器类型："+info.getType() +" packetId:"+packetId);
				//与底层约定，同步返回成功才会有回调
				if (ret == 0) {
					info.setLastHandlePacketId(packetId);
					insertCmdList(info,packetId);
				}
			}
		}
	}
	
	/**
	 * 将serverInfoMap
	 */
	private void cloneServerInfoMap(){
		synchronized (serverInfoMap) {
			serverInfoCloneMap.clear();
			for (String key : serverInfoMap.keySet())
				serverInfoCloneMap.put(key, (ServiceServerInfo)serverInfoMap.get(key).clone());
		}
	}
	
	/**
	 * 组装获取业务数据命令
	 * @param info 服务器信息
	 * @param packetId 数据包id
	 * @return
	 */
	private JsonObject preCmd(ServiceServerInfo info, long packetId){
		JsonObject json = new JsonObject();
		json.addProperty("packid", packetId+"");
		switch (info.getType()) {
		case 1:
			json.addProperty("cmd", "1");
			break;
		case 2:
			json.addProperty("cmd", "100");
			break;
		case 3:
			json.addProperty("cmd", "300");
			json.addProperty("relayid", CommStats.relayIdMap.get(info.getIp()));
			break;
		case 4:
			json.addProperty("cmd", "200");
			break;
		default:
			json = null;
			break;
		}
		return json;
	}
	
	/**
	 * 将命令加入命令缓存队列
	 * @param info 服务器信息
	 * @param packetId 数据包包号
	 */
	private void insertCmdList(ServiceServerInfo info,long packetId){
		Map<Long, Object> cmdList = null;
		synchronized (cmdResultList) {
			cmdList = cmdResultList.get(info.getSid());
			if (cmdList == null) {
				cmdList = new HashMap<Long, Object>();
			}
			cmdList.put(packetId, "");
			cmdResultList.put(info.getSid(), cmdList);
			logger.info("句柄:"+info.getSid()+"命令缓存队列长度:"+cmdList.size());
			return;
		}
	}
	
	private void insertResutTocmdList(int handle, int notifyType, String cmdRep, String cmdResp){
		JsonObject cmd = getJson(cmdRep);
		JsonObject resp = getJson(cmdResp);
		Object e = null;
		if (cmd == null/* || resp == null*/) {
			logger.info("句柄:"+handle+" 回调数据异常");
			return;
		}
		long packetId = cmd.get("packid").getAsLong();
		
		synchronized (cmdResultList) {
			Map<Long,Object> cmdList = cmdResultList.get(handle);
			if (cmdList == null) {
				logger.info("命令缓存队列无该句柄:"+ handle +" 数据，数据丢弃");
				return;
			}else {
				e = cmdList.remove(packetId);
				cmdResultList.put(handle, cmdList);
				logger.info("句柄:"+handle+" 缓存队列:"+cmdList.size());
			}
		}
		if (e == null) {
			logger.info("命令缓存队列有该句柄:"+ handle +" 数据，但无该packetId:" + packetId + "缓存的命令 数据丢弃");
			return;
		}else {
			synchronized (sidServerInfoMap) {
				ServiceServerInfo info = sidServerInfoMap.get(handle);
				if (info != null) {
//					info.setLastHandlePacketId(packetId);
//					sidServerInfoMap.put(handle, info);
					dataDao.add(cmdResp, info.getIp());
					logger.info("命令缓存队列有该句柄:"+ handle +" 数据，有该packetId:" + packetId + "缓存的命令 回应结果加入结果缓存队列");
				}
			}
		}
	}
	
	protected JsonObject getJson(String jsonStr){
    	if (gson == null) {
			gson = new Gson();
		}
    	JsonElement element =null;
    	try {
    		element = gson.fromJson(jsonStr, JsonElement.class);
		} catch (Exception e) {
			logger.info("异常："+e.toString());
		}
    	
    	JsonObject json = null;
    	try {
    		json = element.getAsJsonObject();
		} catch (Exception e) {
			json = null;
		}
    	
    	return json;
    }
}
