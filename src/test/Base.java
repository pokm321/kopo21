package test;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
 
public class Base {
	
	public static void main(String[] args) {
		// ID, PATH 설정
		String DRIVER_ID = "webdriver.chrome.driver";
		String DRIVER_PATH = "C:\\Selenium\\chromedriver.exe";
		
		System.setProperty(DRIVER_ID, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		// 접속할 url
		String base_url = "https://www.google.com";
		try{
			// 접속
			driver.get(base_url);
			// 페이지로 들어갑니다.
			System.out.println(driver.getPageSource());
			// 태그중에 첫번째 찾은거
			WebElement webElement = driver.findElement(By.name("q"));
			// 키를 체크
			webElement.sendKeys("날씨");
			// 데이터 보내기 (수행)
			webElement.submit();
			
//			driver.findElement(By.xpath("")) <-fullXpath주소 넣기 
			
			
			//텍스트 가져오기
			String str = driver.findElement(By.xpath("/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[1]/div/div/div/div[2]/span/div[1]")).getText();
			
			System.out.println(str);
			
			
			driver.findElement(By.xpath("html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[2]/div/div/div/div/div/div/div[1]/a/h3")).click();
 
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}