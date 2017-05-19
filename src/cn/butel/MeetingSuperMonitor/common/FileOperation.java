package cn.butel.MeetingSuperMonitor.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileOperation {
 
 /**
  * 创建文件
  * @param fileName
  * @return
  */
 public static boolean createFile(File fileName)throws Exception{
	  boolean flag=false;
	  try{
	   if(!fileName.exists()){
	    fileName.createNewFile();
	    flag=true;
	   }
	  }catch(Exception e){
	   e.printStackTrace();
	  }
	  return true;
 } 
 
 /**
  * 读TXT文件内容
  * @param fileName
  * @return
  */
 public static String readTxtFile(File fileName)throws Exception{
	  String result=null;
	  FileReader fileReader=null;
	  BufferedReader bufferedReader=null;
	  try{
		  fileReader=new FileReader(fileName);
		   bufferedReader=new BufferedReader(fileReader);
	   try{
		   String read=null;
		    while((read=bufferedReader.readLine())!=null){
		     result=result+read+"\r\n";
	    }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	  }catch(Exception e){
		  e.printStackTrace();
	  }finally{
	   if(bufferedReader!=null){
		   bufferedReader.close();
	   }
	   if(fileReader!=null){
		   fileReader.close();
	   }
	  }
	  System.out.println("读取出来的文件内容是："+"\r\n"+result);
	  return result;
 }
 
 
 public static boolean writeTxtFile(String content,File  fileName)throws Exception{
	 RandomAccessFile mm=null;
	  boolean flag=false;
	  FileOutputStream o=null;
	  try {
	   o = new FileOutputStream(fileName);
	      o.write(content.getBytes("GBK"));
	      o.close();
	   flag=true;
	  } catch (Exception e) {
	   e.printStackTrace();
	  }finally{
	   if(mm!=null) mm.close();
	  }
	  return flag;
 }



public static void contentToTxt(String filePath, String content) {
//        String str = new String(); //原有txt内容
//        String s1 = new String();//内容更新
        try {
            File f = new File(filePath);
            if (f.exists()) {
                System.out.print("文件存在");
            } else {
                System.out.print("文件不存在");
                f.createNewFile();// 不存在则创建
            }
//            BufferedReader input = new BufferedReader(new FileReader(f));
//
//            while ((str = input.readLine()) != null) {
//                s1 += str + "\n";
//            }
//            input.close();
//            s1 += content;

//            BufferedWriter output = new BufferedWriter(new FileWriter(f));
//            output.write(s1);
//            output.close();
            try {
                //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                FileWriter writer = new FileWriter(filePath, true);
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}