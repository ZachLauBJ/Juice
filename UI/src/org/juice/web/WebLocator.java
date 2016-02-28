package org.juice.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.juice.ErrorHandler;
import org.juice.Locator;
import org.juice.util.BaseDataHandler;
import org.juice.util.XmlParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;


public class WebLocator implements Locator{

	private WebDriver driver;
	private String filePath;
	private XmlParser xp;
	private boolean existFlag;
	private int objectWaitTime;
	
	public WebLocator(WebDriver driver, String filePath, int objectWaitTime){
		this.driver = driver;
		this.filePath = filePath;
		this.objectWaitTime = objectWaitTime;
		xp = new XmlParser(filePath);
		existFlag = true;
	}
	
	/**
	* @Description 根据对象xml，查找相应的单个页面元素，返回WebElement。默认最长等待时间在实例化Locator时已配置
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @see getLocator
	* @return WebElement 
	*/
	@Override
	public WebElement element(String pageKey, String objKey) {
		return getElement(pageKey, objKey, true, true);
	}

	/**
	 * 在元素定位时长内，等待页面某个元素是否已隐藏
	 * @param pageKey 对象xml中的页面名称
	 * @param objKey  对象xml中的对象名称
	 * @return boolean
	 */
	public boolean waitElementToBeNonDisplayed(String pageKey, String objKey) {
		final WebElement element = getElement(pageKey, objKey, false, true);
	    boolean wait = false;
	    if (element == null)
	        return wait;
	    try {
	        wait = new WebDriverWait(driver, objectWaitTime)
	                .until(new ExpectedCondition<Boolean>() {
	                    public Boolean apply(WebDriver d) {
	                        return !element.isDisplayed();
	                    }
	                });
	    } catch (Exception e) {
	    	String message = "等待"+objectWaitTime+"秒后，元素："+pageKey+"-"+objKey+" 依然显示!";
	    	ErrorHandler.continueRunning(e, message, true);
	    }
	    return wait;
	}
	
	/**
	* @Description 根据对象xml，查找相应的单个页面元素，返回WebElement。无等待时间
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @see getLocator
	* @return WebElement 
	*/
	@Override
	public WebElement elementNoWait(String pageKey, String objKey) {
		return getElement(pageKey, objKey, false, true);
	}

	/**
	* @Description 根据对象xml，查找同一个定位条件下的多个页面元素，返回WebElement列表
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @see getLocators
	* @return List<WebElement>
	*/
	@Override
	public List<WebElement> elements(String pageKey, String objKey) {
		return getElements(pageKey, objKey);
	}

	/** 
	* @Description 根据对象xml，查找同一个定位条件下的多个页面元素，返回列表中第一个对象
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @return WebElement 
	*/
	public WebElement theFirstElement(String pageKey,String objKey){
		List<WebElement> elements = getElements(pageKey,objKey);	
		return elements.get(0);
	}
	
	/** 
	* @Description 根据对象xml，查找同一个定位条件下的多个页面元素，返回列表中最后一个对象
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @return WebElement 
	*/
	public WebElement theLastElement(String pageKey,String objKey){
		List<WebElement> elements = getElements(pageKey,objKey);	
		return elements.get(elements.size()-1);
	}
	
	/** 
	* @Description 根据对象xml，查找同一个定位条件下的多个页面元素，返回列表中的一个随机对象
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @return WebElement 
	*/
	public WebElement theRandomElement(String pageKey,String objKey){
		List<WebElement> elements = getElements(pageKey,objKey);	
		int index = (int)(Math.random()*elements.size());
		return elements.get(index);
	}
	
	/**
	* @Description 根据对象xml，查找同一个定位条件下的多个页面元素的固定一个属性值，返回字符串数组
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @param attribute 要得到的属性名
	* @return List<String>
	*/
	@Override
	public List<String> elementsAttribute(String pageKey, String objKey,
			String attribute) {
		List<String> list= new ArrayList<String>();
		List<WebElement> elements = elements(pageKey, objKey);
		if(attribute.equalsIgnoreCase("text")){
			for(WebElement e:elements){		
				list.add(e.getText());				
			}
		}else{
			for(WebElement e:elements){		
				list.add(e.getAttribute(attribute));				
			}
		}		
		return list;
	}
	
