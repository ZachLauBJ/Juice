package org.juice;

import org.openqa.selenium.remote.DesiredCapabilities;

public class Parameter extends DesiredCapabilities{
	
	public void set(String name, String value){
		setCapability(name, value);
	}
	
	public void set(String name, boolean value){
		setCapability(name, value);
	}
	
	public void set(String name, Object value){
		setCapability(name, value);
	}
	
	public String get(String name){
		Object cap = getCapability(name);	
		if(cap==null){
			return null;
		}else return cap.toString();	
	}
	
	public boolean isExist(String name){
		Object cap = getCapability(name);
	    if (cap == null) {
	        return false;
	    }else {
	    	return true;  
	    }		
	}
}
