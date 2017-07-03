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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

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
	public void sampleTest()
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
					
					try
					{
						System.out.println(command+" "+value+" into "+element);
						welement = driver.findElement(by);
						welement.clear();
						welement.sendKeys(value);
							
					}
					catch(Exception e)
					{
						System.out.println("Execution Failed.");
						e.printStackTrace();
					}
					
					
					break;
					
				case "click" :
					
					try
					{
						
						System.out.println(command+" on "+element);
						welement = driver.findElement(by);
						
						Actions actions = new Actions(driver);
						//actions.moveToElement(welement).click().perform();
						actions.moveToElement(welement).click(welement).perform();
//						welement.click(); 
						System.out.println("Executed Successfully.");
						
						
					}
					catch(Exception e)
					{
						System.out.println("Executed Failed.");
						e.printStackTrace();
					}
					break;
				
				case "select" :
											
					try
					{
						welement = driver.findElement(by);
						Select dropdown = new Select(welement);
						System.out.println(command+" "+value+" from "+dropdown.getOptions());
						dropdown.selectByVisibleText(value);
						System.out.println("Executed Successfully.");
						driver.findElement(by).sendKeys(Keys.TAB);
						Thread.sleep(2000);
					} 
					catch (Exception e1)
					{
						System.out.println("Execution Failed.");
						e1.printStackTrace();
					}
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
					
					try
					{
						System.out.println(command+" on "+element);
						welement = wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
						welement.click();
						Thread.sleep(5000);
						System.out.println("Executed Successfully.");
						
					}
					catch(Exception e)
					{
						System.out.println("Execution Failed.");
						e.printStackTrace();
					}
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
//					
//					case "SelectFrame" :
//						
//						//Syntax should be like - 
//						//SelectFrame A MenuSelect A
//						// SelectFrame	A Framemain A
//						
//						if(element.contains("FrameMain"))
//						{
//							driver.switchTo().defaultContent();
//							driver.switchTo().frame(element);
//							/**
//							 * Defines Logic to navigate to PageContainerFrame if FrameMain is selected
//							 **/
//							driver.switchTo().frame("PageContainerFrame");	
//							CurrentFrame = "PageContainerFrame"; 
//							
//						}
//						else if(element.contains("MenuSelect"))
//						{
//							driver.switchTo().defaultContent();
//							driver.switchTo().frame(element);
//							CurrentFrame = element;
//							
//						}
//						else if(element.contains("relative=up"))
//						{
//							((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
//						}
//						
//						else if(CurrentFrame.equalsIgnoreCase("PageContainerFrame"))
//						{
//							driver.switchTo().frame(element);
//							CurrentFrame = element;
//						}
						
						
						
					
//						//1	MenuSelect	-	Indexes
//						//2	FrameMain	-	Main Frame for content
//						//3	PageContainerFrame	-	Pages
//						//4	default frame
//						
//						
//						/*if(element.contains("PageContainerFrame"))
//						{
//							driver.switchTo().frame(element);
//						}
//						else if(element.contains("MenuSelect"))
//						{
//							driver.switchTo().frame(element);
//						}
//						else
//						{
//							driver.switchTo().defaultContent();
//							driver.switchTo().frame(element);
//						}*/
//						
//					try {
//						if(element.contains("default"))
//						{
//							System.out.println(command+" Frame : "+element);
//							driver.switchTo().defaultContent();
//							
//							Thread.sleep(3000);
//						
//						}
//						else
//						{
//							/*System.out.println("Switch to element :" + element);
//							by =  By.id(element);
//							WebElement el = driver.findElement(by);
//							
//							System.out.println("Find by element :" + el.getText() );*/
//							System.out.println(command+" Frame : "+element);
//							driver.switchTo().frame(element);
//							System.out.print("Executed Succssfully.");
//							Thread.sleep(3000);
//						}
//					} catch (Exception e2) {
//						System.out.println("Executed Failed.");
//						
//						e2.printStackTrace();
//					}
//						break;
						
					case "SwitchtoFrame" :
						
						System.out.println(command+" to - "+element);
						
						try
						{
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
							
						}catch(Exception e)
						{
							System.out.println("Execution Failed.");
							e.printStackTrace();
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
	}		
				
		
	/*}*/
	
	@AfterSuite
	public void tearDownTest()
	{
		Boolean Scrprint = false;
		Scrprint = config.takeScreenShot(Result);
		if(Scrprint){
			System.out.println("Screen Shot Taken !!");
		}
		else
		{
			System.out.println("Unable to take screen shot !!");
		}
		
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
	
	
}
