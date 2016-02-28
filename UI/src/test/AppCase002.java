package test;

import io.appium.java_client.android.AndroidKeyCode;

import java.util.List;

import org.juice.testbase.AndroidSuite;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class AppCase002 extends AndroidSuite{

	@Test		
	public void case002(){
	
	List<WebElement> links = locator.elements("首页", "频道链接");
	String title = "";
	
		links.get(0).click();
		title = locator.element("首页","频道页主标题").getText().trim();
		checkPoint.equals(title, "出境旅游", "出境游跳转错误！");
		locator.sendKeyEvent(AndroidKeyCode.BACK);
		locator.wait(1);

		links.get(1).click();
		title = locator.element("首页","频道页主标题").getText().trim();
		checkPoint.equals(title, "国内旅游", "国内游跳转错误！");
		locator.sendKeyEvent(AndroidKeyCode.BACK);
		locator.wait(1);

		links.get(3).click();
		title = locator.element("首页","频道页主标题").getText().trim();
		checkPoint.equals(title, "抢游惠", "抢优惠跳转错误！");
		locator.sendKeyEvent(AndroidKeyCode.BACK);
		locator.wait(1);

		checkPoint.result("首页：出境游，国内游，抢优惠链接跳转正常！");
	}	
}
