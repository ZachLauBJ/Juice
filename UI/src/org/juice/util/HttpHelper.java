package org.juice.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.juice.ErrorHandler;
import org.juice.Log;


public class HttpHelper {
		
	public static synchronized boolean URLisAvailable(String url){
		boolean flag = false;
		int counts = 0;  
		while (counts < 3) {  
			int state = -1;
		    try {  
		    	HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();  
		    	state = con.getResponseCode();  
		    	if (state == 200) {  
		    		Log.info("URL is available: "+url);
		    		flag = true;
		    		break;
		    	}  	    	
		    }catch (Exception e) {  
		    	counts++;   
		    	ErrorHandler.continueRunning(e, "URL不可用，第" 
		    			+ counts + "次链接" + url + "为"+state, true);
		    	continue;  
		    }  
		} 
		return flag;	
	}
	
	public static synchronized boolean URLisAvailable(List<String> url){
		boolean flag = true;
		int counts = 0;  
		if (url == null || url.size() <= 0) {    
			flag = false;     
			ErrorHandler.stopRunning("链接数组为空", false);
		}  
		   
		for(int i=0;i<url.size();i++){
			if(url.get(i).equalsIgnoreCase(null)){
			ErrorHandler.continueRunning(url.get(i)+"为空链接", false);
			flag = false;
			}
		while (counts < 3) {  
			int state = -1;
		    try {  
		    	HttpURLConnection con = (HttpURLConnection) new URL(url.get(i)).openConnection();  
		    	state = con.getResponseCode();  
		    	if (state == 200) {  
		    		Log.info("URL is available: "+url.get(i));
		    		break;
		    	}  	    	
		    }catch (Exception e) {  
		    	counts++;   
		    	ErrorHandler.continueRunning(e, "URL不可用，第" 
		    			+ counts + "次链接" + url.get(i) + "为"+state, true);
		    	flag=false;
		    	continue;  
		    }  
		}  
		}
		return flag;  		
	}
	
	public static String doGet(String url){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);	
		String responseText = null;
		try {
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if(code == 200)  
			{  	
				responseText = EntityUtils.toString(response.getEntity());
			}else{
				ErrorHandler.continueRunning("StatusCode:"+code,false);
			}
		} catch (ClientProtocolException e) {
			ErrorHandler.continueRunning(e, "发送get请求失败!"+url, false);
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "发送get请求失败:"+url, false);
		}		
		return responseText;
	}
	
	public static String doGet(String url, Map<String, String> params){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);	
		String responseText = null;
		url = (null == params ? url : url + "?" + parseParam(params));
		try {
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if(code == 200)  
			{  	
				responseText = EntityUtils.toString(response.getEntity());
			}else{
				ErrorHandler.continueRunning("StatusCode:"+code,false);
			}
		} catch (ClientProtocolException e) {
			ErrorHandler.continueRunning(e, "发送get请求失败!"+url, false);
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "发送get请求失败!"+url, false);
		}
		return responseText;
	}
	
	public static String doGet(String url, Map<String, String> headers, Map<String, String> params){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);	
		String responseText = null;
		url = (null == params ? url : url + "?" + parseParam(params));
		get.setHeaders(parseHeader(headers));
		try {
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if(code == 200)  
			{  	
				responseText = EntityUtils.toString(response.getEntity());
			}else{
				ErrorHandler.continueRunning("StatusCode:"+code,false);
			}
		} catch (ClientProtocolException e) {
			ErrorHandler.continueRunning(e, "发送get请求失败!"+url, false);
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "发送get请求失败!"+url, false);
		}
		return responseText;
	}
	
	public static String doPost(String url, Map<String, Object> params) {
		if(params == null || params.isEmpty()){
			ErrorHandler.stopRunning("Post参数为空", false);
		}
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();  
		String responseText = null;
		for(String p : params.keySet()){
			paramsList.add(new BasicNameValuePair(p, (String) params.get(p)));  
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
			HttpResponse response = client.execute(post); 
			int code = response.getStatusLine().getStatusCode();
			if(code == 200)  
			{  	
				responseText = EntityUtils.toString(response.getEntity());
			}else{
				ErrorHandler.continueRunning("StatusCode:"+code,false);
			}			
		} catch (UnsupportedEncodingException e) {
			ErrorHandler.continueRunning(e, "发送post请求失败!"+url, false);
		} catch (ClientProtocolException e) {
			ErrorHandler.continueRunning(e, "发送post请求失败!"+url, false);
		} catch (ParseException e) {
			ErrorHandler.continueRunning(e, "发送post请求失败!"+url, false);
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "发送post请求失败!"+url, false);
		}
		return responseText;		
	}
	
    private static Header[] parseHeader(Map<String, String> headers) {
        if (null == headers || headers.isEmpty()) {
            return getDefaultHeaders();
        }
        Header[] allHeader = new BasicHeader[headers.size()];
        int i = 0;
        for (String str : headers.keySet()) {
            allHeader[i] = new BasicHeader(str, headers.get(str));
            i++;
        }
        return allHeader;
    }

    private static Header[] getDefaultHeaders() {
        Header[] allHeader = new BasicHeader[2];
        allHeader[0] = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
        allHeader[1] = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        return allHeader;
    }
		
    private static String parseParam(Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            sb.append(key + "=" + params.get(key) + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
