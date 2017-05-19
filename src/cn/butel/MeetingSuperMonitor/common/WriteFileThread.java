package cn.butel.MeetingSuperMonitor.common;

public class WriteFileThread extends Thread{
	private WriteFileControl writeFileControl = WriteFileControl.getInstance();
	
	public void run(){
		while(true){
			if (writeFileControl.isActive()) {
				writeFileControl.writeFile();
			}
			try {
				sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
