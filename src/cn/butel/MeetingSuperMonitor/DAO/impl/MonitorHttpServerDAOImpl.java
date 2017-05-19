package cn.butel.MeetingSuperMonitor.DAO.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;



import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.common.CommStats;
import cn.butel.MeetingSuperMonitor.common.ConfigToJson;
import cn.butel.MeetingSuperMonitor.common.FileOperation;
import cn.butel.MeetingSuperMonitor.entity.httpServerResult;

public class MonitorHttpServerDAOImpl implements MonitorBaseDAO {

	protected Logger logger = Logger.getLogger(MonitorHttpServerDAOImpl.class);
	private httpServerResult result = httpServerResult.getInstance();
	
	private final static int LIST_MAX = 10;
	
	@Override
	public int add(Object e, Object type) {
		int serverType = (Integer)type;
		synchronized(this.result){
			switch (serverType) {
			case httpServerResult.NPS_RESULT:
				if(savaResut(result.npsResultList,serverType))
				result.npsResultList.clear();
				result.npsResultList.add(e);
				logger.info("nps队列个数:"+result.npsResultList.size());
				break;
			case httpServerResult.EC_RESULT:
				if(savaResut(result.ecResultList,serverType))
				result.ecResultList.clear();
				result.ecResultList.add(e);
				logger.info("EC队列个数:"+result.ecResultList.size());
				break;
			case httpServerResult.BS_RESULT:
				if(savaResut(result.bsResultList,serverType))
				result.bsResultList.clear();
				result.bsResultList.add(e);
				logger.info("BS队列个数:"+result.bsResultList.size());
				break;
			case httpServerResult.UPDATE_RESULT:
				if(savaResut(result.updateResultList,serverType))
				result.updateResultList.clear();
				result.updateResultList.add(e);
				logger.info("update队列个数:"+result.updateResultList.size());
				break;
			default:
				break;
			}
			
		}
		return 0;
	}
	
	@Override
	public int add(Object data,Object type,Object ip){
		synchronized (result.resultMap) {
			Vector<Object> list = result.resultMap.get(ip);
			if (list == null) {
				return -1;
			}else {
				if (savaResut(list,(Integer)type,(String)ip)) 
					list.clear();
			}
			list.add(data);
//			result.resultMap.put(ip, list);
		}
		return 0;
	}

	@Override
	public int remove(Object o, Object type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		synchronized (this.result) {
			this.result.bsResultList.clear();
			this.result.ecResultList.clear();
			this.result.npsResultList.clear();
			this.result.updateResultList.clear();
			this.result.resultMap.clear();
		}
	}

//	@Override
//	public Object get(Object type) {
//		int serverType = (Integer)type;
//		synchronized(this.result){
//			Object e;
//			switch (serverType) {
//			case httpServerResult.NPS_RESULT:
//				e = getObject(result.npsResultList);
//				break;
//			case httpServerResult.EC_RESULT:
//				e = getObject(result.ecResultList);
//				break;
//			case httpServerResult.BS_RESULT:
//				e = getObject(result.bsResultList);
//				break;
//			case httpServerResult.UPDATE_RESULT:
//				e = getObject(result.updateResultList);
//				break;
//			default:
//				e = null;
//				break;
//			}
//			return e;
//		}
//	}
	
	@Override
	public Object get(Object type) {
		Object e = null;
		synchronized (result.resultMap) {
			Vector<Object> results = result.resultMap.get(type);
			if (results != null ) {
				e = getObject(results);
			}
			return e;
		}
	}
	@Override
	public List<Object> getAllServerInfo() {
		return result.serverInfoList;
	}
	
	private Object getObject(List<Object> list){
		Object e;
		if (list.isEmpty()) {
			e = null;
		}else {
			e = list.get(list.size() -1);
		}
		return e;
	}
	
	private static boolean savaResut(Vector<Object> list,int type){
		if (list.size() >= LIST_MAX) {
			String str ="";
			for(int i = 0; i < list.size(); i++){
				str += (String)list.get(i) +"";
				str +="\n";
			}
			if (type ==httpServerResult.NPS_RESULT ) {
				FileOperation.contentToTxt(CommStats.npsFilePath, str);
			}
			if (type ==httpServerResult.EC_RESULT ) {
				FileOperation.contentToTxt(CommStats.ecFilePath, str);
			}
			if (type ==httpServerResult.BS_RESULT ) {
				FileOperation.contentToTxt(CommStats.bsFilePath, str);
			}
			if (type ==httpServerResult.UPDATE_RESULT ) {
				FileOperation.contentToTxt(CommStats.updateFilePath, str);
			}
			return true;
		}
		else 
			return false;
	}
	
	private  boolean savaResut(Vector<Object> list,int type, String ip){
		if (list.size() >= LIST_MAX) {
			String str ="\r\n";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			str +=df.format(new Date()).toString() +"\r\n";// new Date()为获取当前系统时间
			for(int i = 0; i < list.size(); i++){
				str += (String)list.get(i) +"\r\n\r\n";
			}
			if (type == httpServerResult.NPS_RESULT) {
//				writeFile(CommStats.npsUrls,str,ip,type);
			}
			if (type == httpServerResult.EC_RESULT) {
//				writeFile(CommStats.ECUrls,str,ip,type);
			}
			if (type == httpServerResult.BS_RESULT) {
//				writeFile(CommStats.BSServerUrls,str,ip,type);
			}
			if (type == httpServerResult.UPDATE_RESULT) {
//				writeFile(CommStats.UpdateUrls,str,ip,type);
			}
			return true;
		}else {
				return false;
		}
	}
	
	private void writeFile(String[] strings,String context,String ip,int type){
		logger.info("开始写:"+System.currentTimeMillis());
		for (int i = 0 ; i < strings.length; i++) {
			if (strings[i] == ip) {
				if (type == httpServerResult.NPS_RESULT) {
					FileOperation.contentToTxt(getPath("nps"+ (i+1) + ".txt"),context);
				}
				if (type == httpServerResult.EC_RESULT) {
					FileOperation.contentToTxt(getPath("ec"+ (i+1) + ".txt"),context);
				}
				if (type == httpServerResult.BS_RESULT) {
					FileOperation.contentToTxt(getPath("bs"+ (i+1) + ".txt"),context);
				}
				if (type == httpServerResult.UPDATE_RESULT) {
					FileOperation.contentToTxt(getPath("update"+ (i+1) + ".txt"),context);
				}
			}
		}
		logger.info("写完:"+System.currentTimeMillis());
	}
	
	private static String getPath(String fileName){
		String path = ConfigToJson.class.getClassLoader().getResource("/").toString();
		path = path.substring(6);
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		path += fileName;
		return path;
	}

}
