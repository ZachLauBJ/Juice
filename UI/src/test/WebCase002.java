package test;

import java.util.List;
import java.util.Map;

import org.juice.testbase.WebSuite;
import org.testng.annotations.Test;


public class WebCase002 extends WebSuite{
	
	@Test(dataProvider="getData")
	public void case002(Map<String,String> data){
		locator.linkToPage(data.get("url"));
		List<String> productTitles = locator.elementsAttribute("首页", "精选产品", "title");
		checkPoint.equals(productTitles, "null", "首页  精选产品  产品标题为 null");
		checkPoint.equals(productTitles, "", "首页  精选产品  产品标题为 空");
		checkPoint.result("首页  精选产品  产品标题 正常");
	}
}
