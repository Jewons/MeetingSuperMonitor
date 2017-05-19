package cn.butel.MeetingSuperMonitor.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class WriteFileControl {
	
	private static WriteFileControl WriteFileControl;
	
	private WriteFileControl(){
		bufferWriteList = new ArrayList<Object>();
		active               = false;
	}
	
	public static WriteFileControl getInstance(){
		if (WriteFileControl == null) {
			WriteFileControl = new WriteFileControl();
		}
		return WriteFileControl;
	}
	private List<Object> bufferWriteList;
	
	private String            filePath;
	
	private boolean        active;
	


	public synchronized void writeFile(){
		if (filePath == "" || bufferWriteList.isEmpty()) {
			return;
		}else {
			String str ="\r\n";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			str +=df.format(new Date()).toString() +"\r\n";// new Date()为获取当前系统时间
			for(int i = 0; i < bufferWriteList.size(); i++){
				str += (String)bufferWriteList.get(i) +"\r\n\r\n";
			}
			FileOperation.contentToTxt(filePath,str);
			active = false;
		}
	}
	
//	public void run(){
//		System.out.print("runrunrun");
//		while(true){
//			if (active) {
//				writeFile();
//			}
//			try {
//				sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public synchronized List<Object> getBufferWriteList() {
		return bufferWriteList;
	}

	public synchronized void setBufferInfo(List<Object> bufferWriteList,String filePath) {
		this.filePath = filePath;
		this.bufferWriteList.clear();
		Collections.addAll(this.bufferWriteList, new Object[bufferWriteList.size()]);
		Collections.copy(this.bufferWriteList, bufferWriteList);
		active = true;
//		this.bufferWriteList = bufferWriteList;
////		System.out.println("bbbb:"+this.bufferWriteList.size());
	}

	
	public synchronized boolean isActive() {
		return active;
	}
}
