package cn.butel.MeetingSuperMonitor.DAO;

import java.util.List;

public interface MonitorBaseDAO {
	
	public int add(Object e, Object type);
	
	public int add(Object data,Object type,Object ip);
	
	public int remove(Object e,Object type);
	
	public void clear();
	
	public Object get(Object type);
	
	public List<Object> getAllServerInfo();
}
