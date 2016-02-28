package org.juice.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.juice.ErrorHandler;
import org.juice.util.BaseDataHandler;
import org.juice.util.XmlParser;
import org.juice.web.WebLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


public class AppLocator extends WebLocator{

	private AppiumDriver driver;
	private String filePath;
	private XmlParser xp;
	private boolean existFlag;
	private int objectWaitTime;
	
	public AppLocator(AppiumDriver driver, String filePath, int objectWaitTime) {		
		super(driver, filePath, objectWaitTime);		
		this.driver = driver;
		this.filePath = filePath;
		this.objectWaitTime = objectWaitTime;
		xp = new XmlParser(filePath);
		existFlag = true;
	}

	@Override
	public WebElement element(String pageKey, String objKey) {
		return getElement(pageKey, objKey, true, true);
	}
	
	@Override
	public List<WebElement> elements(String pageKey, String objKey) {
		return getElements(pageKey, objKey);
	}
	
	@Override
	public WebElement elementNoWait(String pageKey, String objKey) {
		return getElement(pageKey, objKey, false, true);
	}
	
	@Override
	public boolean elementExist(boolean isWait, String pageKey, String objKey) {
		getElement(pageKey, objKey, isWait, false);
		return existFlag;
	}
	
	/**
	 * 模拟键盘输出操作
	 * @param key:键值
	 */
	public void sendKeyEvent(int key){
		try {
			driver.sendKeyEvent(key);
		} catch (Exception e) {
			ErrorHandler.continueRunning(e, "模拟键盘输入异常!", false);
		}
	}
	
	/**
	 * 屏幕滑动操作
	 * @param indicator:滑动方向,可传入的参数值：left,right,up,down
	 */
	public void swipeActivity(String indicator){
		Dimension dimension = driver.manage().window().getSize();
		int height = dimension.getHeight();
		int width = dimension.getWidth();
		indicator = indicator.toLowerCase().trim();
		
		switch(indicator){
			case "left":
				driver.swipe(width-20, height/2, 20, height/2, 300);
				break;	
			case "right":
				driver.swipe(20, height/2, width-20, height/2, 300);
				break;		
			case "up":
				driver.swipe(width/2, height-20, width/2, 20, 300);
				break;	
			case "down":
				driver.swipe(width/2, 20, width/2, height-20, 300);
				break;
			default:ErrorHandler.continueRunning("屏幕滑动操作,入参类型错误!", true);
		}
	}
	
	@Override
	protected By getBy(String type ,String value){
		By by = null;
		switch(type){
		case "id":
			by = By.id(value);
			break;
		case "name":
			by = By.name(value);
			break;
		case "className":
			by = By.className(value);	
			break;
		case "tagName":
			by = By.tagName(value);	
			break;
		case "linkText":
			by = By.linkText(value);
			break;
		case "partialLinkText":
			by = By.partialLinkText(value);
			break;
		case "xpath":
			by = By.xpath(value);
			break;	
		case "cssSelector":
			by = By.cssSelector(value);
			break; 
		case "accessibilityId":
			by = By.id("name=accessibilityId"+" value="+value);
			break;
		case "textName":
			by = By.id("name=textName"+" value="+value);
			break;	
		default :
			ErrorHandler.stopRunning("元素定位错误! By "+type+"不存在此类型 ", false);
		}
		return by;
	}
	
	@Override
	protected WebElement waitForElement(final By by,boolean isReport,String pageKey,String objKey){
		WebElement element = null;   
		try {
			element = new WebDriverWait(driver, objectWaitTime)
			 .until(new ExpectedCondition<WebElement>() {
				 @Override
			     public WebElement apply(WebDriver d) {
			     	existFlag = true;			     	
			     	if(by.toString().contains("name=accessibilityId")){
			     		String value = BaseDataHandler.subTextString(by.toString(), "value=");
			     		return driver.findElementByAccessibilityId(value);
			     	}else if(by.toString().contains("name=textName")){
			     		String value = BaseDataHandler.subTextString(by.toString(), "value=");
			     		return driver.findElementByClassName(value);
			     	}else{
			     		 return d.findElement(by);
			     	}
			     }
			 });
		} catch (Exception e) {
			existFlag = false;
			String message = pageKey+"-"+objKey+" 等待"+objectWaitTime+"秒后未出现!";
			ErrorHandler.stopRunning(e,message,isReport);
		}
		return element;			
	}
	
