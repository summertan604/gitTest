package com.xy.extentReport;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseExample {

    final String filePath = "test-output/Extent123.html";
    protected static ExtentReports extent;
    protected ExtentTest test;

    @AfterMethod
    protected void afterMethod(ITestResult result) {

        extent.endTest(test);
        extent.flush();
    }

    @BeforeSuite
    public void beforeSuite() {
        extent = getReporter(filePath);
    }

    @AfterSuite
    protected void afterSuite() {
        extent.close();
    }

    public synchronized static ExtentReports getReporter() {
        return extent;
    }

    public synchronized static ExtentReports getReporter(String filePath) {
        if (extent == null) {
            extent = new ExtentReports(filePath, NetworkMode.OFFLINE); //设置为离线报告
        }

        return extent;
    }
}