package cn.butel.MeetingSuperMonitor.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cn.butel.MeetingSuperMonitor.common.CommStats;



public class httpServerResult {
	
	private static httpServerResult result;  
    
	public Vector<Object> npsResultList;
	public Vector<Object> bsResultList;
	public Vector<Object> updateResultList;
	public Vector<Object> ecResultList;
	
	public Map<Object, Vector<Object>>  resultMap;
	
	public List<Object> serverInfoList;
	
	public final static int NPS_RESULT = 1;
	
	public final static int BS_RESULT   =  2;
	
	public final static int UPDATE_RESULT = 3;
	
	public final static int EC_RESULT = 4;
	
	private httpServerResult (){
		npsResultList = new Vector<Object>();
		bsResultList   = new Vector<Object>();
		updateResultList   = new Vector<Object>();
		ecResultList   = new Vector<Object>();
		resultMap = new HashMap<Object, Vector<Object>>();
		serverInfoList = new ArrayList<Object>();
		ServiceServerInfo info;
		for (int i = 0; i < CommStats.npsUrls.length; i++) {
			Vector<Object> resultList = new Vector<Object>();
			info = new ServiceServerInfo(CommStats.npsUrls[i],5);
			serverInfoList.add(info);
			resultMap.put(info.getIp(),resultList);
		}
		for (int i = 0; i < CommStats.ECUrls.length; i++) {
			Vector<Object> resultList = new Vector<Object>();
			info = new ServiceServerInfo(CommStats.ECUrls[i],6);
			serverInfoList.add(info);
			resultMap.put(info.getIp(),resultList);
		}
		for (int i = 0; i < CommStats.BSServerUrls.length; i++) {
			Vector<Object> resultList = new Vector<Object>();
			info = new ServiceServerInfo(CommStats.BSServerUrls[i],7);
			serverInfoList.add(info);
			resultMap.put(info.getIp(),resultList);
		}
		for (int i = 0; i < CommStats.UpdateUrls.length; i++) {
			Vector<Object> resultList = new Vector<Object>();
			info = new ServiceServerInfo(CommStats.UpdateUrls[i],8);
			serverInfoList.add(info);
			resultMap.put(info.getIp(),resultList);
		}
	}
   
	public static synchronized httpServerResult getInstance() {  
   
		if (result == null) {  
			result = new httpServerResult();  
    }  
    return result;  
    }

	public Vector<Object> getNpsResultList() {
		return npsResultList;
	}

	public void setNpsResultList(Vector<Object> npsResultList) {
		this.npsResultList = npsResultList;
	}

	public Vector<Object> getBsResultList() {
		return bsResultList;
	}

	public void setBsResultList(Vector<Object> bsResultList) {
		this.bsResultList = bsResultList;
	}

	public Vector<Object> getUpdateResultList() {
		return updateResultList;
	}

	public void setUpdateResultList(Vector<Object> updateResultList) {
		this.updateResultList = updateResultList;
	}

	public Vector<Object> getEcResultList() {
		return ecResultList;
	}

	public void setEcResultList(Vector<Object> ecResultList) {
		this.ecResultList = ecResultList;
	}

	
	
}
