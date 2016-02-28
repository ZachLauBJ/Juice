package org.juice;

import java.util.HashSet;
import java.util.List;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;


public class CheckPoint extends Assertion{

	private int flag = 0;
	
	@Override
	public void onAssertSuccess(IAssert assertCommand) {
		Log.info("[断言成功]： "
				  +"预期结果: "+assertCommand.getExpected()
				  +"实际结果: "+assertCommand.getActual());
	}

	@Override
	public void onAssertFailure(IAssert assertCommand) {
		String message = "";
		if(assertCommand.getMessage()!=null){
			message = assertCommand.getMessage();
		}
		Log.error("[断言失败]： "
				  +"预期结果: "+assertCommand.getExpected()
				  +"实际结果: "+assertCommand.getActual());

		Reporter.record(2, message);
		flag = flag+1;
	}
	
	public void equals(boolean actual,boolean expected,String message){
		try{
			assertEquals(actual, expected, message);
		}catch(Error e){}
	}
	
	public void equals(boolean actual,boolean expected){
		try{
			assertEquals(actual, expected);
		}catch(Error e){}
	}
	
	public void equals(String actual,String expected,String message){
		try{
			assertEquals(actual, expected, message);
		}catch(Error e){}
	}
	
	public void equals(String actual,String expected){
		try{
			assertEquals(actual, expected);
		}catch(Error e){}
	}
	
	public void equals(long actual,long expected,String message){
		try{
			assertEquals(actual, expected, message);
		}catch(Error e){}				
	}
	
	public void equals(long actual,long expected){
		try{
			assertEquals(actual, expected);
		}catch(Error e){}				
	}
	
	public void equals(int actual,int expected,String message){
		try{
			assertEquals(actual, expected, message);
		}catch(Error e){}				
	}
	
	public void equals(int actual,int expected){
		try{
			assertEquals(actual, expected);
		}catch(Error e){}				
	}
	
	public void equals(List<String> actual,List<String> expected ,String message){
		if(actual.size()!=0){
			try{
				assertEquals(actual, expected, message);
			}catch(Error e){}		
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);			
	}
	
	public void equals(List<String> actual,List<String> expected){
		if(actual.size()!=0){
			try{
				assertEquals(actual, expected);
			}catch(Error e){}		
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);				
	}
	
