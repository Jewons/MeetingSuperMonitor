package meetingMonitorAgent;

public interface monitorAgentCBInterface{

	public static final int monitorAgentCBInterface_CB_type_sendcmd = 0;
	/*
     *  @param handle        ������    
	 *  @param notifyType    �ο�:monitorAgentCBInterface_CB_type_sendcmd
	 *  @param cmdReq        ���������
	 *  @param  cmdResp      �����Ľ��   
	 *  
	 *  @˵�� :  
	 *    	���ݲ�ͬ��ʱ�����ͣ�ʹ��cmdReq �� cmdResp ���ݣ�
     * 		
	 */
	public abstract void monitorAgent_CallBack(int handle ,int notifyType,String cmdReq,String cmdResp);

	
}