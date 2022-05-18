package test;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
 
public class Base {
	
	public static void main(String[] args) {
		// ID, PATH ����
		String DRIVER_ID = "webdriver.chrome.driver";
		String DRIVER_PATH = "C:\\Selenium\\chromedriver.exe";
		
		System.setProperty(DRIVER_ID, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		// ������ url
		String base_url = "https://www.google.com";
		try{
			// ����
			driver.get(base_url);
			// �������� ���ϴ�.
			System.out.println(driver.getPageSource());
			// �±��߿� ù��° ã����
			WebElement webElement = driver.findElement(By.name("q"));
			// Ű�� üũ
			webElement.sendKeys("����");
			// ������ ������ (����)
			webElement.submit();
			
//			driver.findElement(By.xpath("")) <-fullXpath�ּ� �ֱ� 
			
			
			//�ؽ�Ʈ ��������
			String str = driver.findElement(By.xpath("/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[1]/div/div/div/div[2]/span/div[1]")).getText();
			
			System.out.println(str);
			
			
			driver.findElement(By.xpath("html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[2]/div/div/div/div/div/div/div[1]/a/h3")).click();
 
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}