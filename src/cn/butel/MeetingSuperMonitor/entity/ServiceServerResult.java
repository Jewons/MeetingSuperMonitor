package cn.butel.MeetingSuperMonitor.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.butel.MeetingSuperMonitor.common.CommStats;

public class ServiceServerResult {

	private static ServiceServerResult result;
	
	//被监控的服务器的业务数据缓存map
	public Map<String, List<Object>> serviceMap;
	
	/**
	 * string 被监控服务器Ip地址
	 * Interger 被监控服务器的存活状态 0.存活；-1心跳中断
	 */
	public Map<String,Integer> statusMap;
	//被监控的服务器列表 Object为
	public List<Object> serverList;
	/**
	 * 构造函数 初始化所有服务器缓存队列
	 */
	private ServiceServerResult(){
		serviceMap = new HashMap<String, List<Object>>();
		serverList    = new ArrayList<Object>();
		statusMap   = new HashMap<String, Integer>();
		ServiceServerInfo info;
		for (int i = 0; i < CommStats.stprcList.length; i++) {
			List<Object> resultList = new ArrayList<Object>();
			info = new ServiceServerInfo(CommStats.stprcList[i],2);
			serviceMap.put(info.getIp(), resultList);
			statusMap.put(info.getIp(), 0);
			serverList.add(info);
		}
		for (int i = 0; i < CommStats.stpList.length; i++) {
			List<Object> resultList = new ArrayList<Object>();
			info = new ServiceServerInfo(CommStats.stpList[i],1);
			serviceMap.put(info.getIp(), resultList);
			statusMap.put(info.getIp(), 0);
			serverList.add(info);
		}
		for (int i = 0; i < CommStats.relayrcList.length; i++) {
			List<Object> resultList = new ArrayList<Object>();
			info = new ServiceServerInfo(CommStats.relayrcList[i],4);
			serviceMap.put(info.getIp(), resultList);
			statusMap.put(info.getIp(), 0);
			serverList.add(info);
		}
		for (int i = 0; i < CommStats.relayList.length; i++) {
			List<Object> resultList = new ArrayList<Object>();
			info = new ServiceServerInfo(CommStats.relayList[i],3);
			serviceMap.put(info.getIp(), resultList);
			statusMap.put(info.getIp(), 0);
			serverList.add(info);
		}
	}
	
	public static synchronized ServiceServerResult getInstance(){
		if (result == null) {
			result = new ServiceServerResult();
		}
		return result;
	}
}
