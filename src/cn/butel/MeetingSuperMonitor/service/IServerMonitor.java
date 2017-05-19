package cn.butel.MeetingSuperMonitor.service;



import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import cn.butel.MeetingSuperMonitor.uitl.SendMsgInfo;

import com.google.gson.JsonObject;

public interface IServerMonitor extends SessionAware,ServletRequestAware,ServletResponseAware{

	public JsonObject composCmdParam(int type);
	
	public SendMsgInfo sendMonitorCmd(int packetId);
}
