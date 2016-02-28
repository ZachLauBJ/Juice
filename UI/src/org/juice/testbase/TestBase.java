package org.juice.testbase;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;
import org.juice.util.BaseDataHandler;
import org.juice.util.XmlParser;
import org.testng.annotations.DataProvider;



public class TestBase {

	private String filePath = "test-data/"+this.getClass().getSimpleName()+".xml";
	private XmlParser xp;
	private Map<String,String> commonMap;
	private static Map<String,String> global;
	protected static Map<String, String> globalRunningInfo;
	
	static{
		XmlParser g_xp = new  XmlParser("test-data/Global.xml");
		global = g_xp.getChildrenInfo(g_xp.getElementObject("/*"));
		globalRunningInfo = new HashMap<String, String>();
	}
	
	protected synchronized void updateRunningInfo (String testName){
		String key = Thread.currentThread().getId()+"+"+testName;
		String caseName = this.getClass().getSimpleName();
		globalRunningInfo.put(key, caseName);
	}
	
	public synchronized static Map<String, String> getRunningInfo(long threadId){
		String testName="";
		String caseName="";
		Map <String,String> runNameMap = new HashMap<String,String>();
		for(String key : globalRunningInfo.keySet()){
			if(key.startsWith(Thread.currentThread().getId()+"+")){
				testName = BaseDataHandler.subTextString(key, "+");
				caseName = globalRunningInfo.get(key);
			}
		}
		runNameMap.put("testName", testName);
		runNameMap.put("caseName", caseName);
		return runNameMap;		
	}
	
	@DataProvider
	public Object[][] getData(Method method){
		this.init();
		this.getCommonData();
		File file = new File(filePath);
		List<Element> elements = xp.getElementObjects("data/"+method.getName());
		if(file.exists() && elements.size()>0){
			Object[][] object = new Object[elements.size()][];
			for(int i=0;i<elements.size();i++){
				Map<String,String> mergeCommon = this.getMergeData(
						xp.getChildrenInfo(elements.get(i)), commonMap);
				Map<String,String> mergeGlobal = this.getMergeData(mergeCommon, global);
				Object[] temp = new Object[]{mergeGlobal};
				object[i] = temp;
			}
			return object;	
		}else{
			Object[][] object = new Object[1][1];
			object[0][0] = global;
			return object;	
		}
	}
	
	private void init(){
		if(xp == null){
			xp = new XmlParser(filePath);
		}
	}
	 	 
	private void getCommonData(){
		if(commonMap == null){
			Element element = xp.getElementObject("/*/common");
			commonMap = xp.getChildrenInfo(element); 
		}		
	}
	 
	private Map<String,String> getMergeData(Map<String,String>map1,Map<String,String>map2){
		Iterator<String> it = map2.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = map2.get(key);
				if(!map1.containsKey(key)){
					map1.put(key, value);			
				}
		}
		return map1;
	}
	
}
