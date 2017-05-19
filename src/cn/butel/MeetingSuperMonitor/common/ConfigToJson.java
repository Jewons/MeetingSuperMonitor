package cn.butel.MeetingSuperMonitor.common;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import net.sf.json.JSON;
import net.sf.json.JSONArray;

public class ConfigToJson {
//	public static JSONArray getJSONArray(String key){
//		 JSONObject jsono = getJSONObject();
//		 JSONArray jarray =  jsono.getJSONArray(key);
//		return jarray;
//	}
	
	
	public static JSONArray getJSONObject(String fileName) {
		 //TODO 如果发布到tomcat中，上面的地址需要修改
		String path = ConfigToJson.class.getClassLoader().getResource("/").toString();
		path = path.substring(6);
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		 JSON json = XML2JSON.json(path+fileName);
//		 JSON json = XML2JSON.json(path+"relayId.xml");
//		 JSONObject jsono = JSONObject.fromObject(json);
		 JSONArray array = JSONArray.fromObject(json);
		return array;
	}
	
}
