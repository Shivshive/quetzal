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

public class ItestListner_Impl implements ITestListener {
	
	WebDriver driver;
	String testcasename;
	

	@Override
	public void onTestStart(ITestResult result) {
		
		driver = ((New_Test)result.getInstance()).driver;
		testcasename = ((New_Test)result.getInstance()).testscriptname;
		
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
	
		System.out.println("onTestSuccess Called.");
		takeScreenShot(result);
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		
		System.out.println("onTestFailure Called.");
		takeScreenShot(result);
		
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
		
		try 
		{
			File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
		 // now copy the  screenshot to desired location using copyFile //method
		  
		String FileName = testcasename+"_"+result.getStatus()+"_"+formattedDate;	
		FileUtils.copyFile(src, new File("C:/Sel_Frmwork/TestScripts/"+FileName+".jpg"));
		System.out.println("Screen shot taken.");
		return true;
		}
		catch (IOException e)
		{
			System.out.println("Screen shot not taken.");
			System.out.println(e.getMessage());
			return false;
		  		 
		}
		
		
	}
	

}
