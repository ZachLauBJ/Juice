package org.juice.app;

import org.juice.util.XmlParser;


public class AppConfig {

	public static String automationName;
	public static String platformName;
	public static long appiumServerWaitTime;
	public static String appPath;
	public static String newCommandTimeout;
	public static Boolean noReset;
	public static Boolean fullReset;
	public static String packageName;
	public static String activityName;
	public static String startActivityName;
	public static String deviceReadyTimeout;
	public static Boolean unicodeKeyboard;
	public static int objectWaitTime;
	public static String objectRespository;
	public static int retryTimes;
	public static String SMTPserver;
	public static String from;
	public static String username;
	public static String password ;
	public static String to;
	public static String copyTo;
	public static String filename;
	public static String subject;
	
	static{		
		XmlParser appXp = new XmlParser("config/appConfig.xml");
		automationName = appXp.getElementText("/config/general/automationName");
		platformName = appXp.getElementText("/config/general/platformName");
		appiumServerWaitTime = Integer.parseInt(appXp.getElementText("/config/general/appiumServerWaitTime"));
		appPath = appXp.getElementText("/config/general/appPath");
		newCommandTimeout = appXp.getElementText("/config/general/newCommandTimeout");
		noReset = Boolean.valueOf(appXp.getElementText("/config/general/noReset"));
		fullReset = Boolean.valueOf(appXp.getElementText("/config/general/fullReset"));
		packageName = appXp.getElementText("/config/android/packageName");
		activityName = appXp.getElementText("/config/android/activityName");
		startActivityName = appXp.getElementText("/config/android/startActivityName");
		deviceReadyTimeout = appXp.getElementText("/config/android/deviceReadyTimeout");
		unicodeKeyboard = Boolean.valueOf(appXp.getElementText("/config/android/unicodeKeyboard"));	
		objectWaitTime = Integer.valueOf(appXp.getElementText("/config/objectWaitTime"));
		objectRespository = appXp.getElementText("/config/objectRespository");	
		retryTimes = Integer.valueOf(appXp.getElementText("/config/retryTimes"));
		SMTPserver = appXp.getElementText("/config/mail/SMTPserver");
		from = appXp.getElementText("/config/mail/from");
		username = appXp.getElementText("/config/mail/username");
		password = appXp.getElementText("/config/mail/password");
		to = appXp.getElementText("/config/mail/to");
		copyTo = appXp.getElementText("/config/mail/copyTo");
		filename = appXp.getElementText("/config/mail/filename");
		subject = appXp.getElementText("/config/mail/subject"); 
	}
}
