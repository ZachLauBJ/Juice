package test;

import java.util.Map;
import org.juice.testbase.WebSuite;
import org.testng.annotations.Test;

public class WebCase001 extends WebSuite{

	@Test(dataProvider="getData")
	public void webCase001(Map<String,String> data){
		locator.linkToPage(data.get("url"));
		locator.element("首页", "目的地").clear();
		locator.element("首页", "目的地").sendKeys(data.get("目的地"));
		locator.element("首页", "关键词").clear();
		locator.element("首页", "关键词").sendKeys(data.get("关键词"));
		locator.element("首页", "搜索").click();
		String totalNumber = locator.element("酒店搜索结果页", "结果总数").getAttribute("Text");
		String serchInfo = "目的地："+data.get("目的地")+" 关键词："+data.get("关键词");
		checkPoint.equals(totalNumber, "0", serchInfo+"无搜索结果");		
		checkPoint.result(serchInfo+" 共搜索到  "+totalNumber+" 个结果");		
	}	
}
