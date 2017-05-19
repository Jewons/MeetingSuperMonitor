package cn.butel.MeetingSuperMonitor.ws.proxy;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import cn.butel.MeetingSuperMonitor.common.HttpPostMethod;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
/**
 * spring类
 * 
 * @Author 刘艳伟
 * @date 2014-2-17上午10:54:41
 * @类功能　
 */

@Component
public class RestTemplateProxy implements InitializingBean {

	protected Logger logger = Logger.getLogger(RestTemplateProxy.class);

	@Autowired
	private RestTemplate restTemplate;



	public Result postJson(String url, String json) {
		String jsonResult = null;
		try {
			jsonResult = restTemplate.exchange(url, HttpMethod.POST, createHttpEntity(json), String.class).getBody();
		} catch (Exception e) {
			logger.error("调用接口:" + url + "失败");
			return null;
		}
		return handleResult(jsonResult);
	}

	public JsonObject postJsonWithReturnJSONObject(String url, String json) {
		String jsonResult = null;
		try {
			jsonResult = restTemplate.postForObject( url, createHttpEntity(json), String.class);
		} catch (Exception e) {
			logger.error("调用接口:" + url + "失败");
			return null;
		}
		return getJsonObject(jsonResult);
	}

	public Result postForm(String url, MultiValueMap<String, Object> form) {
		String json = null;
		try {
			json = restTemplate.postForObject( url, form, String.class);
		} catch (Exception e) {
			logger.error("调用接口:"  + url + "失败" + "(" + e.getMessage() + ")");
			return null;
		}
		return handleResult(json);
	}

	// TODO spring中被用到的
	public JsonObject postFormWithReturnJSONObject(String url, MultiValueMap<String, Object> form) {
		String json = null;
		try {
			json = restTemplate.postForObject( url, form, String.class);
		} catch (Exception e) {
			logger.error("调用接口:" + url + "失败" + "(" + e.getMessage() + ")");
			return null;
		}
		return getJsonObject(json);
	}
	
	//TODO spring中被用到的
	public HttpPostMethod httpPostMothed(String url) {
			return new HttpPostMethod(url);
	}
	
	public HttpPostMethod httpPostToOutSide(String url){
		return new HttpPostMethod(url);
	}

	public static HttpEntity<String> createHttpEntity(String json) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("Accept", "application/json");
		requestHeaders.set("Accept-Charset", "utf-8");
		requestHeaders.set("Content-Type", "application/json;charset=utf-8");
		HttpEntity<String> httpEntity = new HttpEntity<String>(json, requestHeaders);
		return httpEntity;
	}

	private Result handleResult(String json) {
		if (StringUtils.isEmpty(json)) {
			logger.error("数据访问层返回空字符串:" + json);
			return null;
		}
		try {
			JsonObject jsonObject = getJsonObject(json);
			Result result = new Result(jsonObject.get("result").getAsInt(), json);
			return result;
		} catch (Exception e) {
			logger.error("解析返回JSON出错");
			logger.error(e.getMessage(), e);
			return null;
		}

	}

	private JsonObject getJsonObject(String json) {
		if (StringUtils.isEmpty(json)) {
			logger.error("数据访问层返回空字符串:" + json);
			return null;
		}
		try {
			Gson gs = new Gson();
			 JsonElement element = gs.fromJson(json, JsonElement.class);
			 JsonObject jsonObject = element.getAsJsonObject();
			return jsonObject;
		} catch (Exception e) {
			logger.error("解析返回JSON出错!");
			logger.error(e.getMessage(), e);
			return null;
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		return;
	}

}

