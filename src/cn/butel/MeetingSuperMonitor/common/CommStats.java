package cn.butel.MeetingSuperMonitor.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 配置文件读取
 * @author Belie
 */
public class CommStats {
	
	private static final int STP                 = 1;
	
	private static final int STPRC             = 2;
	
	private static final int RELAY             = 3;
	
	private static final int  RELAYRC        = 4;
	
	private static final int  NPS                = 5;
	
	private static final int  EC                = 6;
	
	private static final int  BSServer         = 7;
	
	private static final int  Update                = 8;
	
	public static final String version;
	
	public static final String npsUrl;
	
	public static final String ECUrl;
	
	public static final String BSServerUrl;
	
	public static final String UpdateUrl;
	
	public static final String imei;
	
	public static final String name;
	
	public static final String accountId;
	
	public static final String token;
	
	public static  String[] stpList;
	
	public static  String[] stprcList;
	
	public static  String[] relayrcList;
	
	public static  String[] relayList;
	
	public static  String[] npsUrls;
	
	public static  String[] ECUrls;
	
	public static  String[] BSServerUrls;
	
	public static  String[] UpdateUrls;
	
	public static final String monitorAgentIp;
	
	public static final short monitorAgentPort;
	
	public static Map<String, String> relayIdMap = new HashMap<String, String>();
	
	public static String npsFilePath;
	public static String ecFilePath;
	public static String bsFilePath;
	public static String updateFilePath;
	static
	   {  
		   version = ResourceBundle.getBundle("config").getString("version");
		   npsUrl  = ResourceBundle.getBundle("config").getString("npsUrl");
		   ECUrl    = ResourceBundle.getBundle("config").getString("ECUrl"); 
		   BSServerUrl    = ResourceBundle.getBundle("config").getString("BSServerUrl"); 
		   UpdateUrl= ResourceBundle.getBundle("config").getString("UpdateUrl"); 
		   imei    = ResourceBundle.getBundle("config").getString("imei");
		   name    = ResourceBundle.getBundle("config").getString("name"); 
		   accountId    = ResourceBundle.getBundle("config").getString("accountId"); 
		   token    = ResourceBundle.getBundle("config").getString("token"); 
		   getServerList("config", "stp", CommStats.STP);
		   getServerList("config", "stprc", CommStats.STPRC);
		   getServerList("config", "relay", CommStats.RELAY);
		   getServerList("config", "relayrc", CommStats.RELAYRC);
		   getServerList("config", "npsUrl", CommStats.NPS);
		   getServerList("config", "ECUrl", CommStats.EC);
		   getServerList("config", "UpdateUrl", CommStats.Update);
		   getServerList("config", "BSServerUrl", CommStats.BSServer);
		   monitorAgentIp = ResourceBundle.getBundle("config").getString("monitorAgentIp");
		   monitorAgentPort = Short.parseShort(ResourceBundle.getBundle("config").getString("monitorAgentPort"));
		 
		   JSONArray array = ConfigToJson.getJSONObject("relayId.xml");
		   for (int i = 0; i < array.size(); i++) {
			   JSONObject json = array.getJSONObject(i);
			   relayIdMap.put(json.getString("ip"), json.getString("id"));
			   }
		   
		   npsFilePath = getPath("nps.txt");
		   ecFilePath = getPath("ec.txt");
		   bsFilePath = getPath("bs.txt");
		   updateFilePath = getPath("updateServer.txt");
		     
	   }
	
	private  static void getServerList(String fileName,String serverName,int serverType){
		String server = ResourceBundle.getBundle(fileName).getString(serverName);
		if (server == "" || server.lastIndexOf(":") ==-1){
			return;
		}else {
				switch (serverType) {
				case CommStats.STP:
					CommStats.stpList = server.split(",");
					break;
				case CommStats.STPRC:
					CommStats.stprcList = server.split(",");
					break;
				case CommStats.RELAY:
					CommStats.relayList = server.split(",");
					break;
				case CommStats.RELAYRC:
					CommStats.relayrcList = server.split(",");
					break;
				case CommStats.NPS:
					CommStats.npsUrls = server.split(",");
					break;
				case CommStats.EC:
					CommStats.ECUrls = server.split(",");
					break;
				case CommStats.BSServer:
					CommStats.BSServerUrls = server.split(",");
					break;
				case CommStats.Update:
					CommStats.UpdateUrls = server.split(",");
					break;
				default:
					break;
				}
				return ;
			}
		
	}
	
	private static String getPath(String fileName){
		String path = ConfigToJson.class.getClassLoader().getResource("/").toString();
		path = path.substring(6);
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		path += fileName;
		return path;
	}
	
}
