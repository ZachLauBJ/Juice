package org.juice.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.juice.Log;
import org.juice.Reporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class ExecutionListener implements ITestListener{
	
	@Override
	public void onFinish(ITestContext context) {
				ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
				Set<Integer> passedTestIds = new HashSet<Integer>();
				for (ITestResult passedTest : context.getPassedTests().getAllResults()) {
					passedTestIds.add(getId(passedTest));
				}
				Set<Integer> failedTestIds = new HashSet<Integer>();
				for (ITestResult failedTest : context.getFailedTests().getAllResults()) {
					int failedTestId = getId(failedTest);
					if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
						testsToBeRemoved.add(failedTest);
					} else {
						failedTestIds.add(failedTestId);
					}
				}
				for (Iterator<ITestResult> iterator = 
				context.getFailedTests().getAllResults().iterator(); iterator.hasNext();) {
					ITestResult testResult = iterator.next();
					if (testsToBeRemoved.contains(testResult)) {
						iterator.remove();
					}
				}
	}
		
	private int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = id + result.getMethod().getMethodName().hashCode();
		id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
		return id;
	}

	@Override
	public void onStart(ITestContext context) {
		Log.info("测试开始执行,为所有测试用例添加失败重跑机制");
		for(ITestNGMethod method: context.getAllTestMethods()){
			method.setRetryAnalyzer(new TestRetryAnalyzer());
			Log.info(method.getMethodName()+" -> set retry");
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		TestRetryAnalyzer testRetryAnalyzer = (TestRetryAnalyzer) result
				.getMethod().getRetryAnalyzer();
		int currentMaxRetryTimes = testRetryAnalyzer.getMaxRetryTime();
		int currentRetryIndex = testRetryAnalyzer.getRetryCount();		
		String message = "当前用例:"+result.getName()+" 进行第  "+currentRetryIndex+" 次 失败重跑!";
		if(currentRetryIndex<currentMaxRetryTimes){
			Log.error(message);
			Reporter.record(2, message);
		}
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		TestRetryAnalyzer testRetryAnalyzer = (TestRetryAnalyzer) result
				.getMethod().getRetryAnalyzer();
		testRetryAnalyzer.setRetryCount(0);		
	}

}
