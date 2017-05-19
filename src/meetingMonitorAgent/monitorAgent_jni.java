package meetingMonitorAgent;

public class monitorAgent_jni {


  /*��ȡ����״̬����ֵ*/
  public static final int RunState_offline = 0;  //�Ͽ�
  public static final int RunState_online = 1;	 //��


  public final static native int Init(monitorAgentCBInterface cb);

  /*
  *  ��Ӽ�صķ�����
  *  ����ֵ:
  *       ���ڵ���0, ��ʾ�ɹ���ֵΪ���(handle)
  *       ����	 ��ʾʧ��
  */
  public final static native int addMonitor(String localip,int  localPort,
	String serverIp,int  serverPort);
  public final static native void delMonitor(int handle);

  /*
  *����/ֹͣ,��ⴴ���ķ�����
  *����ֵ: 0 - �ɹ������� - ʧ��
  */
  public final static native int Start(int handle);
  public final static native void Stop(int handle);

  /*ֻ�гɹ�(0)���лص�*/
  public final static native int sendCmd(int handle,String strCmd,int len);

  /*��ù���״̬
  * ����ֵ: �ο�RunState_offline
  */
  public final static native int getWorkState(int handle);

  static {
	  String path = System.getProperty("java.library.path");
	  System.out.println(path);
//	  System.load("E:\\MyEclipse 10\\MeetingSuperMonitor\\MonitorAgent.dll");
      System.loadLibrary("MonitorAgent");
  }
}
