package org.juice;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public interface Locator {

	WebElement element(String pageKey,String objKey);
	
	WebElement elementNoWait(String pageKey,String objKey);
	
	List<WebElement> elements(String pageKey,String objKey);
		
	List<String> elementsAttribute(String pageKey,String objKey,String attribute);
	
	boolean elementExist(boolean isWait,String pageKey,String objKey);
	
	Actions action();
	
	Select select(String pageKey,String objKey);
	
	void addJS(String jsCodes);
	
	boolean linkToPage(String Url);
		
	void waitForPageLoad();
	
	void wait(int times);
	
	void takeScreenshot(String imagePath);
	
	WebDriver getDriver();
	
}
