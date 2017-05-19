package cn.butel.MeetingSuperMonitor.monitorAgent;

import java.util.Iterator;

import cn.butel.MeetingSuperMonitor.entity.ServiceServerInfo;
import cn.butel.MeetingSuperMonitor.entity.ServiceServerResult;


public class MonitorAgentThread extends Thread{

	private ServiceServerResult result    =    ServiceServerResult.getInstance();
	private MonitorAgentControl control =    MonitorAgentControl.getInstance();
	private int initStatus;
	public int init(){
		initStatus = control.initMonitorAgent();
		if( initStatus == 0){
			for (Iterator<Object> iterator = result.serverList.iterator(); iterator.hasNext();) {
				ServiceServerInfo info = (ServiceServerInfo)iterator.next();
				control.addMonitor(info.getIp(), info.getType());
				//tests
				//break;
			}
		}
		return initStatus;
	}
	public void run(){
		while(true && initStatus == 0){
			control.getMonitorServerStatusAndData();
			try {
				sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getInitStatus() {
		return initStatus;
	}
	public void setInitStatus(int initStatus) {
		this.initStatus = initStatus;
	}
	
	
}