	/**
	* @Description 根据对象xml，判断某个页面元素是否存在，返回布尔表达式
	* @param isWait  布尔型变量，是否需要等待
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @see getLocator
	* @return boolean
	*/
	@Override
	public boolean elementExist(boolean isWait, String pageKey, String objKey) {
		getElement(pageKey, objKey, isWait, false);
		return existFlag;
	}

	@SuppressWarnings("unused")
	public boolean elementExist(String xpathString){		
        try {
           WebElement element = driver.findElement(By.xpath(xpathString));
           existFlag = true;	 
        } catch (Exception e) {
           existFlag = false;
        }
        return existFlag;  
	}
	
	/**
	* @Description 实例化webdriver的原始Actions类
	*/
	@Override
	public Actions action() {
		return new Actions(driver);
	}
	
	/**
	* @Description 实例化webdriver的原始Select类
	*/
	@Override
	public Select select(String pageKey, String objKey) {
		return new Select(this.getElement(pageKey, objKey, true,true));
	}
	
	/**
	* @Description  操作下拉列表，通过下拉列表中的选项的value属性选中某项
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @param value
	* @return void
	*/
	public void selectByValue(String pageKey,String objKey,String value){
		Select select = select(pageKey, objKey);
		select.selectByValue(value);	
	}
	
	public void selectByValue(WebElement element,String value){
		Select select = new Select(element);
		select.selectByValue(value);	
	}
	
	/**
	* @Description  操作下拉列表，通过下拉列表中选项的索引选中某项
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @param index 索引值
	* @return void
	*/
	public void selectByIndex(String pageKey,String objKey,int index){
		Select select = select(pageKey, objKey);
		select.selectByIndex(index);		
	}
	
	public void selectByIndex(WebElement element,int index){
		Select select = new Select(element);
		select.selectByIndex(index);		
	}
	
	/**
	* @Description  操作下拉列表，通过下拉列表中选项的可见文本选中某项
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @param text 文本值
	* @return void
	*/
	public void selectByVisibleText(String pageKey,String objKey,String text){
		Select select = select(pageKey, objKey);
		select.selectByVisibleText(text);	
	}
	
	public void selectByVisibleText(WebElement element,String text){
		Select select = new Select(element);
		select.selectByVisibleText(text);	
	}
	
	/**
	* @Description  操作下拉列表，遍历下拉列表所有选项，用click进行选中选项
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @return void
	*/
	public void selectEveryOne(String pageKey,String objKey){
		Select select = select(pageKey, objKey);
		for(WebElement e : select.getOptions()){
			e.click();
			wait(1);
		}
	}
	
