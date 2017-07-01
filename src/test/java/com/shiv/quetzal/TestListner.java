package com.shiv.quetzal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestListner implements ITestListener {
	
	WebDriver driver;
	String testcasename;

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		
		this.driver = ((New_Test)result.getInstance()).driver;
		testcasename = ((New_Test)result.getInstance()).testscriptname;
		Reporter.log("Setting UP Driver", true);
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		Reporter.log("Test Success`", true);	
		takeScreenShot(result);
		System.out.println(result.toString());
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		Reporter.log("Test Failure", true);
		takeScreenShot(result);
		System.out.println(result.toString());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean takeScreenShot(ITestResult result)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hhmmss");
		Date dt = new Date();
		String formattedDate = formatter.format(dt);
		String FileName = null;
		
		try 
		{
			File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
		 // now copy the  screenshot to desired location using copyFile //method
		  
		 FileName = testcasename+"_"+result.getStatus()+"_"+formattedDate;	
		FileUtils.copyFile(src, new File("C:"+File.separator+"Selenium"+File.separator+"ScreenSrhot"+File.separator+FileName+".jpg"));
		
		System.out.println("Screen Shot taken.");
		
		return true;
		}
		catch (IOException e)
		{
			System.out.println("Screen Shot not taken.");
			System.out.println("Filename"+FileName);
			System.out.println(e.getMessage());
			return false;
		  		 
		}			
		
	}

}
