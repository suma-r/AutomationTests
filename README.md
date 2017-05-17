

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.google.common.base.Function;
import com.prakat.Helper.Xls_Reader;

public class BaseClass 
{	
	public static Properties config = null;
	public static Properties OR = null;
	public static WebDriver wbDv = null;
	public static EventFiringWebDriver driver = null;
	public static boolean loggedIn = false;
	public static Xls_Reader datatable = null;
	public static Logger APP_Logger = Logger.getLogger("MyLogger");	
	public static String strControllerPath;
	
	/**This method is used to initiate the Driver - Configuration file, Object Repository, Browser type
	 * 
	 * @param strORFileName The Name of the Object Repository with the (.properties) extension
	 * @exception Exception: If any exception is found	
	 */
	public static void initiateDriver(String strORFileName, String loadConfigFile){
		
		try{			
			// loading all the configuration values
			config = loadConfigFile(loadConfigFile);
			
			// loading the Object Repository file
			OR = loadObjectRepositoryFile(strORFileName);
			
			//Assert.assertTrue(loadController(), "The Controller Excel Sheet is not loaded successfully");
			
			if(!config.equals(null) && !OR.equals(null)){
				
				// checking the type of browser
				if(config.getProperty("browserType").equalsIgnoreCase("Chrome")){
					
					System.setProperty("webdriver.chrome.driver","/Users/sumar/Documents/workspace/AutomationFramework/src/com/prakat/Driver/chromedriver.exe");
					
					wbDv = new ChromeDriver();
					
				}else if(config.getProperty("browserType").equalsIgnoreCase("Firefox")){
					
					wbDv = new FirefoxDriver();
					
				}else if(config.getProperty("browserType").equalsIgnoreCase("IE")){
					//System.setProperty("webdriver.ie.driver", "D:\\WorkingFolder\\WorkSpace\\WebDriver\\IEDriverServer.exe");
					System.getProperty("webdriver.ie.driver", "D:\\udaya\\WorkSpace\\lib\\IEDriverServer.exe");
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability("ignoreZoomSetting", true);
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					wbDv = new InternetExplorerDriver();
					
				}else if(config.getProperty("browserType").equalsIgnoreCase("Firefox_Profile")){
					
					File profiler = new File(System.getProperty("user.dir")+"\\src\\com\\late\\rooms\\firefoxProfile\\Test.FirefoxProfile");
					FirefoxProfile profile = new FirefoxProfile(profiler);
					wbDv = new FirefoxDriver(profile);
				}
				
				driver = new EventFiringWebDriver(wbDv);
				
				// putting an implicit wait after every Action or Event
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
				// opening the browser
				driver.navigate().to(getValueFromPropertyFile(config, "testSiteURL"));
				driver.manage().window().maximize();
				
			}else{		
				
				logging("Either config file or OR is returning null");
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		    logging("The initiateDriver function is failing");
			Assert.fail("The initiateDriver function is failing");
		}		
	}
	
	/**This method is used to load the Object Repository file.
	 * 
	 * 
	 * @param strORFileName The Name of the Object Repository with the (.properties) extension
	 * @return Properties it will return the instance of property file after loading
	 * @exception IOException: If there is any Input/Output Exception is found
	 */
	public static Properties loadObjectRepositoryFile(String strORFileName){
		
		try{
			OR = new Properties();   
			if(isFilePresent(System.getProperty("user.dir")+"\\src\\com\\prakat\\Config\\"+strORFileName)){
				
				FileInputStream fp = new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\prakat\\Config\\" + strORFileName);
				OR.load(fp);			
				
				logging("The Object Repository file "+ strORFileName +" is successfully loaded");
				
			}else{				
				
				logging("The Object Repository file "+ strORFileName +" loading is failed");
			}
			
		}catch(IOException strInputOutputException){
			
			logging("IOException occured in loading the Configuration file");
			Assert.fail("IOException occured in loading the Configuration file");
			
		}catch(Exception e){
			
			e.printStackTrace();			
			logging("Some Exception occured in loading the Configuration file");
			Assert.fail("Some Exception occured in loading the Configuration file");
		}	
		return OR;
	}
	
	/**This method is used to load the Configuration file.
	 * 
	 * @ @param The name of the configuration file
	 * @return Properties it will return the instance of property file after loading
	 * @exception IOException: If there is any Input/Output Exception is found
	 */
	public static Properties loadConfigFile(String loadConfigFile){
		
		try{
			config = new Properties();
			if(isFilePresent(System.getProperty("user.dir")+"\\src\\com\\prakat\\Config\\"+loadConfigFile)){
				
				FileInputStream fp = new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\prakat\\Config\\"+loadConfigFile);
				config.load(fp);
				
				logging("The configuration file is successfully loaded");
				
			}else{				
				
				logging("The configuration file loading is failed");
				
			}
			
		}catch(IOException strInputOutputException){				
			
			logging("IOException occured in loading the Configuration file");
			Assert.fail("IOException occured in loading the Configuration file");
			
		}catch(Exception e){
			
			e.printStackTrace();			
			logging("Some Exception occured in loading the Configuration file");
			Assert.fail("Some Exception occured in loading the Configuration file");
		}
		
		return config;
	}
	
	/**This method is used to load the Controller.xls file.
	 * @param loadController The name of the controller file
	 * @return True/False (True if the Controller.xlsx is loaded successfully, false otherwise)
	 * @exception Exception: If there is any Exception is found
	 */
	public static boolean loadController(String loadController){
		
		try{
			String strControllerPath = System.getProperty("user.dir")+"\\src\\com\\prakat\\Config\\"+ loadController; 
			System.out.println("Path>>"+strControllerPath);
			if(isFilePresent(strControllerPath)){
				
				datatable = new Xls_Reader(strControllerPath);
				APP_Logger.debug("The Controller sheet is successfully loaded");
				Reporter.log("The Controller sheet is successfully loaded");
				return true;
				
			}else{
				
				APP_Logger.debug("The Controller sheet loading is failed");
				logging("The Controller sheet loading is failed");
				return false;
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			APP_Logger.debug("Some Exception occured in loading the Configuration file");
			Reporter.log("Some Exception occured in loading the Configuration file");
			Assert.fail("Some Exception occured in loading the Configuration file");
			return false;
		}
	}
	
	/**This method is used to Read data from the OR Property file.
	 * 
	 * 
	 * @param prop The Name of the Property file
	 * @param key The key for which value needs to be fetched
	 * @return String it will return the value against the Key
	 * @exception IllegalArgumentException/NullPointerException/Exception: If there is any exception occurs
	 */
	public static String getValueFromPropertyFile(Properties prop, String key){
		
		try{
			
			return prop.getProperty(key);
			
		}catch(IllegalArgumentException strIllegalArgsException){			
			
			logging(key + " The argument is not legal");
			Assert.fail(key + " Illegal Argument Exception");
			return null;
			
		}catch(NullPointerException strNullPointerException){
			
			logging(key + " is null or not present in the Object Repository");
			Assert.fail(key + " Null pointer Exception");
			return null;
			
		}catch(Exception e){
			
			e.printStackTrace();
			Assert.fail(key + " is throwing Exception");
			return null;
		}	
	}
	
	/**This method is used to check whether the File is present on the path passed as parameter.
	 * 
	 * 
	 * @param key The key for which value needs to be fetched
	 * @param strObjLabel The Object identifier or label displayed in the application
	 * @return True/False (True if the file is present on the path, false otherwise)
	 * @exception Exception: If there is any exception occurs
	 */
	public static boolean isFilePresent(String strFilePath){
		
		try{
			if((strFilePath).trim() == ""){
				
				APP_Logger.debug("The passed file path paramenter is blank");
				Reporter.log("The passed file path paramenter is blank");
				return false;
				
			}else{
				
				File file=new File(strFilePath);
				  boolean exists = file.exists();
				  
				  if(exists) {
					  
					  return true;
					  
				  }else{
					  
					  return false;
				  }
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			return false;	
		}
	}
	
	public static void writeLog(String errorMessage){
		if(config.getProperty("testNgReporterStatus").equalsIgnoreCase("ON")) {
			Reporter.log(errorMessage);
		}
	}
	
	public static void applicationLog(String errorMessage) {
		if(config.getProperty("log4jReporterStatus").equalsIgnoreCase("ON")){
			APP_Logger.debug(errorMessage);
		}	
	}
	
	public static void printStackTraceOnConsole(Exception strExceptionStackTrace){
		  
		  String strStackTrace = getValueFromPropertyFile(config, "CONFIG_PRINT_STACK_TRACE");
		  if(strStackTrace.trim().equalsIgnoreCase("ON")){
		   
		   strExceptionStackTrace.printStackTrace();
		  }
		 }
	
	public static void logging(String errorMessage) {
		//writeLog(errorMessage);
		//applicationLog(errorMessage);
		//printStackTraceOnConsole(null);
		
		}
	
	/**This method is used to pass the input the Value Based on the Object Type
	 
	 * @param objInputField1 The Name of the input value
	 * @exception Exception: If any exception is found	
	 */
	/*public static void fillForm(ArrayList<String []> objInputField1 )
	{
		try
		{
			int intSize = objInputField1.size();
			for(int i=0; i<intSize; i++)
			{
				Assert.assertTrue(ClassSpecificFunction_General(objInputField1.get(i)[0], objInputField1.get(i)[1], objInputField1.get(i)[2]));		
			}
		}
		catch(Exception e)
		{
			BaseClass.logging(null);
		}
	}*/
	
	public enum TestCondition
	 {
		WBtn,WChbox,WEdit,Wlink,CBX,Img,Wlbl;
	 }
	
	/** Main function for input to different objects
     * 
     * @param strXPathKey  The Key to locate the particular object
     * @param strTestData Input value to the object
     * @return True/False (True if the input is successfully passed to object, false otherwise)
     */
	/*public static boolean ClassSpecificFunction_General(String strXPathKey,String strTestData, String strLabelName) throws IOException
	{
		boolean blnFlag = false;
		String [] temp = null;
		temp = strXPathKey.split("_");
		String strObjReference = temp[0];
		switch (TestCondition.valueOf(strObjReference))
		  { 
		      case WEdit:
		       	  blnFlag = TextBoxHelper.type(strXPathKey, strTestData,strLabelName);		       	
		       	  return blnFlag;
		      case WBtn:
		    	  blnFlag = ButtonHelper.clickButton(strXPathKey,strLabelName);
		    	  return blnFlag;		    	  		    
		      case WChbox:
		    	  blnFlag = ComboBoxHelper.clickButton(strXPathKey,strLabelName);
		    	  return blnFlag;
		      case Wlink:
		    	  blnFlag = LinkHelper.click(strXPathKey, strLabelName);
		    	  return blnFlag;
		      case CBX:
		    	  blnFlag = ComboBoxHelper.selectComboBoxItemByValue(strXPathKey, strTestData,strLabelName);
		    	  return blnFlag;		    	  
		      case Img:
		    	  blnFlag = ImageHelper.imagePresent(strXPathKey,strLabelName);
		    	  return blnFlag;
		      case Wlbl:
		      blnFlag = LabelHelper.getText(strXPathKey,strLabelName);
		      
		  }
		return blnFlag;
	}*/
	
	public static WebElement fluentWait(final String waitlocator, int timeOut, int pollingTime){
		String path_locator= BaseClass.OR.getProperty(waitlocator);
 		final String locator= path_locator.split(",")[1];
 		final String identifier=path_locator.split(",")[0];
 		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(BaseClass.driver)
				.withTimeout(timeOut, TimeUnit.SECONDS)
				.pollingEvery(pollingTime, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		WebElement element = wait.until(
				new Function<WebDriver, WebElement>() {
					public WebElement apply(WebDriver driver) {
						if(identifier.equalsIgnoreCase("id")){
							return driver.findElement(By.id(locator));
							}
						else if(identifier.equalsIgnoreCase("xpath")){
							return driver.findElement(By.xpath(locator));
							}
						else{
							logging("OR is returning null");							
							}
						return null;
						}
					}
				);
		return  element;
		};	
	}
