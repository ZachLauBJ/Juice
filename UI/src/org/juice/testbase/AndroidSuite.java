package org.juice.testbase;

import io.appium.java_client.AppiumDriver;

import java.util.HashMap;
import java.util.Map;

import org.juice.CheckPoint;
import org.juice.DriverFactory;
import org.juice.DriverType;
import org.juice.Parameter;
import org.juice.app.AppAction;
import org.juice.app.AppConfig;
import org.juice.app.AppLocator;
import org.juice.app.AppiumServer;
import org.juice.app.AppiumServerType;
import org.juice.util.AdbExecutor;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;


public class AndroidSuite extends TestBase implements AppiumServerType, DriverType{
	
	public static Map<String,AppiumDriver> androidDriverMap = new HashMap<String,AppiumDriver>();
	public static Map<String,AppiumServer> appiumServerMap = new HashMap<String,AppiumServer>();
	protected AppLocator locator;
	protected AppAction action;
	protected CheckPoint checkPoint;
	
	@BeforeTest
	@Parameters({"udid","platformVersion"})
	public synchronized void beforeTest(String udid,String platformVersion){
		Parameter ServerParameters = new Parameter();
		Parameter driverParameters = new Parameter();
		
		ServerParameters.set(DEVICE_ID, udid);
		AppiumServer server = new AppiumServer(ServerParameters);
		server.startService();
		
		driverParameters.set(TYPE, AppConfig.platformName);
		driverParameters.set(APPIUM_SERVER_URL,server.getURL());
		driverParameters.set(DEVICE_NAME, udid);
		driverParameters.set(UDID, udid);
		driverParameters.set(PLATFORM_VERSION, platformVersion);
		driverParameters.set(APP_PATH, AppConfig.appPath);
		driverParameters.set(APP_PACKAGE, AppConfig.packageName);
		driverParameters.set(APP_ACTIVITY, AppConfig.activityName);
		driverParameters.set(NO_RESET, AppConfig.noReset);
		driverParameters.set(FULL_RESET, AppConfig.fullReset);
		driverParameters.set(UNICODE_KEYBOARD, AppConfig.unicodeKeyboard);
		driverParameters.set(DEVICE_READY_TIMEOUT, AppConfig.deviceReadyTimeout);
		driverParameters.set(OBJECT_TIMEOUT,AppConfig.objectWaitTime);
		AppiumDriver driver = (AppiumDriver)DriverFactory.createDriver(driverParameters);
		
		appiumServerMap.put(udid, server);
		androidDriverMap.put(udid,driver);
	}
	
	@BeforeClass
	@Parameters({"deviceName","udid"})
	public synchronized void beforeCase(String deviceName,String udid){
		locator = new AppLocator(androidDriverMap.get(udid), AppConfig.objectRespository, AppConfig.objectWaitTime);
		action = new AppAction(locator);
		action.skipToMain(3);
		checkPoint = new CheckPoint();
		AdbExecutor.startTheActivity(udid,AppConfig.packageName, AppConfig.startActivityName);
	}
	
	@AfterTest
	@Parameters({"udid"})
	public synchronized void afterTest(String udid) {
		androidDriverMap.get(udid).quit();
		appiumServerMap.get(udid).stopService();
		appiumServerMap.get(udid).closeLaunchedCmdWindow();
	}
}