	@Override
	public void addJS(String jsCodes) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
		jsExecutor.executeScript(jsCodes);	
	}
	
	/**
	* @Description  通过引用JavaScript脚本，去除指定元素的read only属性。
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @return void
	*/
	public void removeReadOnly(String pageKey,String objKey){
    	if (xp.isExist("/对象/"+pageKey+"/"+objKey)) {	 
		String type = xp.getElementText("/对象/"+pageKey+"/"+objKey+"/type");
		String value = xp.getElementText("/对象/"+pageKey+"/"+objKey+"/value");	  
		JavascriptExecutor removeAttribute = (JavascriptExecutor)driver;
		
		//document中的getElementBy的属性值首字母都是大写，所以转换一下
		String js ="var js=document.getElementBy"+BaseDataHandler.capitalizesTheFirstLetter(type)
				+"(\""+value+"\");js.removeAttribute('readonly');";
		removeAttribute.executeScript(js);
    	}else{
    		String message = pageKey+"-"+objKey+"未在对象库文件中存在："+filePath;
			ErrorHandler.stopRunning(message, true);
    	}
	}
	
	public void removeReadOnly(String pageKey,String objKey,String index){
    	if (xp.isExist("/对象/"+pageKey+"/"+objKey)) {	 
		String type = xp.getElementText("/对象/"+pageKey+"/"+objKey+"/type");
		String value = xp.getElementText("/对象/"+pageKey+"/"+objKey+"/value");	  
		JavascriptExecutor removeAttribute = (JavascriptExecutor)driver;
		
		//document中的getElementBy的属性值首字母都是大写，所以转换一下
		String js ="var js=document.getElementBy"+BaseDataHandler.capitalizesTheFirstLetter(type)
				+"(\""+value+index+"\");js.removeAttribute('readonly');";
		removeAttribute.executeScript(js);
    	}else{
    		String message = pageKey+"-"+objKey+"未在对象库文件中存在："+filePath;
			ErrorHandler.stopRunning(message, true);
    	}
	}
	
	/**
	 * @Description 将多个页面元素的display='block'属性去除,从而强行显示该元素
	 * @param typeName 元素定位类型：支持classname,id,tagname
	 * @param typeValue
	 */
	public void elementsToBeDisplayed(String typeName,String typeValue){
		StringBuffer sb = new StringBuffer();
		typeName = typeName.toLowerCase();	

		switch(typeName){
		 case "classname": 
			sb.append("var all = document.getElementsByClassName('"+typeValue+"'); ");
			break;
		 case "id": 
			sb.append("var all = document.getElementsById('"+typeValue+"'); ");
			break;
		 case "tagname": 
			sb.append("var all = document.getElementsByTagName('"+typeValue+"'); ");
			break;
		 default: 
			ErrorHandler.stopRunning("document对象的getElements方法不支持此查找类型："+typeName, true);
		}
		sb.append("for(var i=0;i<all.length;i++){all[i].style.display='block';}");
		addJS(sb.toString());
	}
	
	/**
	* @Description   跳转到指定链接，若页面超时未加载(扑捉到TimeoutException)，抛出异常，记录日志和测试报告
	* @param Url 链接地址字符串
	* @return boolean 页面已成功跳转返回true
	*/
	@Override
	public boolean linkToPage(String Url) {
		try {
			driver.get(Url);
		} catch (TimeoutException e) {
			String message = "页面加载失败:"+Url;
			ErrorHandler.continueRunning(e, message, true);
			return false;
		}
		return true;
	}

	/**
	* @Description 遍历指定url，跳转后获取页面指定元素的属性值。返回字符串数组
	* @param url 需要遍历的url数组
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @param attribute  要获取的元素属性类型
	* @return List<String>
	*/
	public  List<String>  linkedAndGet(List<String> url, String pageKey, 
			String objKey, String attribute){	
		List<String> texts = new ArrayList<String>();
		int size = url.size();
			for(int i=0;i<size;i++){
				boolean isLinked = linkToPage(url.get(i));
				if(isLinked){
					if(attribute.equalsIgnoreCase("text")){
						texts.add(element(pageKey,objKey).getText().trim());
					}else texts.add(element(pageKey,objKey).getAttribute(attribute));
				}else texts.add("-1");			
			}	
		return texts;		
	}

	/**
	* @Description 遍历指定url，跳转后检查页面某个指定元素是否存在。返回布尔表达式
	* @param url 需要遍历的url数组
	* @param pageKey 对象xml中的页面名称
	* @param objKey  对象xml中的对象名称
	* @return boolean
	*/
	public boolean linkedAndCheck(List<String> Url, String pageKey,
			String objKey) {
		boolean existFlag = false;
		for(String url:Url){
			boolean isLinked = linkToPage(url);
			if(isLinked){
				waitForPageLoad();
				if(!elementExist(false, pageKey, objKey)){
					ErrorHandler.continueRunning("页面:"+url+" 元素  "+pageKey+"-"+objKey+" 不存在!", true);
					existFlag = false;
				}else existFlag = true;
			}
		}
		return existFlag;
	}
	
	/**
	* @Description 等待页面加载完成。最长等待时间实例化Locator时已配置
	* @return void 
	*/
	@Override
	public void waitForPageLoad() {		
		try {			
			WebDriverWait wait = new WebDriverWait(driver, objectWaitTime);
			wait.until(isPageLoaded());	        
	    }catch(Exception e){	 
	    	String message = "页面："+driver.getCurrentUrl()+"未能在"+objectWaitTime+"秒内全部加载完成!";
	    	ErrorHandler.continueRunning(e, message, true);
	    }	
	}

	/**
	* @Description 固定时间等待
	* @param time固定等待的时长，单位为：秒
	* @return void 
	*/
	@Override
	public void wait(int seconds) {
		try {
			int millis = seconds*1000;
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			ErrorHandler.continueRunning(e, "当前线程正处于等待状态!", false);
		}
	}
	
	//	根据从对象xml读取的type和value值，组合成find element 所需参数值。返回By对象
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
		default :
			ErrorHandler.stopRunning("元素定位错误! By "+type+"不存在此类型 ", false);
		}
		return by;	 
	}
		
	//	在config.xml配置的对象最长等待时间内，定位指定by的元素
	//	定位成功，existFlag置为true。否则抛出Exception，打印错误日志
	protected WebElement waitForElement(final By by,boolean isReport,String pageKey,String objKey) {
		WebElement element = null;   
		try {
			element = new WebDriverWait(driver, objectWaitTime)
			 .until(new ExpectedCondition<WebElement>() {
				 @Override
			     public WebElement apply(WebDriver d) {
			     	existFlag = true;
			        return d.findElement(by);
			     }
			 });
		} catch (Exception e) {
			existFlag = false;
			String message = pageKey+"-"+objKey+" 等待"+objectWaitTime+"秒后未出现!";
			ErrorHandler.stopRunning(e,message,isReport);
		}
		return element;		
	}
	
	//	在config.xml配置的对象最长等待时间内，判断页面元素是否可见
	//	元素可见，返回值为true。否则抛出Exception，打印错误日志	
	protected boolean waitElementToBeDisplayed(final WebElement element, 
			String pageKey, String objKey) {
	    boolean wait = false;
	    if (element == null)
	        return wait;
	    try {
	        wait = new WebDriverWait(driver, objectWaitTime)
	                .until(new ExpectedCondition<Boolean>() {
	                    public Boolean apply(WebDriver d) {
	                        return element.isDisplayed();
	                    }
	                });
	    } catch (Exception e) {
	    	String message = pageKey + "-"+objKey + " 未在页面显示!";
	    	ErrorHandler.continueRunning(e, message, true);
	    }
	    return wait;
	}
	
	/**
	* @Description 单个页面元素查找引擎
	* @param isWait 是否需要等待
	* @param isReport  是否需要打印错误报告
	* @return WebElement 
	*/
	protected WebElement getElement(String pageKey, String objKey, 
			 boolean isWait, boolean isReport){
		WebElement element = null;
		if(xp.isExist("/object/"+pageKey+"/"+objKey)){
			String type = xp.getElementText("/object/"+pageKey+"/"+objKey+"/type");
			String value = xp.getElementText("/object/"+pageKey+"/"+objKey+"/value");
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
					 String message = "未查找到页面元素："+pageKey+"-"+objKey;
		             ErrorHandler.continueRunning(e, message, isReport);
				}	                           
			}
		}else {
			String message = pageKey+"-"+objKey+"未在对象库文件中存在："+filePath;
			ErrorHandler.stopRunning(message, true);
		}		
		return element;		
	}
	
	/**
	* @Description 多个页面元素查找引擎，返回WebElement列表。当有元素定位失败时，停止运行
	* @return List<WebElement> 
	*/
	protected List<WebElement> getElements(String pageKey, String objKey){
		List <WebElement> elements = new ArrayList<WebElement>();
		if(xp.isExist("/object/"+pageKey+"/"+objKey)){
			String type = xp.getElementText("/object/"+pageKey+"/"+objKey+"/type");
			String value = xp.getElementText("/object/"+pageKey+"/"+objKey+"/value");
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
	
	//waitForPageLoad方法的具体实现函数
	protected Function<WebDriver, Boolean> isPageLoaded() {
        return new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
	}

	@Override
	public WebDriver getDriver() {
		return driver;
	}

	@Override
	public void takeScreenshot(String screenPath) {
		try{
			File srcFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(screenPath));
		}catch(IOException e){
			ErrorHandler.continueRunning(e, "截图异常", false);
		}
	}

}
