package cn.butel.MeetingSuperMonitor.service.impl;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorHttpServerDAOImpl;
import cn.butel.MeetingSuperMonitor.common.HttpPostMethod;
import cn.butel.MeetingSuperMonitor.entity.httpServerResult;
import cn.butel.MeetingSuperMonitor.service.BaseServerMonitor;
import cn.butel.MeetingSuperMonitor.service.IServerMonitor;
import cn.butel.MeetingSuperMonitor.uitl.SendMsgInfo;

public class UpdateServerMonitor extends BaseServerMonitor implements IServerMonitor{
	
	protected Logger logger = Logger.getLogger(UpdateServerMonitor.class);
	
	private SendMsgInfo info;
	
	private static  int gPakcetId = 0;
	
	private MonitorBaseDAO dao; 
	
	private String url;
	
	private boolean isComposCmd;
	
	private JsonObject cmd;
	
	public UpdateServerMonitor(String url){
		this.url      = url;
		dao           = new MonitorHttpServerDAOImpl();
		isComposCmd = false;
	}
	
	public UpdateServerMonitor(){
		dao           = new MonitorHttpServerDAOImpl();
	}

	@Override
	public JsonObject composCmdParam(int type) {
//		JsonObject json  = new JsonObject();
//		switch (type) {
//		case CHECK_VERSION:
//			long currentTime = System.currentTimeMillis()/1000 ;
//			json.addProperty("name", "meetingpchvs");
//			json.addProperty("project", "justmeetinghvs");
//			json.addProperty("sn", "GCGD99999999999");
//			json.addProperty("terminal", "PCHVS");
//			json.addProperty("version", "v0.0.0.1");
//			json.addProperty("utctime", currentTime+"");
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
			case CHECK_VERSION:
				long currentTime = System.currentTimeMillis()/1000 ;
				cmd = new JsonObject();
				cmd.addProperty("name", "meetingpchvs");
				cmd.addProperty("project", "justmeetinghvs");
				cmd.addProperty("sn", "GCGD99999999999");
				cmd.addProperty("terminal", "PCHVS");
				cmd.addProperty("version", "v0.0.0.1");
				cmd.addProperty("utctime", currentTime+"");
				isComposCmd = true;
				break;
	
			default:
				cmd = null;
				break;
			}
			return cmd;
		}
//		return json
	}

	@Override
	public SendMsgInfo sendMonitorCmd(int packetId) {
		if (info == null) {
			info = new SendMsgInfo();
		}
		info.setPacketId(packetId);
		JsonObject params = composCmdParam(CHECK_VERSION);
		if (params == null) {
			logger.info("组装获取版本命令失败，无法投递");
			return null;
		}
	    logger.info("投递获取版本请求 packetId:"+packetId);
	   
	    try {
//			HttpClient http = new HttpClient();
//			HttpPostMethod get = new HttpPostMethod(CommStats.UpdateUrl);
			HttpPostMethod get = new HttpPostMethod(url);
			get.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		    get.setParameter("params",params.toString());
		    get.setParameter("service", "CheckVersion");

			 try {
//				    JsonObject json = new JsonObject();
//				    json.addProperty("status", "-10");
				    int statusCode = 0;
				    try {
						statusCode = http.executeMethod(get);
					} catch (Exception e) {
						statusCode = 0;
					}
					if (statusCode != HttpStatus.SC_OK) {
						logger.error("Method failed: " + get.getStatusLine());
//						dao.add(json.toString(), httpServerResult.UPDATE_RESULT);
						dao.add("", httpServerResult.UPDATE_RESULT,url);
					}else {
						//获得response的输入流					
						InputStream data = get.getResponseBodyAsStream();
						String str              = IOUtils.toString(data,"UTF-8");
						data.close();
						logger.info("获得数据，packetId:"+packetId);
						logger.info("数据:"+str);
//						dao.add(str, httpServerResult.UPDATE_RESULT);
						dao.add(str, httpServerResult.UPDATE_RESULT,url);
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
