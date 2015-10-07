package com.pulselms.app.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IResultMap;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

/**
 * This class is representing the TestNG Listener and we can override the methods 
 * which gets invoked before and after the tests get executed by TestNG
 * @author Cognizant
 *
 */
public class CustomListeners implements ITestListener {

   
    @SuppressWarnings("unused")
    private String testRoot = EnvParameters.TEST_ROOT_DIR;
 

    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
        // TODO Auto-generated method stub

    }

    public void onFinish(ITestContext context) {
   
        removeIncorrectlyFailedTests(context);
    }

    public void onStart(ITestContext context) {
       
    }

   
    public void onTestFailure(ITestResult result) {

    	String sTestMethodName = result.getMethod().getMethodName();
		String sTestSuiteName = result.getTestClass().getRealClass().getSimpleName();
		String timeTaken = Long.toString((result.getEndMillis() - result.getStartMillis()) / 1000);
		String testName = result.getTestClass().getName() + "."	+ result.getMethod().getMethodName();
		takeScreenShot(sTestMethodName);
		LoggerUtil.log("<<<*** END: " + sTestSuiteName + "." + sTestMethodName	+ " ***>>> ");
		LoggerUtil.log("=====================================================================================");
		LoggerUtil.log("Test Failed :" + testName + ", Took " + timeTaken + " seconds");

    }

    public void onTestSkipped(ITestResult iTestResult) {
    	String timeTaken = Long.toString((iTestResult.getEndMillis() - iTestResult
				.getStartMillis()) / 1000);
		String testName = iTestResult.getTestClass().getName() + "."
				+ iTestResult.getMethod().getMethodName();
		LoggerUtil.log("/////////////////////////////////////////////////////////////////////////////////////////");
		LoggerUtil.log("Test Skipped :" +testName+ ", Took " + timeTaken
				+ " seconds");

    }

    public void onTestStart(ITestResult iTestResult) {

    	String sTestMethodName = iTestResult.getMethod().getMethodName();
		String sTestSuiteName = iTestResult.getTestClass().getRealClass().getSimpleName();
		LoggerUtil
		.log("===============================================================================================");
		LoggerUtil.log("<<<*** START: " + sTestSuiteName + "."
				+ sTestMethodName + " ***>>> ");
		

    }

    public void onTestSuccess(ITestResult iTestResult) {
    	String sTestMethodName = iTestResult.getMethod().getMethodName();
		String sTestSuiteName = iTestResult.getTestClass().getRealClass().getSimpleName();
		String timeTaken = Long.toString((iTestResult.getEndMillis() - iTestResult.getStartMillis()) / 1000);
		String testName = iTestResult.getTestClass().getName() + "." + iTestResult.getMethod().getMethodName();
		
		LoggerUtil.log("<<<*** END: " + sTestSuiteName + "." + sTestMethodName + " ***>>> ");
		LoggerUtil.log("=====================================================================================");
		LoggerUtil.log("Test Passed :" + testName + ", Took " + timeTaken + " seconds");

    }
    
    public void takeScreenShot(String methodName) {
    	//get the driver
    	WebDriver driver=WebDriverBase.getDriver();
    	 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
         //The below method will save the screen shot in d drive with test method name 
            try {
				FileUtils.copyFile(scrFile, new File("target"+File.separator+"screenshots"+File.separator+methodName+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
    }


    // @Override
            public
            void afterInvocation(IInvokedMethod method, ITestResult result) {
        Reporter.setCurrentTestResult(result);

        if (method.isTestMethod()) {

            List<Throwable> verificationFailures =
                    VerifySafe.getVerificationFailures();
            // if there are verification failures...
            if (verificationFailures.size() > 0) {
                // set the test to failed
                result.setStatus(ITestResult.FAILURE);

                // if there is an assertion failure add it to
                // verificationFailures
                if (result.getThrowable() != null) {
                    verificationFailures.add(result.getThrowable());
                }

                int size = verificationFailures.size();
                // if there's only one failure just set that
                if (size == 1) {
                    result.setThrowable(verificationFailures.get(0));
                } else {
                    // create a failure message with all failures and stack
                    // traces (except last failure)
                    StringBuffer failureMessage =
                            new StringBuffer("Multiple failures (")
                                    .append(size).append("):\n\n");
                    for (int i = 0; i < size; i++) {
                        failureMessage.append("Failure ").append(i + 1).append(
                                " of ").append(size).append(":\n");
                        Throwable t = verificationFailures.get(i);
                        String errorMessage = null;
                        errorMessage = Utils.stackTrace(t, false)[1];
                        failureMessage.append(errorMessage).append("\n\n");
                    }

                    Throwable merged = new Throwable(failureMessage.toString());
                    //

                    result.setThrowable(merged);
                }
            }
        }
    }

    private IResultMap removeIncorrectlyFailedTests(ITestContext test) {
        List<ITestNGMethod> failsToRemove = new ArrayList<ITestNGMethod>();
        IResultMap returnValue = test.getFailedTests();
        for (ITestResult result : test.getFailedTests().getAllResults()) {
            long failedResultTime = result.getEndMillis();
            for (ITestResult resultToCheck : test
                    .getFailedButWithinSuccessPercentageTests().getAllResults()) {
                if (failedResultTime == resultToCheck.getEndMillis()) {
                    failsToRemove.add(resultToCheck.getMethod());
                    break;
                }
            }
            for (ITestResult resultToCheck : test.getPassedTests()
                    .getAllResults()) {
                if (failedResultTime == resultToCheck.getEndMillis()) {
                    failsToRemove.add(resultToCheck.getMethod());
                    break;
                }
            }
        }
        for (ITestNGMethod method : failsToRemove) {
            returnValue.removeResult(method);
        }
        return returnValue;
    }

   
}
