package cn.butel.MeetingSuperMonitor.service.impl;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorHttpServerDAOImpl;
import cn.butel.MeetingSuperMonitor.common.CommStats;
import cn.butel.MeetingSuperMonitor.common.HttpPostMethod;
import cn.butel.MeetingSuperMonitor.entity.httpServerResult;
import cn.butel.MeetingSuperMonitor.service.BaseServerMonitor;
import cn.butel.MeetingSuperMonitor.service.IServerMonitor;
import cn.butel.MeetingSuperMonitor.uitl.SendMsgInfo;

public class BackManagerServerMonitor extends BaseServerMonitor implements IServerMonitor{

	protected Logger logger = Logger.getLogger(BackManagerServerMonitor.class);
	
	private SendMsgInfo info;
	
	private static  int gPakcetId = 0;
	
	private MonitorBaseDAO dao; 
	
	private String url;
	
	public BackManagerServerMonitor(){
		dao           = new MonitorHttpServerDAOImpl();
	}
	
	public BackManagerServerMonitor(String url){
		this.setUrl(url);
		dao           = new MonitorHttpServerDAOImpl();
	}
	
	@Override
	public JsonObject composCmdParam(int type) {
		JsonObject json  = new JsonObject();
		switch (type) {
		//组装绑定token命令
		case BIND_TOKEN:
			json.addProperty("name", CommStats.imei);
			json.addProperty("phoneId", CommStats.accountId);
			json.addProperty("token", CommStats.token);
			break;
		//组装创建会议命令
		case CREATE_MEETING:
			long currentTime = System.currentTimeMillis()/1000 ;
			logger.info("时间:"+currentTime);
			JsonArray array = new JsonArray();
			JsonObject invitor = new JsonObject();
			invitor.addProperty("phoneId", CommStats.accountId);
			array.add(invitor);
			json.addProperty("beginDateTime", currentTime);
			json.addProperty("meetingType", 1);
			json.addProperty("token", CommStats.token);
			json.add("invotedUsers", array);
			break;
		default:
			json = null;
			break;
		}
		
		return json;
	}

	@Override
	public SendMsgInfo sendMonitorCmd(int packetId) {
		if (info == null) {
			info = new SendMsgInfo();
		}
		info.setPacketId(packetId);
//		JsonObject params;
//		if (packetId %2 == 0) {
//			params = composCmdParam(CREATE_MEETING);
//		}
//		else {
//			params = composCmdParam(BIND_TOKEN);
//		}
		JsonObject params = composCmdParam(BIND_TOKEN);
		if (params == null) {
			logger.info("组装绑定token命令失败，无法投递");
			return null;
		}
	    logger.info("投递绑定token请求 packetId:"+packetId);
	   
	    try {
//	    	String urls = CommStats.BSServerUrl;
//			HttpClient http = new HttpClient();
//	    	http.getHttpConnectionManager().getParams().setConnectionTimeout(3000);  
//			http.getHttpConnectionManager().getParams().setSoTimeout(3000);
			
			HttpPostMethod get = new HttpPostMethod(url);
			get.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		    get.setParameter("params",params.toString());
//		    if (packetId %2 == 0) {
//		    	get.setParameter("service", "CreateMeeting");
//			}
//		    else {
//		    	get.setParameter("service", "BindToken");
//			}
		    get.setParameter("service", "BindToken");
			 try {
//				    JsonObject json = new JsonObject();
//				    json.addProperty("rc", "-10");
				    int statusCode = 0;
				    try {
						statusCode = http.executeMethod(get);
					} catch (Exception e) {
						statusCode = 0;
					}
					if (statusCode != HttpStatus.SC_OK) {
						logger.error("Method failed: " + get.getStatusLine());
//						dao.add(json.toString(), httpServerResult.BS_RESULT);
						dao.add("", httpServerResult.BS_RESULT,url);
					}else {
						//获得response的输入流					
						InputStream data = get.getResponseBodyAsStream();
						String str              = IOUtils.toString(data,"UTF-8");
						data.close();
						logger.info("获得数据，packetId:"+packetId);
						logger.info("数据:"+str);
						//这个map会被多线程访问 需要加锁
						//可以把这个map里的内容记到别的地方
						//一个单件类，线程完全
						//resultMap.put(info, status);
//						dao.add(str, httpServerResult.BS_RESULT);
						dao.add(str, httpServerResult.BS_RESULT,url);
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
