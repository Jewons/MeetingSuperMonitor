package meetingMonitorAgent;

public interface monitorAgentCBInterface{

	public static final int monitorAgentCBInterface_CB_type_sendcmd = 0;
	/*
     *  @param handle        对象句柄    
	 *  @param notifyType    参考:monitorAgentCBInterface_CB_type_sendcmd
	 *  @param cmdReq        请求的命令
	 *  @param  cmdResp      命令处理的结果   
	 *  
	 *  @说明 :  
	 *    	根据不同的时间类型，使用cmdReq 和 cmdResp 内容，
     * 		
	 */
	public abstract void monitorAgent_CallBack(int handle ,int notifyType,String cmdReq,String cmdResp);

	
}