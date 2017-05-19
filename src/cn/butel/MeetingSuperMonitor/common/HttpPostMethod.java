package cn.butel.MeetingSuperMonitor.common;

import java.io.IOException;

public class HttpPostMethod extends org.apache.commons.httpclient.methods.PostMethod{  
      
    public HttpPostMethod(String uri) { 
        super(uri);  
    }  
  
      
    /** 
     * Get response as string whether response is GZipped or not 
     *  
     * @return 
     * @throws IOException 
     */  
    public byte[] getResponseBodyover() throws IOException {  
        if (getResponseBody() != null || getResponseStream() != null) {  
            if(getResponseHeader("Content-Encoding") != null  
                     && getResponseHeader("Content-Encoding").getValue().toLowerCase().indexOf("gzip") > -1) {  
                    //For GZip response  
                  return   this.getResponseBody();
                }  else {  
                //For deflate response  
                return super.getResponseBody();  
            }  
        } else {  
            return null;  
        }  
    }  
}  