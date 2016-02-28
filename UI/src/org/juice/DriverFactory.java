package org.juice;

import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.juice.util.AdbExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class DriverFactory implements DriverType{
	
	public static WebDriver createDriver(Parameter parameters){
		WebDriver driver = null;
		if(parameters.isExist(TYPE)){
			String type = parameters.get(TYPE).toLowerCase();		
			switch(type){
				case "firefox" : driver = newFirefox(parameters); break;
				case "ie" : driver = newIE(parameters); break;
				case "chrome" : driver = newChrome(parameters); break;
				case "android" : driver = newAndroid(parameters); break;
				default: ErrorHandler.stopRunning("Driver类型错误!", true);
			}
		}else {
			ErrorHandler.stopRunning("缺少Driver启动参数："+TYPE, true);
		}
		return driver;
	}
	
	private static WebDriver newFirefox(Parameter parameters){
		WebDriver driver = null;
		int browserWaitTime = 30;
		
		if(parameters.isExist(PROFILE_NAME)){
			FirefoxProfile fp = new FirefoxProfile();
			ProfilesIni allProfiles = new ProfilesIni();
			fp = allProfiles.getProfile(parameters.get(PROFILE_NAME));
			driver = new FirefoxDriver(fp);
			
		}else driver = new FirefoxDriver();
		
		if(parameters.isExist(BROWSER_WAIT_TIME)){
			browserWaitTime = Integer.parseInt(parameters.get(BROWSER_WAIT_TIME));
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(browserWaitTime, TimeUnit.SECONDS);
		return driver;	
	}
	
	private static WebDriver newIE(Parameter parameters){
		WebDriver driver = null;
		int browserWaitTime = 30;
		
		if(parameters.isExist(PLUG_IN_PATH)){
			System.setProperty("webdriver.ie.driver", parameters.get(PLUG_IN_PATH));
		}else {
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
		}
		
		if(parameters.isExist(BROWSER_WAIT_TIME)){
			browserWaitTime = Integer.parseInt(parameters.get(BROWSER_WAIT_TIME));
		}
		
		driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(browserWaitTime, TimeUnit.SECONDS);		
		return driver;
	}
	
	private static WebDriver newChrome(Parameter parameters){
		WebDriver driver = null;
		int browserWaitTime = 30;
		
		if(parameters.isExist(PLUG_IN_PATH)){
			System.setProperty("webdriver.chrome.driver", parameters.get(PLUG_IN_PATH));
		}else {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		}
		
		if(parameters.isExist(BROWSER_WAIT_TIME)){
			browserWaitTime = Integer.parseInt(parameters.get(BROWSER_WAIT_TIME));
		}
		
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(browserWaitTime, TimeUnit.SECONDS);
		return driver;	
	}
	
	private static WebDriver newAndroid(Parameter parameters){	
		WebDriver driver = null;
		int timeout = 20;
		boolean appAutomaticInstall = true;
		parameters.set(PLATFORM_NAME, "Android");
		
		if(!parameters.isExist(APP_PACKAGE) || !parameters.isExist(APP_ACTIVITY) || !parameters.isExist(UDID)){
			ErrorHandler.stopRunning("Android Driver缺少启动参数：APP_PACKAGE 或 APP_ACTIVITY 或 UDID", true);
		}
		
		try {
			URL url = new URL("http://127.0.0.1:4723/wd/hub");
			if(!parameters.isExist(DEVICE_NAME)){
				parameters.set(DEVICE_NAME, "device");
			}
	
			if(parameters.isExist(APPIUM_SERVER_URL)){
				url = new URL(parameters.get(APPIUM_SERVER_URL));
			}
			
			if(parameters.isExist(OBJECT_TIMEOUT)){
				timeout = Integer.parseInt(parameters.get(OBJECT_TIMEOUT));
			}
			
			if(parameters.isExist(APP_AUTOMATIC_INSTALL)){
				appAutomaticInstall = Boolean.parseBoolean(parameters.
											   get(APP_AUTOMATIC_INSTALL));
			}
			
			if(appAutomaticInstall){
				if(!AdbExecutor.isPackageInstalled(parameters.get(UDID),parameters.get(APP_PACKAGE))){
					if(parameters.isExist(APP_PATH)){
						File apk = new File(parameters.get(APP_PATH));
						if(parameters.get(APP_PATH).isEmpty()&&!apk.exists()){
							ErrorHandler.stopRunning("Android Driver启动参数错误：apk路径无效或文件不存在!", true);
						}else parameters.set(APP_PATH, parameters.get(APP_PATH));
						
					}else ErrorHandler.stopRunning("Android Driver缺少启动参数:APP_PATH", true);
				}
			}
			
			driver = new AndroidDriver(url,parameters);
			driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);
			
		} catch (Exception e) {
			ErrorHandler.stopRunning(e, "Android Driver启动失败:请查询详细日志信息!", true);
		}
		return driver;
	}
	
}
