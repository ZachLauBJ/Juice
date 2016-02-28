package org.juice.testbase;

import org.juice.CheckPoint;
import org.juice.DriverFactory;
import org.juice.DriverType;
import org.juice.Locator;
import org.juice.Parameter;
import org.juice.web.WebConfig;
import org.juice.web.WebLocator;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


public class WebSingle extends TestBase implements DriverType{

	protected Locator locator;
	protected CheckPoint checkPoint;
	protected WebDriver driver;
		
	@BeforeClass
	public synchronized void beforeTest(){
		updateRunningInfo("Debug");
		Parameter driverParameters = new Parameter();
		driverParameters.set(TYPE, WebConfig.debugBrowser);
		driverParameters.set(BROWSER_WAIT_TIME, WebConfig.browserWaitTime);
		driver = DriverFactory.createDriver(driverParameters);
		checkPoint = new CheckPoint();
		locator = new WebLocator(driver, WebConfig.objectRespository, WebConfig.objectWaitTime);
	}
	
	@AfterClass
	public void afterTest(){
		driver.close();
		driver.quit();
	}
	
}
