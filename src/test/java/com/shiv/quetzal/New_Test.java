package com.shiv.quetzal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Listeners({ItestListner_Impl.class})
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
		testscriptpath = new File("C:/Sel_Frmwork/TestScripts/"+testscriptname+".xlsx");
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
		
		try {
			driver.get(environment);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver,60);		
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
						
					try
					{
						System.out.println(command+" "+value);
						driver.navigate().to(value);
						System.out.println("Executed Successfully.");
						
					}catch(Exception e)
					{
						System.out.println("Execution Failed.");
						e.printStackTrace();
					}
						
						break;
						
					case "type" :
						
					
							System.out.println(command+" "+value+" into "+element);
							welement = driver.findElement(by);
							welement.clear();
							welement.sendKeys(value);
						break;
						
					case "javascript" :
						
						System.out.println("Executing Javascript Code -> "+element);
						((JavascriptExecutor) driver).executeScript(element);
						System.out.println("Executed Successfully..");
						break;
						
					case "click" :
						
							System.out.println(command+" on "+element);
						try {
							welement = driver.findElement(by);
						} catch (Exception e2) {
							
							if(welement == null)
							{
								welement = findBYJavascript(element);
							
								if(welement == null)
								{
									System.out.println("Execution Failed.");
									e2.printStackTrace();
									declarefailed();
								}
							}
							
							
							
						}
							
						welement.click();
						System.out.println("Executed Successfully.");
							
						break;
					
					case "select" :
						    welement = driver.findElement(by);
							Select dropdown = new Select(welement);
							System.out.println(command+" "+value+" from "+dropdown.getOptions());
							dropdown.selectByVisibleText(value);
							System.out.println("Executed Successfully.");
							driver.findElement(by).sendKeys(Keys.TAB);
							Thread.sleep(2000);
						break;
					
					case "END" :
						
						System.out.println("Test Complete ..");
						flag = false;
						break;
					
					case "pause" :
						try 
						{
							int time  = Integer.parseInt(value);
							System.out.println(command+" for "+value);
							Thread.sleep(time);
							System.out.println("Executed Successfully");
						}
						catch (NumberFormatException | InterruptedException e)
						{
							System.out.println("Execution Failed.");
							e.getMessage();
						}
									
						break;	
					
					case "clickandwait" :
							System.out.println(command+" on "+element);
							welement = wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
							welement.click();
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
						try
						{
							Assert.assertTrue(flag, "VerifyTextPresent Failed. Text '/"+value+"'/ , does not present.");
						
						}
						catch (Exception e)
						{
							System.out.println("Execution Failed.");
							System.out.println("Text is not on page..!!");
						}
						break;
						
						
					case "verifyelementpresent" : 
						
										
						try
						{
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
							Assert.assertTrue(flag, "WEB ELEMENT Doest NOT Found :"+element);
							
						}catch(Exception e)
						{
							System.out.println("Execution Failed.");
							e.printStackTrace();
						}
						
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
							
								if(element.equalsIgnoreCase("default"))
								{
									driver.switchTo().defaultContent();
									CurrentFrame = "default";
									System.out.println("Frame now -"+CurrentFrame);
										
								}
								else
								{
									driver.switchTo().frame(element);
									CurrentFrame = element;
									System.out.println("Frame now -"+CurrentFrame);
								}
								System.out.println("Executed Successfully");
									
							break;
							
						case "HandleFrame" :
							
							System.out.println("Handle Frame");
							
						try 
						{
							welement = driver.findElement(by);
						}
						catch (Exception e1) {
						
							driver.switchTo().defaultContent();
							List<WebElement> frames = driver.findElements(By.tagName("frame"));
							
							
							e1.printStackTrace();
						}
							
							
						break;
							
						case "SwitchtoWindow" :
						case "SwitchtoDefault" :
							
							
							if(command.equalsIgnoreCase("SwitchtoWindow"))
							{
								System.out.println(command+" to - "+element);
								currentwindow = driver.getWindowHandle();
								Set<String> openwindows = driver.getWindowHandles();
								try
								{
									for(String testwin : openwindows)
									{
										driver.switchTo().window(testwin);
										if(driver.getTitle().equalsIgnoreCase(element))
										{
											System.out.println("Executed Successfully");
											
											
											break;
										}
									}
								}
								catch(Exception e)
								{
									System.out.println("Execution Failed");
									e.printStackTrace();
								}
								
							
							}
							else if(command.equalsIgnoreCase("SwitchtoDefault")) 
							{
								driver.switchTo().window(currentwindow);
								
							}
							
							break;
							
						case "Scrolldown" :
							
							((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
							
							break;
									
						case "Selectfromsuggestion" :
							
							try
							{
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
								
							}
							catch(Exception e)
							{
								System.out.println("Execution Failed");
								e.getMessage();
							}
							
						break;
						
						case "rate" :
						case "submit" :
							
							System.out.println(command+" Target element - "+element);
					
							for(int i=0;i<Integer.parseInt(value);i++)
							{
								try
								{
									Thread.sleep(1100);
									welement = driver.findElement(by);
									if(welement != null)
									{	
										System.out.println("Executed Successfully");
										break;
									}
								}
								catch(Exception e)
								{
									
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
		} catch (Exception e) {
			System.out.println("Main Catch Called");
			e.printStackTrace();
		}
	}		
				
		
	/*}*/
	
	@AfterSuite
	public void tearDownTest()
	{

		System.out.println("Test Execution complete.. \nClosing driver object.");
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
	
	public WebElement findBYJavascript(String element)
	{
		WebElement welement = null;
		//It only Supports ID. For Xpath and CSS or anyelse it does not work.
		welement = (WebElement) ((JavascriptExecutor) driver).executeScript(""
				+ "function findElementById(elementID, frame)"
				+ "{ "
				+ "var element;"
				+ "if(frame)"
				+ "{ element = frame.document.getElementById(elementID);"
				+ "}"
				+ "else{"
				+ "element = document.getElementById(elementID);"
				+ "	}"
				+ "	if(element){"
				+ "	return element; "
				+ "}"
				+ "var frames;"
				+ "	var arr_frames;"
				+ "	var arr_iframe;"
				+ "	if(frame){"
				+ "	arr_frames = frame.document.getElementsByTagName('frame');"
				+ "	arr_iframe = frame.document.getElementsByTagName('iframe');"
				+ "	}"
				+ "	else{"
				+ "		arr_frames = document.getElementsByTagName('frame');"
				+ "		arr_iframe = document.getElementsByTagName('iframe');	"
				+ "	}"
				+ "	for(var i =0; i < arr_frames.length; i++){"
				+ "		var frame = arr_frames[i].contentWindow;"
				+ "		element = findElementById(elementID, frame);"
				+ "		if(element){"
				+ "			return element;"
				+ "		}"
				+ "}"
				+ "	for(var i =0; i < arr_iframe.length; i++){"
				+ "		var frame = arr_iframe[i].contentWindow;"
				+ "		element = findElementById(elementID, frame);"
				+ "		if(element){"
				+ "			return element;"
				+ "		}"
				+ "	}	return null;"
				+ "} "
				+ "findElementById('"+element+"');");
		
		if(welement == null)
		{
			System.out.print("web element -> "+element+" is null");
		}
		
		return welement;
	}
	
	public void declarefailed()
	{
		Assert.assertTrue(false);
	}
	
}
