package cn.butel.MeetingSuperMonitor.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_EMPTY)
public class ServiceServerInfo extends Object implements Cloneable{
	
	//被监控服务器的ip地址 格式ip:port
	private String ip;
	
	//被监控服务器类型 1.stp;2.stprc;3.relay;4.relayrc;5.nps;6.ec7.bs;8update
	private int type;
	
	//服务器被监控的标识 全局唯一
	private int sid; 
	
	private long lastHandlePacketId;
	
	public Object clone(){
		ServiceServerInfo info = null;
		try {
			info = (ServiceServerInfo)super.clone();
		} catch (Exception e) {
			 e.printStackTrace();  
		}
		return info;
	}
	public ServiceServerInfo(String ip,int type){
		this.ip     = ip;
		this.type = type;
		this.sid   = -1;
		this.lastHandlePacketId = 0;
	}
	public ServiceServerInfo(String ip, int type , int sid) {
		this.ip     = ip;
		this.type = type;
		this.sid    = sid;
		this.lastHandlePacketId = 0;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public long getLastHandlePacketId() {
		return lastHandlePacketId;
	}
	public void setLastHandlePacketId(long lastHandlePacketId) {
		this.lastHandlePacketId = lastHandlePacketId;
	}
}
