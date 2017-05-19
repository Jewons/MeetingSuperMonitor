package cn.butel.MeetingSuperMonitor.DAO.impl;

import java.util.List;

import cn.butel.MeetingSuperMonitor.DAO.MonitorBaseDAO;
import cn.butel.MeetingSuperMonitor.entity.ServiceServerResult;

public class MonitorServiceServerStatusDAOImpl implements MonitorBaseDAO{

	private ServiceServerResult result = ServiceServerResult.getInstance();
	
	@Override
	public int add(Object e, Object type) {
		String ip = (String)type;
		synchronized (result.statusMap) {
			result.statusMap.put(ip, (Integer)e);
		}
		return 0;
	}

	@Override
	public int remove(Object e, Object type) {
		String ip = (String)type;
		synchronized (result.statusMap) {
			result.statusMap.remove(ip);
		}
		return 0;
	}

	@Override
	public void clear() {
		synchronized (result.statusMap) {
			result.statusMap.clear();
		}
	}

	@Override
	public Object get(Object type) {
		String serverType = (String)type;
		synchronized (result.statusMap) {
			Object e = (Object)result.statusMap.get(serverType);
			return e;
		}
	}

	@Override
	public List<Object> getAllServerInfo() {
		return null;
	}

	@Override
	public int add(Object data, Object type, Object ip) {
		// TODO Auto-generated method stub
		return 0;
	}

}
