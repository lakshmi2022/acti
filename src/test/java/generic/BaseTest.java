package generic;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	public WebDriver driver;
	public WebDriverWait wait;
	public static ExtentReports extent;
	public ExtentTest test;
	
	static {
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		System.setProperty("wedriver.gecko.driver", "./driver/geckodriver.exe");
		WebDriverManager.chromedriver().setup();
		WebDriverManager.firefoxdriver().setup();
	}
	
	@BeforeSuite
	public void createReport() {
		extent = new ExtentReports("target/Spark.html");
		
	}
	
	@AfterSuite
	public void publishReport() {
		extent.flush();
	}
	
	@BeforeMethod
	public void openApp(Method testMethod) throws MalformedURLException {
		test = extent.startTest(testMethod.getName());
		String grid = Utils.getProperty("./config.properties", "USEGRID");
		String remoteURL = Utils.getProperty("./config.properties", "REMOTEURL");
		String browser = Utils.getProperty("./config.properties", "BROWSER");
		String eto = Utils.getProperty("./config.properties", "ETO");
		String ito = Utils.getProperty("./config.properties", "ITO");
		System.out.println(browser);
		if(grid.equals("YES")) {
			DesiredCapabilities capability = new DesiredCapabilities();
			capability.setBrowserName(browser);
			driver = new RemoteWebDriver(new URL(remoteURL), capability);
		}else {
			if(browser.equals("chrome")) {
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--remote-allow-origins=*");
				test.log(LogStatus.INFO, "Open Browser");
				driver = new ChromeDriver();
			}else {
				driver = new FirefoxDriver();
			}
		}
		
		test.log(LogStatus.INFO, "Maximize browser");
		driver.manage().window().maximize();
		
		test.log(LogStatus.INFO, "Set implicit timeout");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.valueOf(ito)));
		
		test.log(LogStatus.INFO, "Set explicit timeout");
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.valueOf(eto)));
		
		test.log(LogStatus.INFO, "enter URL");
		driver.get("https://demo.actitime.com/login.do");		
	}
	
	@AfterMethod
	public void closeApp(ITestResult result) throws IOException {
		
		String tcName = result.getName();
		int res = result.getStatus();
		if(res==1) {
			test.log(LogStatus.PASS, "Pass");
		}else {
			String errMsg = result.getThrowable().getMessage();
//			TakesScreenshot t = (TakesScreenshot)driver;
//			File src = t.getScreenshotAs(OutputType.FILE);
//			FileUtils.copyFile(src, new File("./target/"+tcName+".png"));
			String htmlcode = test.addScreenCapture(tcName+".png");
			test.log(LogStatus.FAIL, htmlcode);
			test.log(LogStatus.FAIL, errMsg);
			
		}
		System.out.println(res);
		
		test.log(LogStatus.INFO, "close browser");
		driver.close();
	}

}
