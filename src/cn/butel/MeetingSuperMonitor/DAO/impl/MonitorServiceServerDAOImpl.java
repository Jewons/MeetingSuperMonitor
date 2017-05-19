package cn.butel.MeetingSuperMonitor.DAO.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.common.CommStats;
import cn.butel.MeetingSuperMonitor.common.ConfigToJson;
import cn.butel.MeetingSuperMonitor.common.WriteFileControl;
import cn.butel.MeetingSuperMonitor.entity.ServiceServerResult;


public class MonitorServiceServerDAOImpl implements MonitorBaseDAO {

	protected Logger logger                        = Logger.getLogger(MonitorServiceServerDAOImpl.class);
	private ServiceServerResult result            = ServiceServerResult.getInstance();
	private WriteFileControl fileControl         = WriteFileControl.getInstance();
	
	private final int LIST_MAX = 10;
	//获取所有被监控的业务服务器信息
	@Override
	public List<Object> getAllServerInfo() {
		return result.serverList;
	}





	@Override
	public int add(Object e, Object type) {
		String ip = (String)type;
		synchronized (result.serviceMap) {
			List<Object> list = result.serviceMap.get(ip);
			if (list == null) {
//				list = new ArrayList<Object>();
				return -1;
			}
			if (savaResut(list, ip)) 
				list.clear();
			list.add(e);
//			result.serviceMap.put(ip, list);
			logger.info("服务器业务数据加入结果缓存队列 serverIp:"+ ip +"业务数据队列长度:"+list.size());
		}
		return 0;
	}





	@Override
	public int remove(Object e, Object type) {
		String ip = (String)type;
		synchronized (result.serviceMap){
			List<Object> list = result.serviceMap.get(ip);
			if (list == null) {
				return 0;
			}
			list.remove(e);
//			result.serviceMap.put(ip, list);
		}
		return 0;
	}





	@Override
	public void clear() {
		synchronized (result.serviceMap){
			result.serviceMap.clear();
		}
		
	}





	@Override
	public Object get(Object type) {
		synchronized (result.serviceMap){
			List<Object> list = result.serviceMap.get(type);
			if (list == null || list.size() == 0) {
				return null;
			}else {
				Object e =list.get(list.size()-1);
				return e;
			}
		}
	}
	
	private  boolean savaResut(List<Object> list,String  ip){
		if (list.size() >= LIST_MAX) {
//			String str ="\n";
//			for(int i = 0; i < list.size(); i++){
//				str += (String)list.get(i) +"\n";
//				str +="\n";
//			}
			String path = ConfigToJson.class.getClassLoader().getResource("/").toString();
			path = path.substring(6);
			try {
				path = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
			path += ipFormat(ip) +".txt";
			//test
//			fileControl.setBufferInfo(list, path);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * 将ip地址改为可写成文件形式 10.130.68.109:10001 改为10.130.68,109_10001
	 * @param ip
	 * @return
	 */
	private String ipFormat(String ip){
		String newIp = ip.replace(":", "_");
		return newIp;
	}





	@Override
	public int add(Object data, Object type, Object ip) {
		return 0;
	}
}