	public void equals(String[] actual,String[] expected,String message){
		if(actual.length!=0){
			try{
				assertEquals(actual, expected, message);
			}catch(Error e){}		
		}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);				
	}
	
	public void equals(String[] actual,String[] expected){
		if(actual.length!=0){
			try{
				assertEquals(actual, expected);
			}catch(Error e){}		
		}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);			
	}
	
	public void equals(String[] actuals,String expected,String message){
		if(actuals.length!=0){
			for(String actual:actuals){
				try{
					assertEquals(actual, expected, message);
				}catch(Error e){}	
			}	
		}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);
	}
	
	public void equals(String[] actuals,String expected){
		if(actuals.length!=0){
			for(String actual:actuals){
				try{
					assertEquals(actual, expected);
				}catch(Error e){}	
			}	
		}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);
	}
	
	public void equals(List<String> actuals, String expected ,String message){
		if(actuals.size()!=0){
			for(String actual:actuals){
				try{
					assertEquals(actual, expected, message);
				}catch(Error e){}	
			}	
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);
	}
	
	public void equals(List<String> actuals, String expected){
		if(actuals.size()!=0){
			for(String actual:actuals){
				try{
					assertEquals(actual, expected);
				}catch(Error e){}	
			}	
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);
	}
	
	public void notEquals(boolean actual,boolean expected,String message){
		try{
			assertNotEquals(actual, expected, message);
		}catch(Error e){}				
	}
	
	public void notEquals(boolean actual,boolean expected){
		try{
			assertNotEquals(actual, expected);
		}catch(Error e){}				
	}
	
	public void notEquals(String actual,String expected,String message){
		try{
			assertNotEquals(actual, expected, message);
		}catch(Error e){}				
	}

	public void notEquals(String actual,String expected){
		try{
			assertNotEquals(actual, expected);
		}catch(Error e){}				
	}
	
	public void notEquals(long actual,long expected){
		try{
			assertNotEquals(actual, expected);
		}catch(Error e){}				
	}
	
	public void notEquals(long actual,long expected,String message){
		try{
			assertNotEquals(actual, expected, message);
		}catch(Error e){}				
	}
	
	public void notEquals(int actual,int expected,String message){
		try{
			assertNotEquals(actual, expected, message);
		}catch(Error e){}			
	}	
	
	public void notEquals(int actual,int expected){
		try{
			assertNotEquals(actual, expected);
		}catch(Error e){}			
	}
	
	public void notEquals(String[]actual,String expected,String message){
		if(actual.length!=0){	
			for(int i=0;i<actual.length;i++){				
				notEquals(actual[i], expected, message);
			}
		}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);
	}
	
	public void notEquals(String[]actual,String expected){
		if(actual.length!=0){	
			for(int i=0;i<actual.length;i++){				
				notEquals(actual[i], expected);
			}
		}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);
	}
	
	public void notEquals(List<String>actual,String expected,String message){
		if(actual.size()!=0){
			for(int i=0;i<actual.size();i++){			
				notEquals(actual.get(i), expected, message);
			}
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);
	}
	
	public void notEquals(List<String>actual,String expected){
		if(actual.size()!=0){
			for(int i=0;i<actual.size();i++){			
				notEquals(actual.get(i), expected);
			}
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);
	}
	
	public void startWith(String actual,String expected,String message){		
		if (!actual.startsWith(expected)){			
			equals(true, false ,message+"[实际结果:"+actual+" 预期结果:"+expected+"]");
		}			
	}
	
	public void startWith(String[] actual,String expected,String message){	
		if(actual.length!=0){
		for(String act : actual )			
			if (!act.startsWith(expected)){				
				equals(true, false ,message+"[实际结果:"+act+" 预期结果:"+expected+"]");
			}	
		}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);
	}
	
	public void startWith(List<String> actual,String expected,String message){	
		if(actual.size()!=0){
		for(String act : actual )			
			if (!act.startsWith(expected)){				
				equals(true, false ,message+"[实际结果:"+act+" 预期结果:"+expected+"]");
			}	
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);
	}
	
	public void contains(String[] actual,String expected,String message){
		if(actual.length!=0){
			for(String act : actual )			
				if (!act.contains(expected)){			
	
				}
			}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);
	}
	
	public void contains(String[] actual,String expected1,String expected2,String message ){
		if(actual.length!=0){
			for(String act : actual )			
				if (!act.contains(expected1) && !act.contains(expected2) ){			
					equals(true, false ,message+"[实际结果:"+act+" 预期结果:"+expected1+" "+expected2+"]");
				}
			}else ErrorHandler.continueRunning("检查点函数:实际结果 数组 对象为空!", false);
	}
	
	public void contains(List<String> actual,String expected,String message){
		if(actual.size()!=0){
			for(String act : actual )			
				if (!act.contains(expected)){			
					equals(true, false ,message+"[实际结果:"+act+" 预期结果:"+expected+"]");
				}
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);
	}
	
	public void contains(List<String> actual,String expected1,String expected2,String message ){
		if(actual.size()!=0){
			for(String act : actual )			
				if (!act.contains(expected1) && !act.contains(expected2) ){			
					equals(true, false ,message+"[实际结果:"+act+" 预期结果:"+expected1+" "+expected2+"]");
				}
		}else ErrorHandler.continueRunning("检查点函数:实际结果 集合 对象为空!", false);
	}
	
	public void isRepeat(List<String> actual,String message){
		HashSet<String> set=new HashSet<String>();
		for(String s:actual){
			set.add(s);
		}		
		if(!(set.size()==actual.size())){
			equals(true, false ,message);
		}
	}
	
	/**
	* @Description 当case中需要用到if语句检查结果，错误时使用该检查点
	* @param message：Faild时需要记录到日志的信息
	*/
	public void isFaild(String message){
		equals(true, false, message);
	}
	
	/**
	* @Description 当case中需要用到if语句检查结果，pass时强制使用该检查点
	* @param message：pass时需要记录信息
	*/
	public void isSuccess(String message){
		equals(true, true, message);
	}
	
	public void addInfor(String message){
		Log.info(message);
		Reporter.record(0, message);
	}
	
	/**
	* @Description 依据flag，判断此前所有检查点中是否有错误值。若失败，退出当前运行的.java程序。
	* 			         失败后，result方法下方的代码将不会被执行
	* @param message:case成功时需要记录到日志的信息
	*/
	public void result(String message){	
		org.testng.Assert.assertEquals(flag, 0);
		Log.info(message);
		Reporter.record(1, message);
	}
	
	public void result(int iFlag,String message){	
		org.testng.Assert.assertEquals(iFlag, 0);
		Log.info(message);
		Reporter.record(1, message);
	}

	/**
	* @Description 阶段性检查点函数.依据flag，判断此前所有检查点中是否有错误值.若存在，不终止当前用例，只重置标志位.
	* @param message:失败时需要记录的信息
	*/
	public void phaseResult(String message){
		equals(flag, 0, message);
		initFlag();
	}
	
	public int getFlag(){
		return flag;		
	}
	
	public void initFlag(){
		flag = 0;		
	}	
}
