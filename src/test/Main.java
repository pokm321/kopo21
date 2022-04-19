package test;
 
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
	static WebDriver driver;
	static int REVIEWS_MIN = 50; //�����Ϳ� �ֱ����� �ּ� ���䰹�� ����
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
	private void login() {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//////////// �ֱ�,���� �ŸŰ���Ȯ��
	private float getPriceIncrease() {
		
		// �ɶ����� ������ Ŭ�� �ݺ�
		while(true) {
			try {
				click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/div/div[1]/div[4]/div[2]/div[3]/a/div");
				Thread.sleep(1000);
			} catch (org.openqa.selenium.NoSuchElementException e) {
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//���� �ֱٰ��ݰ� ��¥ ����
		String priceNewString = getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[2]");
		int priceNewYear = Integer.parseInt((getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[1]").split("\\."))[0]);
		//���� �������ݰ� ��¥ ����
		int i = 0;
		String priceOldString = "";
		int priceOldYear = 0;
		while(true) {
			try {
				i++;
				priceOldString = getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(i) + "]/td[2]");
				priceOldYear = Integer.parseInt((getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(i) + "]/td[1]").split("\\."))[0]);
			} catch (org.openqa.selenium.NoSuchElementException e) {
				break;
			}
		}
		
		if (priceNewYear == priceOldYear) {
			return 0;
		}
		
		// 9�� 5,000 ���¸� 95000���� ��ȯ
		priceNewString = priceNewString.replace("��", "0000").replace(",", "");
		float priceNew = 0;
		String[] arrayTemp = priceNewString.split(" ");
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
		return priceIncrease;
	}
	
	//////////// ����
	public static void main(String[] args) {
		try {
			Main main = new Main();
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\Project\\Apt 2021.csv")); // ������ ����Ʈ ���
			File file = new File("C:\\Users\\user\\Desktop\\Project\\Result.csv"); // ����� �Է��� ����
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
			driver = new ChromeDriver();
			
			// ������, ������ Ű�����
			ArrayList<String> positive = new ArrayList<String>(Arrays.asList("���ƿ�", "����", "���׿�", "�����ϴ�", "������", " ���ؿ�", " ����", "�Ƹ��ٿ�", "����", "�ְ���", "�ְ��Դϴ�", "�����ݴϴ�", "���ϰ�", "���ؿ�", "���մϴ�", "������", "������", "������", "�ƴ�", "����", "ȯ����", "�ò���", "�ò�����", "����", "���޴ϴ�"));
			ArrayList<String> negative = new ArrayList<String>(Arrays.asList("�����ؿ�", "�����մϴ�", "���Դϴ�", "���̿���", "������", "���޴ϴ�", "����", "�� ��", "������", "���� ��", "�־��", "�����ؿ�", "�����մϴ�", "����ȭ", "�ɰ���", "�ɰ��մϴ�", "�ɰ��ؿ�", "��������", "������", "�ò�������", "�ò�������", "�����ؿ�", "�����մϴ�", "�������", "��ƽ��ϴ�", "�־���", "�־���", "�־��̿���", "������", "������", "�����ؿ�", "��������", "���� ����", "��Ʈ����", "����", "����", "����"));
			
			bw.write("�ּ�,�ų� ����������(%),���� ����(������ ����),���� ������Ű����,���� ������Ű����,�ֱ�1�Ⱓ ����(������ ����),�ֱ�1�Ⱓ ������Ű���� ��,�ֱ�1�Ⱓ ������Ű���� ��\n"); //��������� ù�ٱ��
			
			main.login(); //�α���
			
			String line;
			int skip = 0;
			int lineCnt = 0;
			int cnt = 0;
			String address = "";
			int reviews;
			String url;
			while ((line = br.readLine()) != null && cnt < 2) { //�������Ͽ��� �ּһ̾ƿͼ� ȣ����뿡�� ������������ Result���Ͽ� ���, �װ� �ݺ�
				lineCnt++;
				System.out.println(lineCnt + "...");
				if (skip < 248){ //skips the first 16 lines which have no data
					skip++;
					continue;
				}
				System.out.println();
				line = line.replace("\"", "");
				String[] array = line.split(",");	
				
				if ((array[0] + " " + array[1]).equals(address)) { //�ߺ��Ǵ� �ּҵ� ��ŵ
					continue;
				}
				address = (array[0] + " " + array[1]);
				
				
				//////////////////////////////////////////////////////////////////�ּҿ���
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
					continue;
				}
				Thread.sleep(1000);
				try {
					click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/fieldset/div[4]/ul/li[3]/a"); //����Ŭ��
				} catch (org.openqa.selenium.NoSuchElementException e) { //����Ŭ���� �ȵȴٸ� ȣ����뿡 �������� ���°��̹Ƿ� �н�
					continue;
				}
				Thread.sleep(2000);
				try {
					reviews = Integer.parseInt(getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/fieldset/div[4]/ul/li[3]/a")); //���䰹�� ��������
					if (reviews < REVIEWS_MIN) { //���䰹���� 50�� �̸��ΰ�� �н�
						continue;
					}
				} catch (NumberFormatException e) { //���䰡 0���� ��� Integer��� "�̾߱�"��� String�� �޾�����, �̷���� �н�
					continue;
				}
				
				float PriceIncrease = main.getPriceIncrease();
				if (PriceIncrease == 0) {
					continue;
				}
				
				////////////////////////////////////////////////////////////////////����Ȯ��
				
				Robot r = new Robot();
				r.mouseMove(600, 700);
				Thread.sleep(100);
				r.mousePress(InputEvent.BUTTON2_DOWN_MASK);
				Thread.sleep(100);
				r.mouseMove(600, 1000);
				url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(reviews + 1) + "]/div[1]/div[2]";	//���ϾƷ������ url	
				while (true) { //���� �Ʒ������ ���ö����� ��ũ���ؼ� ��� �ε���Ŵ
					try {
						getText(url);
						break;
					} catch (Exception e) {
						Thread.sleep(500);
					}
				}
				r.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
				
				int PNew = 0; // �ֱ� 1�Ⱓ ������ Ű���� �Ѱ���
				int NNew = 0; // �ֱ� 1�Ⱓ ������ Ű���� �Ѱ���
				int POld = 0; // 5���̻� ������ ������ Ű���� �Ѱ���
				int NOld = 0; // 5���̻� ������ ������ Ű���� �Ѱ���
				for (int i = reviews + 1; i > 1; i--) { // ������� ������ ����
					url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(i) + "]/div[1]/div[2]";
						try {
							click(url + "/a"); // ������ ������ ������
							Thread.sleep(100);
						} catch (Exception e) {
							
						}
					String textReview = driver.findElement(By.xpath(url)).getText(); // ���� �ؽ�Ʈ�� �ҷ���
					
					url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(i) + "]/div[1]/div[3]";
					String time = (getText(url).split("\n"))[0]; //time�� ���� ���䰡 �������� ������. (e.g. "8�� ��")
					
					if (time.contains("�� ��") || time.contains("�� ��") || time.contains("�� ��") || time.contains("�Ϸ� ��") || time.contains("�ð� ��")) { //recent posts
						for (int j = 0; j < positive.size(); j++) { // ������ Ű���� ������ ��ΰ�
							if (textReview.contains(positive.get(j))) {
								PNew++;
							}
						}
						for (int j = 0; j < negative.size(); j++) { // ������ Ű���� ������ ��ΰ�
							if (textReview.contains(negative.get(j))) {
								NNew++;
							}
						}
					} else if (!(time.contains("�ϳ�") || time.contains("2��") || time.contains("3��") || time.contains("4��"))) { //old posts (more than 5 years)
						for (int j = 0; j < positive.size(); j++) { // ������ Ű���� ������ ��ΰ�
							if (textReview.contains(positive.get(j))) {
								POld++;
							}
						}
						for (int j = 0; j < negative.size(); j++) { // ������ Ű���� ������ ��ΰ�
							if (textReview.contains(negative.get(j))) {
								NOld++;
							}
						}
					}
				}
				
				////////////////////////////////////////////////////////////////////prints the results
				bw.write(address + "," + PriceIncrease + "%,"); //�ּ�, %�� �ٲ� ���ݻ����(�Ҽ��� 1�ڸ�)
				
				if (POld == 0 && NOld == 0) {
					bw.write("No data,");
				} else {
					bw.write((float) (Math.round(1000 * (float)POld / (float)(POld + NOld)) / 10.0) + "%,"); //���� ����, %�� �ٲ��� �Ҽ���1�ڸ����� �ݿø�
				}
				bw.write(POld + "," + NOld + ","); //���� ������ Ű���� ��, ������ Ű���� ��
				
				if (PNew == 0 && NNew == 0) {
					bw.write("No data,");
				} else {
					bw.write((float) (Math.round(1000 * (float)PNew / (float)(PNew + NNew)) / 10.0) + "%,"); //�ֱ� ����, %�� �ٲ��� �Ҽ���1�ڸ����� �ݿø�
				}
				bw.write(PNew + "," + NNew + ",");  //�ֱ� ������ Ű���� ��, ������ Ű���� ��
				bw.write("\n");
				cnt++;
				System.out.println("****** " + cnt + " ��° �ּ� �ۼ��� ******\n");
			}
			bw.close();
			System.out.println("Finished");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}

