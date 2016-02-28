package org.juice.util;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlParser {
	
	String filePath;
	Document document;

	public XmlParser(String filePath){
		this.filePath=filePath;
		this.Load(filePath);
	}
	
	private void Load(String filePath){
		File file=new File(filePath);
		if(file.exists()){
			SAXReader saxReader = new SAXReader();
			try{
				document=saxReader.read(file);
			}catch(DocumentException e){

			}
		}else {

		}
	}
	
	public Element getElementObject(String elementPath){
		return (Element) document.selectSingleNode(elementPath);
	}
	
	public boolean isExist(String elementPath){
		boolean flag=false;
		Element element = this.getElementObject(elementPath);
		if(element!=null)
			flag=true;
		return flag;
	}

	public String getElementText(String elementPath){
		Element element=this.getElementObject(elementPath);
		if(element!=null)
			return element.getTextTrim();
		else 
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Element> getElementObjects(String elementPath){
		return document.selectNodes(elementPath);
	}
	
	public Map<String,String> getChildrenInfo(String elementPath,String key,String key2){
		List<Element> elements = this.getElementObjects(elementPath);
		Map<String,String> map = new LinkedHashMap<String,String>();
		for(Element e : elements){
			map.put(e.element(key).getTextTrim(), e.element(key2).getTextTrim());
		}
		return map;
	}
	
	public Map<String,String> getChildrenInfo(Element element){
		@SuppressWarnings("unchecked")
		List<Element> childrens = element.elements();
		Map<String,String> map = new LinkedHashMap<String,String>();	
		for(Element e:childrens){
			map.put(e.getName(), e.getTextTrim());
		}
		return map;	
	}

	public Map<String,String> getChildrenInfo(String elementPath){
		@SuppressWarnings("unchecked")
		List<Element> elements = this.getElementObject(elementPath).elements();
		Map<String,String> map = new LinkedHashMap<String,String>();
		
		for(Element e:elements){
			map.put(e.getName(), e.getTextTrim());
		}
		return map;	
	}

}
