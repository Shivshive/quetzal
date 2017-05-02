package com.shiv.quetzal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Configuration {
	
	XSSFWorkbook workbook;
	XSSFSheet mastersheet,resultsheet;
	FileInputStream fis;
	File wpath;
	String environement , browser ;
	int testcaserow, testcasecell;
	WebDriver driver;
	String testcasename;
	
	public Configuration()
	{
		//Configuration Class sets all properties required to execute test..
		//Also contains required methods to pass required instances..
		
		wpath = new File("./MasterSheet.xlsx");
		try 
		{
			fis = new FileInputStream(wpath);
			workbook = new XSSFWorkbook(fis);
			mastersheet = workbook.getSheetAt(0);
			resultsheet = workbook.getSheetAt(1);
			environement = mastersheet.getRow(1).getCell(3).toString();
			browser = mastersheet.getRow(1).getCell(2).toString();
			//Takes browser as parameter and assign appropriate driver..
			switch(browser)
			{
				case "googlechrome" : 
					System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
					driver = new ChromeDriver();
					break;
				
				case "firefox" :
					//GECKO Driver provides support for Firefox version greater than- v45..
					System.setProperty("webdriver.gecko.driver","./Drivers/geckodriver.exe");
					driver = new FirefoxDriver();
					break;
			
				case "iexplorer" : 
					System.setProperty("webdriver.IE.driver", "./Drivers/IEDriverServer.exe");
					driver = new InternetExplorerDriver();
					break;
					
				default : 
					System.out.println("Driver not mentioned..");
					break;
				
			}
			
			for(int i=0;i<=mastersheet.getLastRowNum();i++)
			{
				if(mastersheet.getRow(i).getCell(1).toString().equalsIgnoreCase("Y"))
				{
					testcasename = mastersheet.getRow(i).getCell(0).toString();
					testcaserow = i;
					testcasecell=1;
					System.out.println("Selected Test Case :"+testcasename);
					System.out.println("Row :"+testcaserow);
					System.out.println("Cell :"+testcasecell);
					break;
				}
			}
			
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Master File is not on path.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Returns Execution Browser..
		public String getBrowser()
		{
			return browser;
		}
		
		//Returns Execution Environment..
		public String getEnvironment()
		{
			return environement;
		}
		
		//Returns WebDriver instance..
		public WebDriver getDriver()
		{
			return driver;
		}
		
		//Returns TestCase Name..
		public String testcaseName()
		{
			return testcasename;
		}
		
		//Return Testcase Row..
		public int testcaseRow()
		{
			return testcaserow;
		}
		
		//Return Testcase Cell..
		public int testcasecell()
		{
			return testcasecell;
		}
		public XSSFWorkbook getMasterwokbook()
		{
			return workbook;
		}
		
		public boolean takeScreenShot(String Result)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hhmmss");
			Date dt = new Date();
			String formattedDate = formatter.format(dt);
			
			try 
			{
				File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				
			 // now copy the  screenshot to desired location using copyFile //method
			  
			String FileName = testcasename+"_"+Result+"_"+formattedDate;	
			FileUtils.copyFile(src, new File("./TestScreenShots/"+FileName+".jpg"));
			return true;
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
				return false;
			  		 
			}
			
			
		}

}
