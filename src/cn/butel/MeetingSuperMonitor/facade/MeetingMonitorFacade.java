package cn.butel.MeetingSuperMonitor.facade;



import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;




import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorHttpServerDAOImpl;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorServiceServerDAOImpl;
import cn.butel.MeetingSuperMonitor.DAO.impl.MonitorServiceServerStatusDAOImpl;
import cn.butel.MeetingSuperMonitor.result.ItemsResult;


@Service
@Path("/MeetingMontior")
@SuppressWarnings("unchecked")
public class MeetingMonitorFacade {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	private MonitorBaseDAO dao     = new MonitorHttpServerDAOImpl();
	private MonitorBaseDAO ssdao  = new MonitorServiceServerDAOImpl();
	private MonitorBaseDAO sssdao = new MonitorServiceServerStatusDAOImpl();
	
	@Path("/getNpsServerStatus")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ItemsResult<Object> getNpsServerStatus(
			@FormParam("ip") String ip,
			@FormParam("type") int type,
			@FormParam("index") int index){
		logger.info("进入server的getNpsServerStatus");
		ItemsResult<Object> result = new ItemsResult<Object>();
//		Object e = dao.get(httpServerResult.NPS_RESULT);
		Object e = dao.get(ip);
		int status =0;
		if (e == null) {
			logger.info("Nps结果队列为空");
			status = -2;
		}else if ((String)e =="") {
			status = -10;
//			logger.error("nps返回结果超时:"+status);
		}else {
			result.setInfo((String)e);
		}
		result.setResult(status);
		logger.info("退出server的getNpsServerStatus 结果:"+result.toString());
		return result;
	}
	
	@Path("/getECServerStatus")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ItemsResult<Object> getECServerStatus(
			@FormParam("ip") String ip,
			@FormParam("type") int type,
			@FormParam("index") int index){
		logger.info("进入server的getECServerStatus");
		ItemsResult<Object> result = new ItemsResult<Object>();
//		Object e = dao.get(httpServerResult.EC_RESULT);
		Object e = dao.get(ip);
		int status = 0;
		if (e == null) {
			logger.info("EC缓存队列为空");
			status = -2;
		}else if ((String)e =="") {
			status = -10;
//			logger.error("EC返回结果超时:"+result.toString());
		}else {
			result.setInfo((String)e);
		}
		result.setResult(status);
		logger.info("退出server的getECServerStatus:"+result.toString());
		return result;
	}
	
	@Path("/getBSServerStatus")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ItemsResult<Object> getBSServerStatus(
			@FormParam("ip") String ip,
			@FormParam("type") int type,
			@FormParam("index") int index){
		logger.info("进入server的getBSServerStatus");
		ItemsResult<Object> result = new ItemsResult<Object>();
//		Object e = dao.get(httpServerResult.BS_RESULT);
		Object e = dao.get(ip);
		int status = 0;
		if (e == null) {
			logger.info("BS结果队列为空");
			status = -2;
		}else if ((String)e == "") {
			status = -10;
//			logger.error("BS返回结果超时:"+status);
		}else {
			result.setInfo((String)e);
		}
		result.setResult(status);
		logger.info("退出server的getBSServerStatus:"+result.toString());
		return result;
	}
	
	@Path("/getUpdateServerStatus")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ItemsResult<Object> getUpdateServerStatus(
			@FormParam("ip") String ip,
			@FormParam("type") int type,
			@FormParam("index") int index){
		logger.info("进入server的getUpdateServerStatus");
		ItemsResult<Object> result = new ItemsResult<Object>();
//		Object e = dao.get(httpServerResult.UPDATE_RESULT);
		Object e = dao.get(ip);
		int status = 0;
		if (e == null) {
			logger.info("update结果队列为空");
			status = -2;
			result.setResult(status);
		}else if ((String)e == "") {
			status = -10;
//			logger.error("BS返回结果超时:"+status);
		}else {
			result.setInfo((String)e);
		}
		result.setResult(status);
		logger.info("退出server的getUpdateServerStatus:"+result.toString());
		return result;
	}
	
	//获取所有监控服务器信息 ，用于业务展示
	@Path("/getAllMonitorServerInfo")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ItemsResult<Object> getAllMonitorServerInfo(
			@FormParam("index") int index){
		logger.info("进入server的getAllMonitorServerInfo");
		ItemsResult<Object> result = new ItemsResult<Object>();
		List<Object> list = ssdao.getAllServerInfo();
		logger.info("getAllMonitorServerInfo List:"+list.size());
		List<Object> list2 = dao.getAllServerInfo();
		logger.info("getAllMonitorServerInfo httpList:"+list2.size());
//		list.addAll(list2);
		List<Object> listTotal = new ArrayList<Object>();
		listTotal.addAll(list);
		listTotal.addAll(list2);
		logger.info("getAllMonitorServerInfo Listtotal:"+listTotal.size());
//		list.addAll(dao.getAllServerInfo());
//		List<ServiceServerInfo> serverList = new ArrayList<ServiceServerInfo>();
//		for (Object e : list) {
//			serverList.add((ServiceServerInfo)e);
//		}
		result.setResult(0);
		result.setCount(listTotal.size());
		result.setItems(listTotal);
		logger.info("退出server的getAllMonitorServerInfo");
		return result;
	}
	
	//获取监控服务器的存活状态
	@Path("/getServerLiveStatus")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ItemsResult<Object> getServerLiveStatus(
			@FormParam("ip") String serverIp,
			@FormParam("type") int serverType){
		logger.info("进入server的getServerLiveStatus 获取服务器:"+serverIp +" 类型:"+serverType);
		ItemsResult<Object> result = new ItemsResult<Object>();
		Object e = sssdao.get(serverIp);
		if (e == null) {
			result.setResult(-1);
		}else {
			result.setResult((Integer)e -1);
		}
		logger.info("result:"+result.toString());
		return result;
	}
	
	//获取监控服务器业务数据
	@Path("/getServerService")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ItemsResult<Object> getServerService(
			@FormParam("ip") String serverIp,
			@FormParam("type") int serverType){
		logger.info("进入server的getServerService 获取服务器:"+serverIp +" 类型:"+serverType);
		ItemsResult<Object> result = new ItemsResult<Object>();
		Object e = ssdao.get(serverIp);
		if (e == null) {
			result.setResult(-100);
		}else {
			result.setResult(0);
			result.setInfo((String)e);
		}
		logger.info("result:"+result.toString());
		return result;
	}
}
