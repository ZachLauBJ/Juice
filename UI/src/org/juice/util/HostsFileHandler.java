package org.juice.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.juice.ErrorHandler;
import org.juice.Log;
import org.juice.Reporter;


public class HostsFileHandler {
	
	 final static String windowsFilePath = "C://WINDOWS//system32//drivers//etc//hosts";
	 final static String linuxFilePath = "/etc/hosts";
	 
	 public synchronized static  boolean updateHostName(String ip,String hostName) throws Exception {
	        String splitter = " ";
	        String fileName = null;
	        if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
	            fileName = linuxFilePath;
	        } else {
	            fileName = windowsFilePath;
	        }

	        List < ? > lines = FileUtils.readLines(new File(fileName));
	        List <String> newLines = new ArrayList <String>();
	        boolean findFlag = false;
	        boolean updateFlag = false;
	        for (Object line : lines) {
	            String strLine = (String) line;
	            if (strLine.trim().length()>0 && !strLine.startsWith("#")) {
	                strLine = strLine.replaceAll("/t", splitter);
	                int index = strLine.toLowerCase().indexOf(hostName.toLowerCase());
	                if (index != -1) {
	                    String[] array = strLine.trim().split(splitter);
	                    for (String name : array) {
	                        if (hostName.equalsIgnoreCase(name)) {
	                            findFlag = true;
	                            if (array[0].equals(ip)) {
	                                newLines.add(strLine);
	                                break;
	                            }
	                            StringBuilder sb = new StringBuilder();
	                            sb.append(ip);
	                            for (int i = 1; i < array.length; i++) {
	                                sb.append(splitter).append(array[i]);
	                            }
	                            newLines.add(sb.toString());
	                            updateFlag = true;
	                            break;
	                        }
	                    }

	                    if (findFlag) {
	                        break;
	                    }
	                }
	            }
	            newLines.add(strLine);
	        }
	        if (!findFlag) {
	            newLines.add(new StringBuilder(ip).append(splitter).append(hostName).toString());
	        }

	        if (updateFlag || !findFlag) {
	            FileUtils.writeLines(new File(fileName), newLines);
	            String formatIp = formatIpv6IP(ip);
	            for (int i = 0; i < 20; i++) {
	                try {
	                    boolean breakFlg = false;
	                    InetAddress[] addressArr = InetAddress.getAllByName(hostName);

	                    for (InetAddress address : addressArr) {
	                        if (formatIp.equals(address.getHostAddress())) {
	                            breakFlg = true;
	                            break;
	                        }
	                    }

	                    if (breakFlg) {
	                        break;
	                    }
	                } catch (Exception e) {
	                   ErrorHandler.continueRunning(e, "更新hosts文件异常!", false);
	                }
	                Thread.sleep(3000);
	            }
	        }

	        return updateFlag;
	    }

	private static String formatIpv6IP(String ipV6Addr) {
		String strRet = ipV6Addr;
	    StringBuffer replaceStr;
	    int iCount = 0;
	    char ch = ':';	        
	    if (ipV6Addr.trim().length()>0 && ipV6Addr.indexOf("::") > -1) {
	    	for (int i = 0; i < ipV6Addr.length(); i++) {
	    		if (ch == ipV6Addr.charAt(i)) {
	    			iCount++;
	            }
	        }

	        if (ipV6Addr.startsWith("::")) {
	        	replaceStr = new StringBuffer("0:0:");
	             for (int i = iCount; i < 7; i++) {
	             	replaceStr.append("0:");
	             }
	        } else if (ipV6Addr.endsWith("::")) {
	        	replaceStr = new StringBuffer(":0:0");
	            for (int i = iCount; i < 7; i++) {
	            	replaceStr.append(":0");
	            }
	        } else {
	            replaceStr = new StringBuffer(":0:");
	            for (int i = iCount; i < 7; i++) {
	            	replaceStr.append("0:");
	            }
	        }
	          strRet = ipV6Addr.trim().replaceAll("::", replaceStr.toString());
	    }
	        
	    return strRet;
	}
	    
	public static void removeHosts(){
		File file=new File(windowsFilePath);
	    List<?> lines;
		try {
			lines = FileUtils.readLines(file);
			int len=lines.size();
			for(int i=len-1;i>=0;i--){
				String lineString=((String)lines.get(i)).trim();
				if(!lineString.startsWith("#")&&!lineString.trim().equals("")){
					lines.remove(i);
				}
			}
			FileUtils.writeLines(file, lines);
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "删除hosts原有内容异常!", false);
		}
	}
	
	public static List<String> getHosts(){
		File file=new File(windowsFilePath);
	    List<String> lines = new ArrayList<String>();
		try {
			lines = FileUtils.readLines(file);
			int len=lines.size();
			for(int i=len-1;i>=0;i--){
				String lineString=((String)lines.get(i)).trim();
				if(lineString.startsWith("#")&&lineString.trim().equals("")){
					lines.remove(i);
				}
			}
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "获取当前hosts文件内容异常!", false);
		}
		return lines;
	}
	
	public synchronized static  void init(){	
		XmlParser p = new XmlParser("suite.xml");
		Map<String,String> hosts = new HashMap<String,String>();
		hosts = p.getChildrenInfo("/suite/host", "hostName", "ip");
		List <String> newLines = new ArrayList <String>();
		if(hosts.size()!=0){
			removeHosts();			
			for(String hostName:hosts.keySet()){
				newLines.add(hosts.get(hostName)+" "+hostName);
			}			
			try {
				FileUtils.writeLines(new File(windowsFilePath), newLines);
			} catch (IOException e) {
				ErrorHandler.continueRunning(e, "写入hosts文件异常!", false);
			}
			refresh();
		}
		Log.info("当前运行hosts:\n"+getHosts());
		Reporter.record(0, "当前运行hosts:\n"+getHosts());
	}
	
	public static void refresh(){
		Runtime runtime=Runtime.getRuntime();
		try {
			runtime.exec("cmd.exe /k start ipconfig /flushdns");
		} catch (IOException e) {
			ErrorHandler.continueRunning(e, "刷新hosts文件异常!", false);
		}
	}	
}
