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
	static int REVIEWS_MIN = 50; //데이터에 넣기위한 최소 리뷰갯수 기준
	//////////// 클릭
	private static void click(String a) {
		driver.findElement(By.xpath(a)).click();
	}
	
	//////////// 텍스트 가져오기
	private static String getText(String a) {
		String text = driver.findElement(By.xpath(a)).getText();
		return text;
	}
	
	//////////// 로그인
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
	
	//////////// 최근,옛날 매매가격확인
	private float getPriceIncrease() {
		
		// 될때까지 더보기 클릭 반복
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
		
		//제일 최근가격과 날짜 저장
		String priceNewString = getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[2]");
		int priceNewYear = Integer.parseInt((getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[1]").split("\\."))[0]);
		//제일 옛날가격과 날짜 저장
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
		
		// 9억 5,000 형태를 95000으로 변환
		priceNewString = priceNewString.replace("억", "0000").replace(",", "");
		float priceNew = 0;
		String[] arrayTemp = priceNewString.split(" ");
		for (i = 0; i < arrayTemp.length; i++) {
			priceNew += Integer.parseInt(arrayTemp[i]);
		}
		
		priceOldString = priceOldString.replace("억", "0000").replace(",", "");
		float priceOld = 0;
		arrayTemp = priceOldString.split(" ");
		for (i = 0; i < arrayTemp.length; i++) {
			priceOld += Integer.parseInt(arrayTemp[i]);
		}
		
		//현재가격이 옛날가격 대비 매년 평균 몇% 올랐는가
		float priceIncrease = (float) Math.pow((priceNew / priceOld), 1.0 / (priceNewYear - priceOldYear)); // 복리로 계산
		priceIncrease = (float) ((Math.round(1000 * priceIncrease) / 10.0) - 100); //100을 곱해서 %단위로 바꾸고, 소수점 한자리까지 반올림
		return priceIncrease;
	}
	
	//////////// 메인
	public static void main(String[] args) {
		try {
			Main main = new Main();
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\Project\\Apt 2021.csv")); // 가져올 아파트 목록
			File file = new File("C:\\Users\\user\\Desktop\\Project\\Result.csv"); // 결과를 입력할 파일
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
			driver = new ChromeDriver();
			
			// 긍정적, 부정적 키워드들
			ArrayList<String> positive = new ArrayList<String>(Arrays.asList("좋아요", "좋음", "좋네요", "좋습니다", "좋으며", " 편해요", " 편함", "아름다운", "멋진", "최고에요", "최고입니다", "끝내줍니다", "편리하고", "편리해요", "편리합니다", "조용하", "조용해", "조용합", "아늑", "저평가", "환상적", "시끄럽", "시끄러워", "예뻐", "예쁩니다"));
			ArrayList<String> negative = new ArrayList<String>(Arrays.asList("불편해요", "불편합니다", "흠입니다", "흠이에요", "나빠요", "나쁩니다", "안좋", "안 좋", "좋지못", "좋지 못", "멀어요", "쾌적해요", "쾌적합니다", "노후화", "심각함", "심각합니다", "심각해요", "주차문제", "주차난", "시끄러워요", "시끄럽습니", "부족해요", "부족합니다", "어려워요", "어렵습니다", "최악입", "최악임", "최악이에요", "끔찍합", "끔찍함", "끔찍해요", "층간소음", "층간 소음", "스트레스", "고평가", "심함", "심합"));
			
			bw.write("주소,매년 가격증가율(%),옛날 여론(긍정적 비율),옛날 긍정적키워드,옛날 부정적키워드,최근1년간 여론(긍정적 비율),최근1년간 긍정적키워드 수,최근1년간 부정적키워드 수\n"); //결과파일의 첫줄기록
			
			main.login(); //로그인
			
			String line;
			int skip = 0;
			int lineCnt = 0;
			int cnt = 0;
			String address = "";
			int reviews;
			String url;
			while ((line = br.readLine()) != null && cnt < 2) { //엑셀파일에서 주소뽑아와서 호갱노노에서 데이터추출후 Result파일에 기록, 그걸 반복
				lineCnt++;
				System.out.println(lineCnt + "...");
				if (skip < 248){ //skips the first 16 lines which have no data
					skip++;
					continue;
				}
				System.out.println();
				line = line.replace("\"", "");
				String[] array = line.split(",");	
				
				if ((array[0] + " " + array[1]).equals(address)) { //중복되는 주소들 스킵
					continue;
				}
				address = (array[0] + " " + array[1]);
				
				
				//////////////////////////////////////////////////////////////////주소열기
				driver.get("https://www.google.com");
				Thread.sleep(500);
				driver.findElement(By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input")).sendKeys(address + " site:hogangnono.com" + Keys.ENTER); //구글검색
				Thread.sleep(1000);
				if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[1]/div/div[1]/div/a/h3")).size() > 0) { //첫번째 검색결과 클릭하는데
					click(url);
				} else if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[11]/div/div[2]/div[2]/div/div/div[1]/div/div[1]/div/a/h3")).size() > 0) { //url이 네가지로 나뉘므로 넷중하나 클릭
					click(url);
				} else if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div[2]/div/div[1]/div/a/h3")).size() > 0) {
					click(url);
				} else if (driver.findElements(By.xpath(url = "/html/body/div[7]/div/div[10]/div/div[2]/div[2]/div/div/div/div/div/div[1]/div/a/h3")).size() > 0) {
					click(url);
				} else { //검색결과가 하나도 없을경우 패스
					continue;
				}
				Thread.sleep(1000);
				try {
					click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/fieldset/div[4]/ul/li[3]/a"); //리뷰클릭
				} catch (org.openqa.selenium.NoSuchElementException e) { //리뷰클릭이 안된다면 호갱노노에 페이지가 없는것이므로 패스
					continue;
				}
				Thread.sleep(2000);
				try {
					reviews = Integer.parseInt(getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/fieldset/div[4]/ul/li[3]/a")); //리뷰갯수 가져오기
					if (reviews < REVIEWS_MIN) { //리뷰갯수가 50개 미만인경우 패스
						continue;
					}
				} catch (NumberFormatException e) { //리뷰가 0개인 경우 Integer대신 "이야기"라는 String이 받아지고, 이럴경우 패스
					continue;
				}
				
				float PriceIncrease = main.getPriceIncrease();
				if (PriceIncrease == 0) {
					continue;
				}
				
				////////////////////////////////////////////////////////////////////리뷰확인
				
				Robot r = new Robot();
				r.mouseMove(600, 700);
				Thread.sleep(100);
				r.mousePress(InputEvent.BUTTON2_DOWN_MASK);
				Thread.sleep(100);
				r.mouseMove(600, 1000);
				url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(reviews + 1) + "]/div[1]/div[2]";	//제일아래댓글의 url	
				while (true) { //제일 아래댓글이 나올때까지 스크롤해서 모두 로딩시킴
					try {
						getText(url);
						break;
					} catch (Exception e) {
						Thread.sleep(500);
					}
				}
				r.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
				
				int PNew = 0; // 최근 1년간 긍정적 키워드 총갯수
				int NNew = 0; // 최근 1년간 부정적 키워드 총갯수
				int POld = 0; // 5년이상 오래된 긍정적 키워드 총갯수
				int NOld = 0; // 5년이상 오래된 부정적 키워드 총갯수
				for (int i = reviews + 1; i > 1; i--) { // 리뷰관련 데이터 수집
					url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(i) + "]/div[1]/div[2]";
						try {
							click(url + "/a"); // 더보기 있으면 누르고
							Thread.sleep(100);
						} catch (Exception e) {
							
						}
					String textReview = driver.findElement(By.xpath(url)).getText(); // 리뷰 텍스트를 불러옴
					
					url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(i) + "]/div[1]/div[3]";
					String time = (getText(url).split("\n"))[0]; //time은 언제 리뷰가 적혔는지 가져옴. (e.g. "8달 전")
					
					if (time.contains("달 전") || time.contains("주 전") || time.contains("일 전") || time.contains("하루 전") || time.contains("시간 전")) { //recent posts
						for (int j = 0; j < positive.size(); j++) { // 긍정적 키워드 갯수가 몇개인가
							if (textReview.contains(positive.get(j))) {
								PNew++;
							}
						}
						for (int j = 0; j < negative.size(); j++) { // 부정적 키워드 갯수가 몇개인가
							if (textReview.contains(negative.get(j))) {
								NNew++;
							}
						}
					} else if (!(time.contains("일년") || time.contains("2년") || time.contains("3년") || time.contains("4년"))) { //old posts (more than 5 years)
						for (int j = 0; j < positive.size(); j++) { // 긍정적 키워드 갯수가 몇개인가
							if (textReview.contains(positive.get(j))) {
								POld++;
							}
						}
						for (int j = 0; j < negative.size(); j++) { // 부정적 키워드 갯수가 몇개인가
							if (textReview.contains(negative.get(j))) {
								NOld++;
							}
						}
					}
				}
				
				////////////////////////////////////////////////////////////////////prints the results
				bw.write(address + "," + PriceIncrease + "%,"); //주소, %로 바꾼 가격상승율(소수점 1자리)
				
				if (POld == 0 && NOld == 0) {
					bw.write("No data,");
				} else {
					bw.write((float) (Math.round(1000 * (float)POld / (float)(POld + NOld)) / 10.0) + "%,"); //옛날 여론, %로 바꾼후 소수점1자리까지 반올림
				}
				bw.write(POld + "," + NOld + ","); //옛날 긍정적 키워드 수, 부정적 키워드 수
				
				if (PNew == 0 && NNew == 0) {
					bw.write("No data,");
				} else {
					bw.write((float) (Math.round(1000 * (float)PNew / (float)(PNew + NNew)) / 10.0) + "%,"); //최근 여론, %로 바꾼후 소수점1자리까지 반올림
				}
				bw.write(PNew + "," + NNew + ",");  //최근 긍정적 키워드 수, 부정적 키워드 수
				bw.write("\n");
				cnt++;
				System.out.println("****** " + cnt + " 번째 주소 작성함 ******\n");
			}
			bw.close();
			System.out.println("Finished");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}

