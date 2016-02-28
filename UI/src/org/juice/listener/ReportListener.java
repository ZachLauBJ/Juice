package org.juice.listener;

import java.util.Map;
import org.juice.Reporter;


public class ReportListener extends Reporter{

	@Override
	protected void doReport(Map<String, Map<String, String>> testResultSet) {
		for(String key:testResultSet.keySet()){
			System.out.println(key);
				for(String testname:testResultSet.get(key).keySet()){
					System.out.println(testname+" : "+testResultSet.get(key).get(testname));
				}
		System.out.println("-------------------------");		
		}
	}

//	@Override
//	protected void doReport(Map<String, String> testResultSet) {
//
//			
//		String context = getReportTemplate("SampleReport.txt");
//		context = context.replaceAll("#juice_projectName", testResultSet.get(PROJECT_NAME));
//		context = context.replaceAll("#juice_testEndTime", testResultSet.get(TEST_END_TIME));
//		context = context.replaceAll("#juice_passedNumber", testResultSet.get(PASSED_NUMBER));
//		context = context.replaceAll("#juice_skippedNumber", testResultSet.get(SKIPPED_NUMBER));
//		context = context.replaceAll("#juice_failedNumber", testResultSet.get(FAILED_NUMBER));
//		context = context.replaceAll("#juice_passRate", testResultSet.get(PASS_RATE));
//		context = context.replaceAll("#juice_customLog", testResultSet.get(CUSTOM_LOG));	
//		FileEditor.createFile(context, "D://1.html", "GBK");
//		System.out.println( testResultSet.get(CUSTOM_LOG));
//	}


}
