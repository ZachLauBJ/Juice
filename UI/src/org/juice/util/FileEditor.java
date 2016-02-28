package org.juice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import org.juice.ErrorHandler;


public class FileEditor {

		
	public static void createFile(String context, String targetPath, String encoding){
		OutputStreamWriter pw = null;
		try {
			pw = new OutputStreamWriter(new FileOutputStream(targetPath),encoding);
			pw.write(context);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			
			try {
				if(pw!=null)
					pw.close();
			} catch (IOException e) {			
				ErrorHandler.continueRunning(e, "输出文件,关闭流异常！  ", true);
			}
		}	
	}
	
		
	public static void copyFile(String srcPath, String targetPath){		
		 File srcFile = new File(targetPath);
		 FileChannel in = null;  
		 FileChannel out = null;  
		 FileInputStream inStream = null;  
		 FileOutputStream outStream = null; 		 
		 try {
			inStream = new FileInputStream(srcFile);
			outStream = new FileOutputStream(targetPath);  
			in = inStream.getChannel();  
			out = outStream.getChannel();  			
			in.transferTo(0, in.size(), out);
			
		 } catch (IOException e) {
				e.printStackTrace();
		 } finally {  

		        try {
					in.close();
					inStream.close();
					out.close();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }  	 
	}
	
}
