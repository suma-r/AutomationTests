public class Keywords{

public static WebDriver driver;
	
	@BeforeTest
	public void openBrowser(){
		System.setProperty("webdriver.chrome.driver", "/Users/sumar/Documents/workspace/Test/src/Driver/chromedriver");
		 driver=new ChromeDriver();
		 driver.manage().window().maximize();
		 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	}
	@Test
	public void login() throws InterruptedException {
 String expectURL= "https://web.flock.co/?";
		 driver.get(expectURL);
		String actual = driver.getCurrentUrl();
		 System.out.println(actual);
		 if(expectURL.equals(actual))
		 {
			 System.out.println("Success....!!!");
		 }
		 //int var_loc_size= driver.findElements(By.xpath("//*[@id='widgets_InputBox_0']/input")).size();
		 //System.out.println(var_loc_size);
		 //driver.findElements(By.xpath("//*[@id='widgets_InputBox_0']/input")).get(var_loc_size-1).click();
		 //new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='widgets_InputBox_0']/input"))).sendKeys("sumar@flock.co");
		Thread.sleep(2000);
		boolean s1 = driver.findElement(By.xpath("//input[@type='email']")).isDisplayed();
		System.out.println(s1);
		 
		driver.findElement(By.cssSelector("input[class='input']")).sendKeys("sumar@flock.co");
		 driver.findElement(By.xpath("//*[@id='uniqName_15_0']/div[1]/div[2]/div/a")).click();
		 }
	@AfterTest
	public void closebrowser()
	{
		try{
		    driver.quit();
		   }catch (Exception e){
		      System.out.println("print on console" +e.getMessage());
		      }
}
	}


