package org.juice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.juice.ErrorHandler;


public class AdbExecutor {
	
	public static final String AdbPath = "adb shell ";       //"D:/android-sdk/platform-tools/adb";
	
	private static Process Execute(String cmd){	
		Process adbProcess = null;	
		try {
			adbProcess = Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {	
			ErrorHandler.continueRunning(e, "ADB执行器进程异常!", false);
		}
		return adbProcess;
	}
	
	private static String getOutput(Process process){		
		BufferedReader  br = null;
		String line = null;
		StringBuilder output = new StringBuilder();	
		br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		try {
			while ((line = br.readLine())!=null) {
				output.append(line+"\n");
			}
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "ADB执行器进程异常!", false);
		}
		 
		return output.toString();
	}
	
	

	public static String getTheFocusedActivity(String udid){
		String cmd = "adb -s "+udid+" shell dumpsys activity | grep Focuse";	
		return getOutput(Execute(cmd));	
	}
	

	public static String getDevicesInfo(){
		String cmd = "cmd /c adb devices";
		return getOutput(Execute(cmd));
	}
	

	public static boolean  isPackageInstalled(String udid,String packageName){
		boolean flag = false; 
		String cmd = "adb -s "+udid+" shell pm list package";
		String output = getOutput(Execute(cmd));
		if(output.contains(packageName)){
			flag = true;
		}	
		return flag;			
	}
	
	public static boolean  isPackageInstalled(String packageName){
		boolean flag = false; 
		String cmd = AdbPath+" pm list package";
		String output = getOutput(Execute(cmd));
		if(output.contains(packageName)){
			flag = true;
		}	
		return flag;			
	}
	

	public static void startTheActivity(String udid,String packageName,String activityName){
		String cmd = "adb -s "+udid+" shell am start -n "+packageName+"/"+packageName+"."+activityName;	
		Execute(cmd);	
	}
	
	public static void startTheActivity(String packageName,String activityName){
		String cmd = AdbPath+" am start -n "+packageName+"/"+packageName+"."+activityName;	
		Execute(cmd);	
	}
	

	public static void closeTheAPP(String udid,String packageName){
		String cmd = "adb -s "+udid+" shell am force-stop "+packageName;	
		Execute(cmd);	
	}
	

	public static void clearAndroidCache(String udid,String packagename){
		String cmd = "adb -s "+udid+" shell pm clear "+packagename;
		Execute(cmd);
	}
	

	public static void closeCmdWindows(){
		String cmd = "taskkill /f /im cmd.exe";
		Execute(cmd);
	}
}
