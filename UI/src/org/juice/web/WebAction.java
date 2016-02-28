package org.juice.web;

import java.util.List;
import org.juice.Log;
import org.juice.util.HttpHelper;
import org.openqa.selenium.WebElement;

public class WebAction {

	private WebLocator locator;
	
	public WebAction(WebLocator locator){
		this.locator = locator;
	}
	
	public boolean checkIamgeLinks(String pageKey,String objKey){
		int failedNumber=0;
		Boolean flag =null;
		List<WebElement> imageList = locator.elements(pageKey, objKey);
		for(WebElement e : imageList){
			if(!e.isDisplayed()){
				Log.error("ͼƬδ��ʾ");
				failedNumber = failedNumber + 1;
			}
		}		
		List<String> imageSrc = locator.elementsAttribute(pageKey, objKey, "src");
		flag = HttpHelper.URLisAvailable(imageSrc);		
		if(flag && failedNumber==0 ){
			return true;
		}else return false;
	}
	
}
