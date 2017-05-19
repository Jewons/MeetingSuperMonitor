package cn.butel.MeetingSuperMonitor.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.httpclient.HttpClient;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionContext;
import cn.butel.MeetingSuperMonitor.ws.proxy.RestTemplateProxy;

public abstract class BaseServerMonitor extends Thread implements IServerMonitor,SessionAware, ServletRequestAware, ServletResponseAware{

	
	
	protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected Map<String, Object> map;
    
    protected final static int QUERY_NPS                =   1;
    
    protected final static int CREATE_TOKEN           =   2;
    
    protected final static int  BIND_TOKEN              =   3;
    
    protected final  static int  CREATE_MEETING      =  4;
    
    protected final  static int  CHECK_VERSION     =  4;
    
    protected HttpClient http;
    
    protected Gson gson;
    
    public BaseServerMonitor(){
    	http = new HttpClient();
    	http.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
    	http.getHttpConnectionManager().getParams().setSoTimeout(3000);
    }
    @Autowired
    protected RestTemplateProxy proxy;
    
    public void setResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    public void setSession(Map<String, Object> map)
    {
        this.map = map;
    }

    public void setServletRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response)
    {
        this.response = response;
    }



    public void put(String key, Object value)
    {
        ActionContext context = ActionContext.getContext();
        context.put(key, value);
    }



    public Object get(String key)
    {
        ActionContext context = ActionContext.getContext();
        return context.get(key);
    }
    
    public Object getsession(String key){
        return map.get(key);
    }
    
    protected JsonObject getJson(String jsonStr){
    	if (gson == null) {
			gson = new Gson();
		}
    	JsonElement element = gson.fromJson(jsonStr, JsonElement.class);
    	JsonObject json = element.getAsJsonObject();
    	return json;
    }
}
