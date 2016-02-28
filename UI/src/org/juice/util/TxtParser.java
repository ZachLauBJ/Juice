package org.juice.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TxtParser {
	
	String filePath = null;
	BufferedReader br = null;
	BufferedWriter bw = null;
	
	public TxtParser(String filePath){
		this.filePath = filePath;
	}
	
	public List<String> readByLinesToList()  {
		
		List<String> result = new ArrayList<String>();
		
		try {
			br = new BufferedReader(new FileReader(filePath));	
			String temp = null;
			
			while((temp=br.readLine()) != null){
				result.add(temp);
			}
							
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}finally{
		
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			    
		return result;	
	}
	
	public String readByLinesToString()  {
		
		StringBuffer result = new StringBuffer();
		
		try {
			br = new BufferedReader(new FileReader(filePath));	
			String temp = null;
			
			while((temp=br.readLine()) != null){
				result.append(temp);
			}
							
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}finally{
		
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			    
		return result.toString();	
	}

	public void writeByLines(List<String> texts){
		
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			int size = texts.size();
			for(int i=0;i<size-1;i++){
				bw.write(texts.get(i));
				bw.write("\n");
			}
			bw.write(texts.get(size-1));	
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			if(bw !=null){
				try {
					bw.flush();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(bw !=null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
