package org.juice.testbase;

import java.util.HashMap;
import java.util.Map;

import org.juice.CheckPoint;
import org.juice.DriverFactory;
import org.juice.DriverType;
import org.juice.Locator;
import org.juice.Parameter;
import org.juice.util.HostsFileHandler;
import org.juice.web.WebConfig;
import org.juice.web.WebLocator;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;


public class WebSuite extends TestBase implements DriverType{

	public static Map<String,WebDriver> driverMap = new HashMap<String,WebDriver>();
	protected Locator locator;
	protected CheckPoint checkPoint;
	
	@BeforeSuite
	public void beforeSuite(){
		HostsFileHandler.init();
	}
	
	@BeforeTest
	@Parameters({"browserType", "browserIndex", "testName"})
	public synchronized void beforeTest(String browserType, String browserIndex, String testName){		
		updateRunningInfo(testName);
		Parameter driverParameters = new Parameter();
		driverParameters.set(TYPE, browserType);
		driverParameters.set(BROWSER_WAIT_TIME, WebConfig.browserWaitTime);
		WebDriver driver = DriverFactory.createDriver(driverParameters);
		driverMap.put(browserIndex, driver);
		locator = new WebLocator(driver, WebConfig.objectRespository, WebConfig.objectWaitTime);
	}
	
	@BeforeClass
	@Parameters({"testName"})
	public void beforeClass(String testName){
		updateRunningInfo(testName);
		checkPoint = new CheckPoint();
	}
	
	@AfterTest
	@Parameters({"browserIndex"})
	public synchronized void afterTest(String browserIndex){
		driverMap.get(browserIndex).close();
		driverMap.get(browserIndex).quit();
	}
	
}
