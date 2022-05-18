package test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
 
public class Test2 {
	static WebDriver driver;
	
	public static void main(String[] args) throws InterruptedException, AWTException {
		
		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
		driver = new ChromeDriver();
		
		try {
			driver.get("https://naver.com/");
			Thread.sleep(300);
			System.out.println(driver.getWindowHandle());
			driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div/div[1]/h1/a[1]")).sendKeys(Keys.CONTROL + "\n");
			
			for(String currentWindow : driver.getWindowHandles()){
			    driver.switchTo().window(currentWindow);
			}
			
			System.out.println(driver.getWindowHandle());
			System.out.println("opened");
			Thread.sleep(3000);
			System.out.println(driver.getWindowHandles().size());
			if (driver.getWindowHandles().size() > 1) {
				driver.close();
				System.out.println("closed");
			}
			
			for(String currentWindow : driver.getWindowHandles()){
			    driver.switchTo().window(currentWindow);
			}
			System.out.println(driver.getWindowHandle());
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("finished");
	}
	
	
	
	
	
}








//bw.write("");
//bw.close();
//driver.findElement(By.xpath("")).click();
//String text = driver.findElement(By.xpath("")).getText();
//driver.findElement(By.xpath("")).sendKeys("abc");