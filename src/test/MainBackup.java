package test;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainBackup {
	static BufferedReader br;
	static BufferedWriter bw;
	static WebDriver driver;
	
	static String dataFile = "C:\\Users\\user\\Desktop\\Project\\Apt 2021.csv";
	static String resultFile = "C:\\Users\\user\\Desktop\\Project\\Result.csv";
	static String chromeDriverFile = "C:\\Selenium\\chromedriver.exe";
	
	static final int REVIEWS_MIN = 50; //�����Ϳ� �ֱ����� �ּ� ���䰹�� ����
	static final int KEYWORDS_MIN = 10; //�����Ϳ� �ֱ����� �ּ� ��ġ Ű���� ����
	static final int PRICE_INFO_MIN = 5; // �ּ� 5���̻��� ����Ʈ ���� �����Ͱ� �־����
	
	// ������, ������ Ű�����
	static ArrayList<String> positive = new ArrayList<String>(Arrays.asList("���ƿ�", "����", "���׿�", "�����ϴ�", "������", " ���ؿ�", " ����", "�Ƹ��ٿ�", "����", "�ְ���", "�ְ��Դϴ�", "�����ݴϴ�", "���ϰ�", "���ؿ�", "���մϴ�", "������", "������", "������", "�ƴ�", "����", "ȯ����", "�ò���", "�ò�����", "����", "���޴ϴ�"));
	static ArrayList<String> negative = new ArrayList<String>(Arrays.asList("�����ؿ�", "�����մϴ�", "���Դϴ�", "���̿���", "������", "���޴ϴ�", "����", "�� ��", "������", "���� ��", "�־��", "�����ؿ�", "�����մϴ�", "����ȭ", "�ɰ���", "�ɰ��մϴ�", "�ɰ��ؿ�", "��������", "������", "�ò�������", "�ò�������", "�����ؿ�", "�����մϴ�", "�������", "��ƽ��ϴ�", "�־���", "�־���", "�־��̿���", "������", "������", "�����ؿ�", "��������", "���� ����", "��Ʈ����", "����", "����", "����"));
	
	static String line;
	static int lineCnt = 0;
	static String url;
	static String address = "";
	static boolean passOrNot;
	static String[] array;
	static int reviewNumber;
	static int numOfReviews;
	static float priceIncrease;
	static int recordedAddressCount = 0;
	
	static final int POSITIVE_NEW = 0; // �ֱ� 1�Ⱓ ������ Ű���� �Ѱ��� (�ε���)
	static final int NEGATIVE_NEW = 1; // �ֱ� 1�Ⱓ ������ Ű���� �Ѱ��� (�ε���)
	static final int POSITIVE_OLD = 2; // 5���̻� ������ ������ Ű���� �Ѱ��� (�ε���)
	static final int NEGATVIE_OLD = 3; // 5���̻� ������ ������ Ű���� �Ѱ��� (�ε���)
	static int[] numberOfKeywords = new int[4]; // �� 4���� ���� ������ �迭
	
	//////////// Ŭ��
	private static void click(String a) {
		driver.findElement(By.xpath(a)).click();
	}
	
	//////////// �ؽ�Ʈ ��������
	private static String getText(String a) {
		String text = driver.findElement(By.xpath(a)).getText();
		return text;
	}
	
	//////////// �α���
	private static void login() {
		try {
			System.out.println("�α��� ��...");
			driver.get("https://hogangnono.com/");
			Thread.sleep(800);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[2]/div[2]/a[2]")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[3]/a")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[1]/div[2]/span[3]/a")).click();
			Thread.sleep(300);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[2]/form/div[1]/input")).sendKeys("01038877826");
			Thread.sleep(300);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[2]/form/div[2]/input")).sendKeys("abcdefghij");
			Thread.sleep(300);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[2]/form/a")).click();
			Thread.sleep(1000);
			System.out.println("�α��� �Ϸ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//////////// ���� ���������� ���° ���� �а��ִ��� ���, ���ڿ��� �迭�� �ֱ�
	private static void CurrentLineToArray() {
		lineCnt++;
		System.out.print("line" + lineCnt + "... ");
		line = line.replace("\"", "");
		array = line.split(",");
	}
	
	//////////// ���ϴ� ������ŭ ������ ���� �ǳʶ�
	private static void skip(int a) {
		for (int i = 0; i < a; i++) {
			try {
				br.readLine();
				lineCnt++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//////////// ��������� ù�ٱ��
	private static void writeLabels() {
		try {
			bw.write("�ּ�,�ų� ����������(%/year),���� ����(������/��ü),���� ������,���� ������,�ֱ� ����(������/��ü),�ֱ� ������,�ֱ� ������\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//////////// �ߺ��Ǵ� �ּҵ� ��ŵ
	private static void checkDuplicate() {
		if ((array[0] + " " + array[1]).equals(address)) {
			address = (array[0] + " " + array[1]);
			System.out.println("�ߺ��Ǵ� �ּ�");
			passOrNot = true;
		} else {
			address = (array[0] + " " + array[1]);
			passOrNot = false;
		}
	}
	
	//////////// �ּҿ���
	private static boolean openAddress() throws InterruptedException {
			driver.get("https://www.google.com");
			Thread.sleep(500);
			driver.findElement(By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input")).sendKeys(address + " site:hogangnono.com" + Keys.ENTER); //���۰˻�
			Thread.sleep(1000);
			
			if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[1]/div/div[1]/div/a/h3")).size() > 0) { //ù��° �˻���� Ŭ���ϴµ�
				click(url);
			} else if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[11]/div/div[2]/div[2]/div/div/div[1]/div/div[1]/div/a/h3")).size() > 0) { //url�� �װ����� �����Ƿ� �����ϳ� Ŭ��
				click(url);
			} else if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[2]/div/div[1]/div/a/h3")).size() > 0) {
				click(url);
			} else if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div/div/div/div[1]/div/a/h3")).size() > 0) {
				click(url);
			} else { //�˻������ �ϳ��� ������� �н�
				System.out.println("�˻���� ����");
				return passOrNot = true;
			}
		
			Thread.sleep(1000);
			return passOrNot = false;
	}
	
	//////////// ���䰹���� �˻�
	private static void checkReviewNumbers() throws InterruptedException {
		try {
			click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/fieldset/div[4]/ul/li[3]/a"); //����Ŭ��
		} catch (org.openqa.selenium.NoSuchElementException e) { //����Ŭ���� �ȵȴٸ� ȣ����뿡 �������� ���°��̹Ƿ� �н�
			System.out.println("������ ����");
			passOrNot = true;
			return;
		}
		Thread.sleep(2000);
		try {
			numOfReviews = Integer.parseInt(getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/fieldset/div[4]/ul/li[3]/a")); //���䰹�� ��������
			if (numOfReviews < REVIEWS_MIN) { //���� ������ 50�� �̸��ΰ�� �н�
				System.out.println("���� ���� ����");
				passOrNot = true;
				return;
			}
		} catch (NumberFormatException e) { //���䰡 0���� ��� Integer��� "�̾߱�"��� String�� �޾�����, �̷���� �н�
			System.out.println("���� ���� ����");
			passOrNot = true;
			return;
		}
		
		passOrNot = false;
	}
	
	
	//////////// ���ο� �� �ݱ�
	private static void closeUnwantedWindow() {
			if (driver.getWindowHandles().size() > 1) {
				for(String windowHandle : driver.getWindowHandles()){ // �ǹٲٰ�
				    driver.switchTo().window(windowHandle);
				}
				
				driver.close(); // �ݰ�
				
				for(String windowHandle : driver.getWindowHandles()){ // �ٽ� ����������
				    driver.switchTo().window(windowHandle);
				}
			}
	}
	
	//////////// �ֱ�,���� �ŸŰ���Ȯ��
	private static float getPriceIncrease() throws InterruptedException {
		
		// �ɶ����� ������ Ŭ�� �ݺ�
		while(true) {
			try {
				click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/div/div[1]/div[4]/div[2]/div[3]/a/div");
				Thread.sleep(500);
			} catch (NoSuchElementException e) {
				break;
			}
		}
		
		//���� �ֱٰ��ݰ� ��¥ ����
		String priceNewString = getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[2]");
		int priceNewYear = Integer.parseInt((getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[1]").split("\\."))[0]);
		//���� �������ݰ� ��¥ ����
		int urlIndex = 0;
		String priceOldString = "";
		int priceOldYear = 0;
		while(true) {
			try {
				urlIndex++;
				getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(urlIndex) + "]/td[2]");
			} catch (NoSuchElementException e) {
				priceOldString = getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(urlIndex - 1) + "]/td[2]");
				priceOldYear = Integer.parseInt((getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(urlIndex - 1) + "]/td[1]").split("\\."))[0]);
				break;
			}
		}
		
		// �ּ� 3���̻� ���� ������ �־�� �Ѵ�.
		if (priceNewYear - priceOldYear < PRICE_INFO_MIN) {
			System.out.println("���� ���� ����");
			passOrNot = true;
			return 0;
		}
		
		// 9�� 5,000 ���¸� 95000���� ��ȯ
		priceNewString = priceNewString.replace("��", "0000").replace(",", "");
		float priceNew = 0;
		String[] arrayTemp = priceNewString.split(" ");
		int i = 0;
		for (i = 0; i < arrayTemp.length; i++) {
			priceNew += Integer.parseInt(arrayTemp[i]);
		}
		
		priceOldString = priceOldString.replace("��", "0000").replace(",", "");
		float priceOld = 0;
		arrayTemp = priceOldString.split(" ");
		for (i = 0; i < arrayTemp.length; i++) {
			priceOld += Integer.parseInt(arrayTemp[i]);
		}
		
		//���簡���� �������� ��� �ų� ��� ��% �ö��°�
		float priceIncrease = (float) Math.pow((priceNew / priceOld), 1.0 / (priceNewYear - priceOldYear)); // ������ ���
		priceIncrease = (float) ((Math.round(1000 * priceIncrease) / 10.0) - 100); //100�� ���ؼ� %������ �ٲٰ�, �Ҽ��� ���ڸ����� �ݿø�
		
		passOrNot = false;
		return priceIncrease;
	}
	
	//////////// ����Ȯ��
	public static void countKeywords() throws InterruptedException {
		String reviewText = "";
		for (reviewNumber = 2; reviewNumber <= numOfReviews + 1; reviewNumber++) { // ������� ������ ����
			url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(reviewNumber) + "]/div[1]/div[2]"; 
			
			try {
				click(url + "/a"); // �� ����� �������ư Ŭ��
				Thread.sleep(100);
			} catch (NoSuchElementException e) {
	
			} catch (StaleElementReferenceException e) {
				continue;
			}
			
			closeUnwantedWindow();
	
			while (true) {
				try {
					reviewText = getText(url); // ���� �ؽ�Ʈ�� �ҷ���
					break;
				} catch (NoSuchElementException e) {
					try {
						click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/a"); // �Ⱥҷ������� �ؿ� ��� �� �ε��ϴ� ��ư Ŭ��
					} catch (StaleElementReferenceException f) {
			
					}
					
					while (driver.findElements(By.xpath(url)).size() == 0) { // �ε��ɶ����� ��ٸ�
						Thread.sleep(500);
					}
				}
			}
			
			url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(reviewNumber) + "]/div[1]/div[3]";
			String time = (getText(url).split("\n"))[0]; //time�� ���� ���䰡 �������� ������. (e.g. "8�� ��")
			
			if (time.contains("�� ��") || time.contains("�� ��") || time.contains("�� ��") || time.contains("�Ϸ� ��") || time.contains("�ð� ��")) { // recent posts
				for (int j = 0; j < positive.size(); j++) { // ������ Ű���� ������ ��ΰ�
					if (reviewText.contains(positive.get(j))) {
						numberOfKeywords[POSITIVE_NEW]++;
					}
				}
				for (int j = 0; j < negative.size(); j++) { // ������ Ű���� ������ ��ΰ�
					if (reviewText.contains(negative.get(j))) {
						numberOfKeywords[NEGATIVE_NEW]++;
					}
				}
			} else if (!(time.contains("�ϳ�") || time.contains("2��") || time.contains("3��") || time.contains("4��"))) { //old posts (more than 5 years)
				for (int j = 0; j < positive.size(); j++) { // ������ Ű���� ������ ��ΰ�
					if (reviewText.contains(positive.get(j))) {
						numberOfKeywords[POSITIVE_OLD]++;
					}
				}
				for (int j = 0; j < negative.size(); j++) { // ������ Ű���� ������ ��ΰ�
					if (reviewText.contains(negative.get(j))) {
						numberOfKeywords[NEGATVIE_OLD]++;
					}
				}
			}
		}
		
		//�ش�Ǵ� Ű���� ������ �ʹ� ������ ����Ȯ�ϴٰ� �Ǵ��ؼ� �н���
		if ((numberOfKeywords[POSITIVE_NEW] + numberOfKeywords[NEGATIVE_NEW] < KEYWORDS_MIN) || (numberOfKeywords[POSITIVE_OLD] + numberOfKeywords[NEGATVIE_OLD] < KEYWORDS_MIN)) {
			System.out.println("Ű���� ���� ����");
			passOrNot = true;
		} else {
			passOrNot = false;
		}		
	}
	
	//////////// ���Ͽ� ��� �ۼ�
	private static void writeResult() {
		try {
			bw.write(address + "," + priceIncrease + "%,"); //�ּ�, %�� �ٲ� ���ݻ����(�Ҽ��� 1�ڸ�)
			
			bw.write((float) (Math.round(1000 * (float)numberOfKeywords[POSITIVE_OLD] / (float)(numberOfKeywords[POSITIVE_OLD] + numberOfKeywords[NEGATVIE_OLD])) / 10.0) + "%,"); //���� ����, %�� �ٲ��� �Ҽ���1�ڸ����� �ݿø�
			bw.write(numberOfKeywords[POSITIVE_OLD] + "," + numberOfKeywords[NEGATVIE_OLD] + ","); //���� ������ Ű���� ��, ������ Ű���� ��
			
			bw.write((float) (Math.round(1000 * (float)numberOfKeywords[POSITIVE_NEW] / (float)(numberOfKeywords[POSITIVE_NEW] + numberOfKeywords[NEGATIVE_NEW])) / 10.0) + "%,"); //�ֱ� ����, %�� �ٲ��� �Ҽ���1�ڸ����� �ݿø�
			bw.write(numberOfKeywords[POSITIVE_NEW] + "," + numberOfKeywords[NEGATIVE_NEW] + ",");  //�ֱ� ������ Ű���� ��, ������ Ű���� ��
			bw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		recordedAddressCount++;
		System.out.println(" ****** " + recordedAddressCount + " ��° �ּ� �ۼ��� ******");
	}
	
	//////////// ��������, ����޽��� ���
	private static void wrapUp() {
		try {
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Finished");	
	}
	
	//////////// ����
	public static void main(String[] args) {
		try {
			br = new BufferedReader(new FileReader(dataFile)); // ������ ����Ʈ ���
			bw = new BufferedWriter(new FileWriter(resultFile)); // ����� �Է��� ����
			System.setProperty("webdriver.chrome.driver", chromeDriverFile);
			driver = new ChromeDriver(); // ũ�� ����̹�
			
			login(); //�α���
			writeLabels(); // ��������� ù�� ���
			skip(16); // ���ϴ� �����Ͱ� 17��° �ٺ��� �����ϹǷ� ������ ù 16���� ��ŵ��
			
			while ((line = br.readLine()) != null) { //�������Ͽ��� �ּһ̾ƿͼ� ȣ����뿡�� ������������ Result���Ͽ� ���, �װ� �ݺ�
				CurrentLineToArray();
				
				checkDuplicate(); // �ߺ��ּ����� �˻�
				if (passOrNot == true) {
					continue;
				}
				
				openAddress(); // �ּҸ� ���۰˻��ؼ� ����Ʈ�� ����. ������ ���� �ּҷ�
				if (passOrNot == true) {
					continue;
				}
				
				checkReviewNumbers(); // ���䰹���� �˻�
				if (passOrNot == true) {
					continue;
				}
				
				priceIncrease = getPriceIncrease();
				if (passOrNot == true) {
					continue;
				}
				
				countKeywords();
				if (passOrNot == true) {
					continue;
				}
				
				writeResult();
			}
			
			wrapUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}

