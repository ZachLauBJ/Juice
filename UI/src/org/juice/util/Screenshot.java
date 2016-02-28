package org.juice.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.juice.Locator;

public class Screenshot {
	
	private Locator locator;
	private String screenPath;
	
	public Screenshot(Locator locator){
		this.locator = locator;
		initScreenPath();
	}
	
	public Screenshot(Locator locator, String screenPath){
		this.screenPath = screenPath;
		this.locator = locator;
	}
	
	public void takeScreenshot(){
		locator.takeScreenshot(screenPath);
	}
	
	private void initScreenPath(){
	  	SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String mDateTime = formatter.format(new Date().getTime());
		String screenName = mDateTime+".bmp";
	    File dir=new File("test-output/snapshot");
	    if(!dir.exists()){
	    	dir.mkdirs();
	    }
	    screenPath = dir.getAbsolutePath() + "/" + screenName;
	}
	
}
