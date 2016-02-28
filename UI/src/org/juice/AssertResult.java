package org.juice;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

public class AssertResult extends Assertion{

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
		Log.error("[断言失败]： " +message
							  +" 预期结果: " 	 +assertCommand.getExpected()
							  +" 实际结果: "		 +assertCommand.getActual());		
		Reporter.record(2, message);
	}
	
}
