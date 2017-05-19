package cn.butel.MeetingSuperMonitor.service.impl;


import java.io.InputStream;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


import com.google.gson.JsonObject;




import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorHttpServerDAOImpl;
import cn.butel.MeetingSuperMonitor.common.CommStats;
import cn.butel.MeetingSuperMonitor.common.HttpPostMethod;
import cn.butel.MeetingSuperMonitor.entity.httpServerResult;
import cn.butel.MeetingSuperMonitor.service.BaseServerMonitor;
import cn.butel.MeetingSuperMonitor.service.IServerMonitor;
import cn.butel.MeetingSuperMonitor.uitl.SendMsgInfo;

public class NpsServerMonitor extends BaseServerMonitor implements IServerMonitor{

	protected Logger logger = Logger.getLogger(NpsServerMonitor.class);
	
	private SendMsgInfo info;
	
	private static  int gPakcetId = 0;
	
	private MonitorBaseDAO dao; 
	
	private String url;
	
	private boolean isComposCmd;
	
	private JsonObject cmd;
	
	public NpsServerMonitor(){
//		resultMap = new HashMap<SendMsgInfo, Object>();
		dao           = new MonitorHttpServerDAOImpl();
	}
	
	public NpsServerMonitor(String url){
		this.url            = url;
		dao                = new MonitorHttpServerDAOImpl();
		isComposCmd = false;
	}
	@Override
	public JsonObject composCmdParam(int type) {
//		JsonObject json  = new JsonObject();
//		switch (type) {
//		case QUERY_NPS:
//			json.addProperty("devModel", "PCHVS");
//			json.addProperty("serialNumber", CommStats.imei);
//			json.addProperty("requestParam", "X1MeetingCtrlAgent");
//			break;
//
//		default:
//			json = null;
//			break;
//		}
		if (isComposCmd) {
			return cmd;
		}else {
			switch (type) {
			case QUERY_NPS:
				cmd = new JsonObject();
				cmd.addProperty("devModel", "PCHVS");
				cmd.addProperty("serialNumber", CommStats.imei);
				cmd.addProperty("requestParam", "X1MeetingCtrlAgent");
				isComposCmd = true;
				break;
	
			default:
				cmd = null;
				break;
			}
			return cmd;
		}
	}

	@Override
	public SendMsgInfo sendMonitorCmd(int packetId) {
		if (info == null) {
			info = new SendMsgInfo();
		}
		info.setPacketId(packetId);
		JsonObject params = composCmdParam(QUERY_NPS);
		if (params == null) {
			logger.info("组装命令失败，无法投递");
			return null;
		}
	    logger.info("投递请求 packetId:"+packetId);
	   
	    try {
//			HttpClient http = new HttpClient();
//			HttpPostMethod get = new HttpPostMethod(CommStats.npsUrl);
			HttpPostMethod get = new HttpPostMethod(url);
			get.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
			get.setParameter("params",params.toString());

			 try {
//				    JsonObject json = new JsonObject();
//				    json.addProperty("status", "-10");
				    int statusCode = 0;
				    try {
				    	//超时不崩，但是对方关闭连接，竟然崩
						statusCode = http.executeMethod(get);
					} catch (Exception e) {
						statusCode = 0;
					}
					if (statusCode != HttpStatus.SC_OK) {
						logger.error("Method failed: " + get.getStatusLine());
						//这个map会被多线程访问 需要加锁
						//可以把这个map里的内容记到别的地方
						//一个单件类，线程完全
						//resultMap.put(info, status);
//						dao.add(json.toString(), httpServerResult.NPS_RESULT);
						dao.add("",httpServerResult.NPS_RESULT,url);
					}else {
						//获得response的输入流					
						InputStream data = get.getResponseBodyAsStream();
						String str              = IOUtils.toString(data,"UTF-8");
						data.close();
						logger.info("获得数据，packetId:"+packetId);
						logger.info("数据:"+str);
//						dao.add(str, httpServerResult.NPS_RESULT);
						dao.add(str, httpServerResult.NPS_RESULT,url);
					}				
					
				} catch (Exception e) {
					logger.error("投递数据失败:"+e.getMessage());
					e.printStackTrace();
				} finally {
					get.releaseConnection();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	public void run(){
		while(true){
			sendMonitorCmd(gPakcetId);
			gPakcetId++;
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isComposCmd() {
		return isComposCmd;
	}

	public void setComposCmd(boolean isComposCmd) {
		this.isComposCmd = isComposCmd;
	}
	
}

