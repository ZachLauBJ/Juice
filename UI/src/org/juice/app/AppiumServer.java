package org.juice.app;

import java.lang.reflect.Field;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.mina.util.AvailablePortFinder;
import org.juice.ErrorHandler;
import org.juice.Parameter;
import org.jvnet.winp.WinProcess;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AppiumServer implements AppiumServerType{
	
	private static final String serverPath = "/wd/hub";
	private static final String statusPath = "/wd/hub/status";
	private static final HttpClient httpClient = HttpClients.createDefault();
	private Process process;
	private int maxTryLinks = 3;		//最大尝试链接次数
	private long startTimeout = 30;		//启动服务的最大等待时长
	private String ip = "localhost";
	private String udid = "-1";			//指定设备串号
	private int appiumPort = -1;
	private int bootstrapPort = -1;
	private int selendroidPort = -1;
	private int chromeDriverPort = -1;
	
	/**
	 * 未自定义参数，实例化默认配置项的Appium服务
	 */
	public AppiumServer(){
		this(new Parameter());
	}
		
	public AppiumServer(Parameter parameters){
		init(parameters);	
	}
	
	/**
	 * 启动Appium服务
	 */
	public void startService(){
		int tryNumber = 0;
		try {
			process = Runtime.getRuntime().exec(commandBuilder());
			} catch (Exception e) {
				ErrorHandler.stopRunning(e, "启动AppiumServer失败:"+commandBuilder(), true);
		    }

		long start = System.currentTimeMillis();    //获取等待服务的起始时间     
		boolean state = isRunning();		//判断服务是否正常运行		
		     
		while (!state) {
			long end = System.currentTimeMillis();       //获取当前时间	      
		    if (end - start > startTimeout*1000&&tryNumber<maxTryLinks) {
		    	tryNumber = tryNumber+1;
		    	restartService();            	    	
		    }else if(tryNumber==maxTryLinks){
		    	ErrorHandler.stopRunning("启动AppiumServer失败:",true);
		    }
		    state = isRunning();		 //在循环结尾再获取运行状态
		}
	}
	
	/**
	 * 停止最新启动的Appium服务
	 */
	public void stopService() {
		try {
			WinProcess winp = new WinProcess(process);
			ErrorHandler.stopRunning("结束Appium Server进程:"+winp.getPid(),false);
			winp.killRecursively();
		} catch (Exception e) {
			ErrorHandler.stopRunning(e,"结束Appium Server进程异常!",false);
		}
	}
 	
	/**
	 * 关闭Appium服务命令行窗口
	 */
	public void closeLaunchedCmdWindow() {
		try {
			WinProcess winp = new WinProcess(this.getPid());
			ErrorHandler.stopRunning("关闭Appium Server命令行窗口"+winp.getPid(),false);
			winp.killRecursively();
		} catch (Exception e) {
			ErrorHandler.stopRunning(e,"关闭Appium Server命令行窗口异常!",false);
		}
	}
	
	/**
	 * 重启Appium服务
	 */
	public void restartService(){
		this.stopService();
		this.startService();
	}
 
	/**
	 * 通过发送http请求，检查回参json中的status字段判断Appium服务是否正常运行
	 */	
   public boolean isRunning() {
       try {
           URI uri = new URIBuilder().setScheme("http").setHost(ip)
                 	  .setPort(appiumPort).setPath(statusPath).build();
           HttpGet httpget;
           HttpResponse response;
           httpget = new HttpGet(uri);
           response = httpClient.execute(httpget);
           HttpEntity entity = response.getEntity();
           String rs = EntityUtils.toString(entity);
           JsonElement json = new JsonParser().parse(rs);
           int status = json.getAsJsonObject().get("status").getAsInt();
           return status == 0;

       } catch (Exception e) {
    	   ErrorHandler.continueRunning(e, "判断Appium Server服务状态异常!", false);
           return false;
       }
   }
 
   /**
	 * 获取Appium服务URL字符串
	 */
   public String getURL(){
       try {
    	   URI uri = new URIBuilder().setScheme("http").setHost(ip)
                     .setPort(appiumPort).setPath(serverPath).build();
            
           return uri.toURL().toString();
            
       } catch (Exception e) {
    	   ErrorHandler.continueRunning(e, "获取Appium Server服务URL异常!", false);
    	   return "-1";
       }  
   }
 
   /**
	 * 获取Appium服务进程PID
	 */
   public int getPid() {
       try {
           WinProcess winp = new WinProcess(process);
           return winp.getPid();
       } catch (Exception e) {
    	   ErrorHandler.continueRunning(e, "获取Appium Server服务进程PID异常!", false);
           return -1;
       }
   }
 
   /**
  	 * 获取Appium服务IP
  	 */
   public String getIP() {
       return ip;
   }
   
   /**
 	 * 获取Appium服务设备串号
 	 */
   public String getUdid() {
       return udid;
   }
   
   /**
	 * 获取Appium服务端口号
	 */
   public int getAppiumPort() {
       return appiumPort;
   }
 
   /**
	 * 获取Appium服务Bootstrap端口号
	 */
   public int getBootstrapPort() {
       return bootstrapPort;
   }
 
   /**
	 * 获取Appium服务Selendroid端口号
	 */
   public int getSelendroidPort() {
	   return selendroidPort;
   }
 
   /**
	 * 获取Appium服务ChromeDriver端口号
	 */
   public int getChromeDriverPort() {
	   return chromeDriverPort;
   }
 
   /**
  	 * 获取Appium服务详细配置信息(String)
  	 */
   @Override
   public String toString() {
       return "AppiumServer [pid=" + getPid() + ", ip=" + ip + serverPath+", appiumPort="
               + appiumPort + ", bootstrapPort=" + bootstrapPort
               + ", selendroidPort=" + selendroidPort + ", chromeDriverPort="
               + chromeDriverPort + ", udid="+udid+"]";
   }
 
   //初始化Server参数
   private void init(Parameter parameters){	   
	   appiumPort = AvailablePortFinder.getNextAvailable();
	   bootstrapPort = AvailablePortFinder.getNextAvailable();
	   selendroidPort = AvailablePortFinder.getNextAvailable();
	   chromeDriverPort = AvailablePortFinder.getNextAvailable();
	      
	   for(String name :parameters.asMap().keySet()){
		   Object value = parameters.get(name);
		   if(value!=null){
			   try {
				   Field field = this.getClass().getDeclaredField(name);
				   field.set(this, value);				
			} catch (NoSuchFieldException e) {
				ErrorHandler.stopRunning(e, "Appium Server不存在如下参数名："+name, true);
			} catch (SecurityException e) {
				ErrorHandler.continueRunning(e, "初始化AppiumServer 参数异常!", false);
			} catch (IllegalArgumentException e) {
				ErrorHandler.stopRunning(e, "AppiumServer 以下参数，参数值类型不正确："+name, true);
			} catch (IllegalAccessException e) {
				ErrorHandler.continueRunning(e, "初始化AppiumServer 参数异常!", false);
			}
		   
		   }
	   }
   }
  
   //构造启动Appium服务的CMD命令
   private String commandBuilder(){
	  StringBuffer cmds = new StringBuffer();
	  cmds.append("cmd /c start appium.cmd");
	  cmds.append(String.format(" --port=%d", appiumPort));
	  cmds.append(String.format(" --chromedriver-port=%d", chromeDriverPort));
	  cmds.append(String.format(" --selendroid-port=%d", selendroidPort));
	  cmds.append(String.format(" --bootstrap-port=%d", bootstrapPort));
	  cmds.append(" --session-override");
	  if(!udid.equals("-1")){
		  cmds.append(String.format(" -U=%s", udid));
	  }  
	  return cmds.toString();	 
   }

}
