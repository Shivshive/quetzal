package com.shiv.quetzal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

//Implemented TestListner for this..
@Listeners(TestListner.class)
public class New_Test {
	
	Configuration config;
	WebDriver driver;
	String environment, browser;
	XSSFWorkbook workbook;
	XSSFSheet mastersheet, resultsheet, testscript;
	String testscriptname;
	File testscriptpath;
	FileInputStream fis;
	Boolean flag=true;
	String Result = null;
	String currentwindow;

	
	@BeforeSuite
	public void setUp()
	{
		config = new Configuration();
		driver = config.getDriver();
		environment = config.getEnvironment();
		browser = config.getBrowser();
		testscriptname = config.testcaseName();
		testscriptpath = new File("./TestScripts/"+testscriptname+".xlsx");
		try 
		{
			fis = new FileInputStream(testscriptpath);
			workbook = new XSSFWorkbook(fis);
			//test script has been selected..
			testscript = workbook.getSheetAt(0);
			
			//MasterSheet Details -
			workbook = config.getMasterwokbook();
			mastersheet = workbook.getSheetAt(0);
			resultsheet = workbook.getSheetAt(1);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Hey Buddy ! TestScript does not seems to be presnet in Tesscript Folder, please check it..");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void sampleTest() throws InterruptedException
	{
		try
		{
			driver.get(environment);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver,20);		
			int cmd_row=1;
			String CurrentFrame = null;
			while(flag==true)
			{

				String command, element , elementtype;
				WebElement welement = null;
				By by = null;
				DataFormatter fmt = new DataFormatter();

				command = testscript.getRow(cmd_row).getCell(0).toString();
				elementtype = testscript.getRow(cmd_row).getCell(1).toString();
				element = testscript.getRow(cmd_row).getCell(2).toString();

				//value = testscript.getRow(cmd_row).getCell(3).toString();
				Cell cell = testscript.getRow(cmd_row).getCell(3);
				String value = fmt.formatCellValue(cell);


				if(!elementtype.equals("A"))
				{
					switch(elementtype)
					{
					case "id" :

						by = By.id(element);
						break;

					case "name":
						by = By.name(element);
						break;

					case "xpath":
						by = By.xpath(element);
						break;

					case "css":
						by = By.cssSelector(element);
						break;

					case "linkText":
						by = By.linkText(element);
						break;

					case "partialLinkText":
						by = By.partialLinkText(element);
						break;

					default:
						by = null;
						break;

					}

				}

				/*if()
			{

			}
			else
			{*/
				switch(command)
				{
				case "open" :

					System.out.println(command+" "+value);
					driver.navigate().to(value);
					System.out.println("Executed Successfully.");					
					break;

				case "type" :

					System.out.println(command+" "+value+" into "+element);
					try 
					{
						welement = driver.findElement(by);
						welement.clear();
						
					} catch (Exception e3) 
					{						
						welement = tryJavaScript(element, elementtype);
						if(welement == null)
						{
							declareFailed(command, element);
							e3.printStackTrace();
		
						}
					}				
					welement.sendKeys(value);
					break;

				case "click" :

					System.out.println(command+" on "+element);
					try
					{
						welement = driver.findElement(by);	
						
					}
					catch(Exception e)
					{
						welement = tryJavaScript(element, elementtype);
						if(welement == null)
						{
							declareFailed(command, element);
							e.printStackTrace();

						}
						
					}
					welement.click();
					System.out.println("Executed Successfully.");
					break;

				case "select" :

					try {
						welement = driver.findElement(by);
					} catch (Exception e3) {
						
						welement = tryJavaScript(element, elementtype);
						if(welement == null)
						{
							declareFailed(command, element);
							e3.printStackTrace();
						}
						
					}
					
					Select dropdown = new Select(welement);
					System.out.println(command+" "+value+" from "+dropdown.getOptions());
					dropdown.selectByVisibleText(value);
					System.out.println("Executed Successfully.");
					driver.findElement(by).sendKeys(Keys.TAB);
					break;

				case "END" :

					System.out.println("Test Complete ..");
					flag = false;
					break;

				case "pause" :
					int time  = Integer.parseInt(value);
					System.out.println(command+" for "+value);
					Thread.sleep(time);
					System.out.println("Executed Successfully");			
					break;	

				case "clickandwait" :

					System.out.println(command+" on "+element);
					try {
						welement = wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
						welement.click();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						welement = tryJavaScript(element, elementtype);
						if(welement == null)
						{
							declareFailed(command, element);
							e.printStackTrace();
						}
						
					}
					Thread.sleep(5000);
					System.out.println("Executed Successfully.");
					break;

				case "verifytextpresent" :

					System.out.println(command+" text : "+value);
					flag = verifytextpresent(value);
					if(flag == true)
					{
						System.out.println("Executed Successfully.");
						Result = "PASS";
					}
					else
					{
						Result = "FAIL";
					}
					Assert.assertTrue(flag, "VerifyTextPresent Failed. Text '/"+value+"'/ , does not present.");
					break;


				case "verifyelementpresent" : 

					System.out.println(command+" element : "+element);
					flag = verifyelementpresent(by);
					if(flag == true)
					{
						System.out.println("Executed Successfully.");
						Result = "PASS";
					}
					else
					{
						System.out.println("Executed Successfully.");
						Result = "FAIL";
					}
					Assert.assertTrue(flag, "Command:"+command+" execution "+Result+" over "+element);
					break;

				case "accept_certificate" :
					System.out.println(command);
					Runtime rc = Runtime.getRuntime();
					try
					{
						rc.exec("./accpet_cert.exe");
						System.out.println("Executed Successfully");
					}
					catch (IOException e1) 
					{
						System.out.println("Execution Failed.");
						e1.printStackTrace();
					}	
					break;


				case "SwitchtoFrame" :

					System.out.println(command+" to - "+element);
					if(element.contains("default"))
					{
						driver.switchTo().defaultContent();
						CurrentFrame = "default";

					}
					else
					{

						driver.switchTo().frame(element);
						CurrentFrame = element;
					}						
					System.out.println("Executed Successfully");
					break;

				case "SwitchtoWindow" :
				case "SwitchtoDefault" : 
					
					if(command.equalsIgnoreCase("SwitchtoWindow"))
					{
						System.out.println(command+" to - "+element);
						currentwindow = driver.getWindowHandle();
						Set<String> openwindows = driver.getWindowHandles();
						try {
							for(String testwin : openwindows)
							{
								driver.switchTo().window(testwin);
								if(driver.getTitle().equalsIgnoreCase(element))
								{
									System.out.println("Executed Successfully");	
									break;
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Executed Failed");
							e.printStackTrace();
						}
						
					}
					
					if(command.equalsIgnoreCase("SwitchtoDefault"))
					{
						try {
							driver.switchTo().window(currentwindow);
							System.out.println("Executed Successfully");
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Executed Failed");
							e.printStackTrace();
						}
					}
					break;

				case "Scrolldown" :	
					((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
					break;

				case "Selectfromsuggestion" :

					System.out.println(command+" value - "+value);
					//welement = wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
					welement = driver.findElement(by);
					welement.sendKeys(value);
					Thread.sleep(2000);

					for(int i=0; i<=3; i++)
					{
						welement.sendKeys(Keys.ARROW_DOWN);
						Thread.sleep(1000);
					}

					welement.sendKeys(Keys.ENTER);
					System.out.println("Executed Successfully");
					Thread.sleep(3000);	
					break;

				case "rate" :
				case "submit" :

					System.out.println(command+" Target element - "+element);

					for(int i=0;i<Integer.parseInt(value);i++)
					{
						Thread.sleep(1100);
						welement = driver.findElement(by);
						if(welement != null)
						{	
							System.out.println("Executed Successfully");
							break;
						}
					}
					if(welement == null)
					{
						System.out.println("Execution Failed");
					}
					break;

				}
				cmd_row = cmd_row+1;

			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}		
				
		
	/*}*/
	
	@AfterSuite
	public void tearDownTest()
	{
		Boolean Scrprint = false;
//		if(Scrprint){
//			System.out.println("Screen Shot Taken !!");
//		}
//		else
//		{
//			System.out.println("Unable to take screen shot !!");
//		}
//		
		System.out.println("Test Executed..");
		driver.quit();
		
	}
	
	public boolean verifytextpresent(String value)
	{
		
		boolean b = driver.getPageSource().contains(value);
		return b;
		
	}
	
	public boolean verifyelementpresent(By by)
	{
		WebElement element = null;
		try
		{
			element = driver.findElement(by);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(element == null)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
	
	public boolean verifyvalue()
	{
		return false;
	}
	
	
	public WebElement tryJavaScript(String element, String elementtype)
	{
		WebElement welement = null;
		
		if(elementtype.equalsIgnoreCase("id"))
		{
			welement = (WebElement) ((JavascriptExecutor) driver).executeScript("document.getElementById('"+element+"')");
			
		}
		else if(elementtype.equalsIgnoreCase("xpath"))
		{
			welement = (WebElement) ((JavascriptExecutor) driver).executeScript("$x('"+element+"')");
			
		}
				
		if(welement == null)
		{
			System.out.println("Web Element is null.");
		}
		return welement;
	}
	
	
	public void declareFailed(String command, String element)
	{
		Assert.assertTrue(false, "Command -> "+command+" : Over "+element+" Failed..");
	}
	
	public void declarePassed(String command, String element)
	{
		Assert.assertTrue(true, "Command -> "+command+" : Over "+element+" Passed..");
	}
	
}
