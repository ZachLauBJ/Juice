package org.juice.testbase;

import io.appium.java_client.AppiumDriver;

import org.juice.CheckPoint;
import org.juice.DriverFactory;
import org.juice.DriverType;
import org.juice.Parameter;
import org.juice.app.AppAction;
import org.juice.app.AppConfig;
import org.juice.app.AppLocator;
import org.juice.app.AppiumServer;
import org.juice.util.AdbExecutor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class AndroidSingle extends TestBase implements DriverType{

	protected static AppiumServer server;
	protected static AppiumDriver driver;
	protected AppLocator locator;
	protected AppAction action;
	protected CheckPoint checkPoint;
	
	@BeforeClass
	public void setup(){
		Parameter driverParameters = new Parameter();
		server = new AppiumServer();
		server.startService();
		driverParameters.set(TYPE, AppConfig.platformName);
		driverParameters.set(APP_PATH, AppConfig.appPath);
		driverParameters.set(APP_PACKAGE,AppConfig.packageName);
		driverParameters.set(APP_ACTIVITY, AppConfig.activityName);
		driverParameters.set(NO_RESET, AppConfig.noReset);
		driverParameters.set(FULL_RESET, AppConfig.fullReset);
		driverParameters.set(UNICODE_KEYBOARD, AppConfig.unicodeKeyboard);
		driverParameters.set(DEVICE_READY_TIMEOUT, AppConfig.deviceReadyTimeout);
		driverParameters.set(OBJECT_TIMEOUT,AppConfig.objectRespository);
		driver = (AppiumDriver)DriverFactory.createDriver(driverParameters);
		locator = new AppLocator(driver, AppConfig.objectRespository, AppConfig.objectWaitTime);
		checkPoint = new CheckPoint();
		action = new AppAction(locator);
		action.skipToMain(3);
		AdbExecutor.startTheActivity(AppConfig.packageName, AppConfig.startActivityName);
	}
	
	@AfterClass
	public void setdown(){
		driver.quit();
		server.stopService();
		AdbExecutor.closeCmdWindows();
	}
	
}