	@Override
	protected WebElement getElement(String pageKey, String objKey, 
			 boolean isWait, boolean isReport){
		WebElement element = null;
		if(xp.isExist("/object/"+pageKey+"/"+objKey)){
			String type = xp.getElementText("/object/"+pageKey+"/"+objKey+"/type");
			String value = xp.getElementText("/object/"+pageKey+"/"+objKey+"/value");
    		//如果定位类型为：name或accessibilityId，就不再使用拼接By对象定位元素
    		if("name".equals(type) || "accessibilityId".equals(type)){
    			try {
    				if("name".equals(type)){
    					element = driver.findElementByName(value);
    		        }else if("accessibilityId".equals(type)){
    		            element = driver.findElementByAccessibilityId(value);        		            		
    		        }
    		        existFlag = true;      		                    
    			 } catch (Exception e) {
    				 element = null;
    				 String message = "未定位到页面元素："+pageKey+"-"+objKey;
    				 ErrorHandler.stopRunning(e, message, isReport);
    		         existFlag = false;
    		     }
    			return element;
    		}
    		
			By by = getBy(type, value);
			if(isWait){
				//等待元素在页面存在，若始终未存在则抛出异常
				element = waitForElement(by, isReport, pageKey, objKey);
				//等待元素为可见状态,若始终未可见则输出日志后继续操作
				waitElementToBeDisplayed(element,pageKey,objKey);
				try {
					element = driver.findElement(by);
			        existFlag = true;		//提供elementExist方法使用  
				} catch (Exception e) {
					existFlag = false;
					String message = "未定位到页面元素："+pageKey+"-"+objKey;
			        ErrorHandler.continueRunning(e, message, isReport);
				}	                           
			}
		}else {
			String message = pageKey+"-"+objKey+"未在对象库文件中存在："+filePath;
			ErrorHandler.stopRunning(message, true);
		}		
		return element;			
	}
	
	@Override
	protected List<WebElement> getElements(String pageKey, String objKey){
		List <WebElement> elements = new ArrayList<WebElement>();
		if(xp.isExist("/object/"+pageKey+"/"+objKey)){
			String type = xp.getElementText("/object/"+pageKey+"/"+objKey+"/type");
			String value = xp.getElementText("/object/"+pageKey+"/"+objKey+"/value");		
    		if("name".equals(type) || "accessibilityId".equals(type)){            	
            	if("name".equals(type)){
            		elements = driver.findElementsByName(value);
            	}else if("accessibilityId".equals(type)){
            		elements = driver.findElementsByAccessibilityId(value);        		            		
            	}     		                    
                if(elements.size()==0){
                	String message = "未查找到页面元素："+pageKey+"-"+objKey;
    				ErrorHandler.stopRunning(message, true);
                }
                for(WebElement e:elements){
                	if(e.equals("")||e==null){
                		String message = pageKey+"-"+objKey+" 返回多个对象，其中存在空值";
        				ErrorHandler.stopRunning(message, true);
                	}
                }        	   	
                return elements;
    		}

			By by = getBy(type, value);
			elements = driver.findElements(by);   //findElements不会抛出异常，因此下方需要对element做检查
			if(elements.size()==0){
				String message = "未查找到页面元素："+pageKey+"-"+objKey;
				ErrorHandler.stopRunning(message, true);
			}
			for(WebElement e:elements){
				if(e.equals("")||e==null){
					String message = pageKey+"-"+objKey+" 返回多个对象，其中存在空值";
					ErrorHandler.stopRunning(message, true);
				}
			}
		}else{
			String message = pageKey+"-"+objKey+"未在对象库文件中存在："+filePath;
			ErrorHandler.stopRunning(message, true);
		}
		return elements;		
	}

	public AppiumDriver getDriver(){
		return driver;
	}
	
	@Override
	public void takeScreenshot(String screenPath) {
		try{		
			File srcFile = driver.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(screenPath));
		}catch(IOException e){
			ErrorHandler.continueRunning(e, "截图异常", false);
		}
	}
}
