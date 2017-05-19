package cn.butel.MeetingSuperMonitor.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import cn.butel.MeetingSuperMonitor.common.WriteFileThread;
import cn.butel.MeetingSuperMonitor.entity.ServiceServerInfo;
import cn.butel.MeetingSuperMonitor.entity.httpServerResult;
import cn.butel.MeetingSuperMonitor.monitorAgent.MonitorAgentThread;
import cn.butel.MeetingSuperMonitor.service.impl.BackManagerServerMonitor;
import cn.butel.MeetingSuperMonitor.service.impl.ECServerMonitor;
import cn.butel.MeetingSuperMonitor.service.impl.NpsServerMonitor;
import cn.butel.MeetingSuperMonitor.service.impl.UpdateServerMonitor;

public class MoniterNpsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MoniterNpsServlet() {
		super();

	}

	/**
	 * 启动NPS线程、接口服务器线程、企业用户中心地址
	 * 初始化缓存结果队列
	 */
	public void init() throws ServletException {
		httpServerResult     result       =    httpServerResult.getInstance();
		MonitorAgentThread monitorAgentThread = new MonitorAgentThread();
		monitorAgentThread.init();		
		new Thread(monitorAgentThread).start();
		
//		new Thread(new NpsServerMonitor()).start();
//		new Thread(new BackManagerServerMonitor()).start();
//		new Thread(new ECServerMonitor()).start();
//		new Thread(new UpdateServerMonitor()).start();
		for (Object e : result.serverInfoList) {
			ServiceServerInfo info =(ServiceServerInfo)e;
			if (info.getType() == 5) {
				new Thread(new NpsServerMonitor(info.getIp())).start();
			}
			if (info.getType() == 6) {
				new Thread(new ECServerMonitor(info.getIp())).start();
			}
			if (info.getType() == 7) {
				new Thread(new BackManagerServerMonitor(info.getIp())).start();
			}
			if (info.getType() == 8) {
				new Thread(new UpdateServerMonitor(info.getIp())).start();
			}
		}
		
		//test 先注释掉
//		WriteFileThread writeFileThread = new WriteFileThread();
//		new Thread(writeFileThread).start();
		
		
		
		
//		List<Object> list = new ArrayList<Object>();
//		for(int i = 0; i < 10; i++){
//			String a =i+"";
//			list.add(a);
//		}
//		writeFileThread.setBufferWriteList(list);
//		list.clear();
//		System.out.print("aaaaaa:"+list.size());
//		System.out.print("bbbb:"+writeFileThread.getBufferWriteList().size());
	}
	
	
}
