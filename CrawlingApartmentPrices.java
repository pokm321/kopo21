package test;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class CrawlingApartmentPrices {
	static BufferedReader br;
	static BufferedWriter bw;
	static WebDriver driver;
	
	static String dataFile = "C:\\Eclipse-workspace\\Apt 2021.csv";
	static String resultFile = "C:\\Eclipse-workspace\\Result.csv";
	static String chromeDriverFile = "C:\\Eclipse-workspace\\chromedriver.exe";
	
	static final int REVIEWS_MIN = 50; //데이터에 넣기위한 최소 리뷰갯수 기준
	static final int KEYWORDS_MIN = 10; //데이터에 넣기위한 최소 일치 키워드 개수
	static final float PRICE_INFO_MIN = 5.0f; // 최소 5년이상의 아파트 가격 데이터가 있어야함
	
	// 긍정적, 부정적 키워드들
	static ArrayList<String> positive = new ArrayList<String>(Arrays.asList("좋아요", "좋음", "좋네요", "좋습니다", "좋으며", " 편해요", " 편함", "아름다운", "멋진", "최고에요", "최고입니다", "끝내줍니다", "편리하고", "편리해요", "편리합니다", "조용하", "조용해", "조용합", "아늑", "저평가", "환상적", "시끄럽", "시끄러워", "예뻐", "예쁩니다"));
	static ArrayList<String> negative = new ArrayList<String>(Arrays.asList("불편해요", "불편합니다", "흠입니다", "흠이에요", "나빠요", "나쁩니다", "안좋", "안 좋", "좋지못", "좋지 못", "멀어요", "쾌적해요", "쾌적합니다", "노후화", "심각함", "심각합니다", "심각해요", "주차문제", "주차난", "시끄러워요", "시끄럽습니", "부족해요", "부족합니다", "어려워요", "어렵습니다", "최악입", "최악임", "최악이에요", "끔찍합", "끔찍함", "끔찍해요", "층간소음", "층간 소음", "스트레스", "고평가", "심함", "심합"));
	
	static String line;
	static int lineCnt = 0;
	static String url;
	static String address = "";
	static String addressToSearch = "";
	static boolean passOrNot;
	static String[] lineArray;
	static int reviewNumber;
	static int numOfReviews;
	static float priceIncrease;
	static int recordedAddressCount = 0;
	
	static final int POSITIVE_NEW = 0; // 최근 1년간 긍정적 키워드 총갯수 (인덱스)
	static final int NEGATIVE_NEW = 1; // 최근 1년간 부정적 키워드 총갯수 (인덱스)
	static final int POSITIVE_OLD = 2; // 5년이상 오래된 긍정적 키워드 총갯수 (인덱스)
	static final int NEGATVIE_OLD = 3; // 5년이상 오래된 부정적 키워드 총갯수 (인덱스)
	static int[] numberOfKeywords = new int[4]; // 위 4개의 값을 저장할 배열
	
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
	private static void login() {
		try {
			System.out.println("로그인 중...");
			driver.get("https://hogangnono.com/");
			Thread.sleep(800);
			click("/html/body/div[2]/div/div[1]/div[2]/div[2]/a[2]");
			Thread.sleep(500);
			click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[3]/a");
			Thread.sleep(500);
			click("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[1]/div[2]/span[3]/a");
			Thread.sleep(300);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[2]/form/div[1]/input")).sendKeys("01038877826");
			Thread.sleep(300);
			driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[2]/form/div[2]/input")).sendKeys("abcdefghij");
			Thread.sleep(300);
			click("/html/body/div[2]/div/div[2]/div[2]/div/div/div/div[2]/form/a");
			Thread.sleep(1000);
			click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/a[1]/span");
			System.out.println("로그인 완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//////////// 현재 엑셀파일의 몇번째 줄을 읽고있는지 출력, 문자열을 배열에 넣기
	private static void CurrentLineToArray() {
		lineCnt++;
		System.out.print("line" + lineCnt + "... ");
		line = line.replace("\"", "");
		lineArray = line.split(",");
	}
	
	//////////// 원하는 갯수만큼 문서의 줄을 건너뜀
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
	
	//////////// 결과파일의 첫줄기록
	private static void writeLabels() {
		try {
			bw.write("주소,매년 가격증가율(%/year),옛날 여론(긍정적/전체),옛날 긍정적,옛날 부정적,최근 여론(긍정적/전체),최근 긍정적,최근 부정적\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//////////// 중복되는 주소들 스킵
	private static void checkDuplicate() throws InterruptedException {
		if ((lineArray[0] + " " + lineArray[1]).equals(address)) { // 중복되는 경우
			address = (lineArray[0] + " " + lineArray[1]);
			System.out.println("중복되는 주소");
			passOrNot = true;
		} else { // 중복안되는 경우
			addressToSearch = (lineArray[0].split(" ")[2] + " " + lineArray[4]); // 검색할 주소 (아파트이름으로)
			address = (lineArray[0] + " " + lineArray[1]); // 지번 주소
			passOrNot = false;
		}
		Thread.sleep(500);
	}
	
	//////////// 주소열기
	private static boolean openAddress() throws InterruptedException {
		String test = "abcd1234";
		if (driver.findElements(By.xpath(url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/fieldset/div[1]/div/input")).size() > 0) {
			driver.findElement(By.xpath(url)).sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // 검색
			driver.findElement(By.xpath(url)).sendKeys(addressToSearch + Keys.ENTER);
		} else if (driver.findElements(By.xpath(url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[2]/fieldset/div/div[1]/input")).size() > 0) {
			driver.findElement(By.xpath(url)).sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // 검색
			driver.findElement(By.xpath(url)).sendKeys(addressToSearch + Keys.ENTER);
		} else if (driver.findElements(By.xpath(url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/fieldset/div[1]/div/input")).size() > 0) {
			driver.findElement(By.xpath(url)).sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // 검색
			driver.findElement(By.xpath(url)).sendKeys(addressToSearch + Keys.ENTER);
		}
		
		Thread.sleep(2000);
		click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div[1]/div[1]/ul/li[1]/a/div[1]");
		Thread.sleep(2000);
		
		
		if (getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/fieldset/div[3]").equals(address)) {
			return passOrNot = false;
		} else {
			System.out.println("주소검색 실패");
			return passOrNot = true;
		}
	}
	
	//////////// 리뷰갯수를 검사
	private static void checkReviewNumbers() throws InterruptedException {
		try {
			click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/fieldset/div[4]/ul/li[3]/a"); //리뷰클릭
		} catch (org.openqa.selenium.NoSuchElementException e) { //리뷰클릭이 안된다면 호갱노노에 페이지가 없는것이므로 패스
			System.out.println("페이지 없음");
			passOrNot = true;
			return;
		}
		Thread.sleep(2000);
		try {
			numOfReviews = Integer.parseInt(getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/fieldset/div[4]/ul/li[3]/a")); //리뷰갯수 가져오기
			if (numOfReviews < REVIEWS_MIN) { //리뷰 개수가 50개 미만인경우 패스
				System.out.println("리뷰 개수 부족");
				passOrNot = true;
				return;
			}
		} catch (NumberFormatException e) { //리뷰가 0개인 경우 Integer대신 "이야기"라는 String이 받아지고, 이럴경우 패스
			System.out.println("리뷰 개수 부족");
			passOrNot = true;
			return;
		}
		
		passOrNot = false;
	}
	
	//////////// 새로운 탭 닫기
	private static void closeUnwantedWindow() {
		if (driver.getWindowHandles().size() > 1) {
			for(String windowHandle : driver.getWindowHandles()){ // 탭바꾸고
			    driver.switchTo().window(windowHandle);
			}
			
			driver.close(); // 닫고
			
			for(String windowHandle : driver.getWindowHandles()){ // 다시 원래탭으로
			    driver.switchTo().window(windowHandle);
			}
		}
	}
	
	//////////// 최근,옛날 매매가격확인
	private static float getPriceIncrease() throws InterruptedException {
		
		// 될때까지 더보기 클릭 반복
		while(true) {
			try {
				click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[1]/div/div/div[1]/div[4]/div[2]/div[3]/a/div");
				Thread.sleep(500);
			} catch (NoSuchElementException e) {
				break;
			}
		}
		
		//제일 최근가격과 날짜 저장
		String priceNewString = getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[2]");
		String url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[1]/td[1]";
		float priceNewYear = Integer.parseInt((getText(url).split("\\."))[0]) + Integer.parseInt((getText(url).split("\\."))[1]) / 12;
		//제일 옛날가격과 날짜 저장
		int urlIndex = 0;
		String priceOldString = "";
		float priceOldYear = 0;
		while(true) {
			try {
				urlIndex++;
				getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(urlIndex) + "]/td[2]");
			} catch (NoSuchElementException e) {
				priceOldString = getText("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(urlIndex - 1) + "]/td[2]");
				url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div/div/div/div[1]/div[4]/div[2]/div[3]/table/tbody/tr[" + Integer.toString(urlIndex - 1) + "]/td[1]";
				priceOldYear = Integer.parseInt((getText(url).split("\\."))[0]) + Integer.parseInt((getText(url).split("\\."))[1]) / 12;
				break;
			}
		}
		
		// 최소 3년이상 가격 정보가 있어야 한다.
		if (priceNewYear - priceOldYear < PRICE_INFO_MIN) {
			System.out.println("가격 정보 부족");
			passOrNot = true;
			return 0;
		}
		
		// 9억 5,000 형태를 95000으로 변환
		priceNewString = priceNewString.replace("억", "0000").replace(",", "");
		float priceNew = 0;
		String[] arrayTemp = priceNewString.split(" ");
		int i = 0;
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
		
		passOrNot = false;
		return priceIncrease;
	}
	
	//////////// 리뷰확인
	public static void countKeywords() throws InterruptedException {
		String reviewText = "";
		for (reviewNumber = 2; reviewNumber <= numOfReviews + 1; reviewNumber++) { // 리뷰관련 데이터 수집
			url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(reviewNumber) + "]/div[1]/div[2]"; 
			
			try {
				click(url + "/a"); // 긴 댓글의 더보기버튼 클릭
				Thread.sleep(100);
			} catch (NoSuchElementException e) {
	
			} catch (StaleElementReferenceException e) {
				continue;
			}
			
			closeUnwantedWindow();
	
			while (true) {
				try {
					reviewText = getText(url); // 리뷰 텍스트를 불러옴
					break;
				} catch (NoSuchElementException e) {
					try {
						click("/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/a"); // 안불러와지면 밑에 댓글 더 로드하는 버튼 클릭
					} catch (StaleElementReferenceException f) {
			
					}
					
					while (driver.findElements(By.xpath(url)).size() == 0) { // 로딩될때까지 기다림
						Thread.sleep(500);
					}
				}
			}
			
			url = "/html/body/div[2]/div/div[1]/div[1]/div[3]/div/div[4]/div[2]/div[2]/div/div[1]/div/div[" + Integer.toString(reviewNumber) + "]/div[1]/div[3]";
			String time = (getText(url).split("\n"))[0]; //time은 언제 리뷰가 적혔는지 가져옴. (e.g. "8달 전")
			
			if (time.contains("달 전") || time.contains("주 전") || time.contains("일 전") || time.contains("하루 전") || time.contains("시간 전")) { // recent posts
				for (int j = 0; j < positive.size(); j++) { // 긍정적 키워드 갯수가 몇개인가
					if (reviewText.contains(positive.get(j))) {
						numberOfKeywords[POSITIVE_NEW]++;
					}
				}
				for (int j = 0; j < negative.size(); j++) { // 부정적 키워드 갯수가 몇개인가
					if (reviewText.contains(negative.get(j))) {
						numberOfKeywords[NEGATIVE_NEW]++;
					}
				}
			} else if (!(time.contains("일년") || time.contains("2년") || time.contains("3년") || time.contains("4년"))) { //old posts (more than 5 years)
				for (int j = 0; j < positive.size(); j++) { // 긍정적 키워드 갯수가 몇개인가
					if (reviewText.contains(positive.get(j))) {
						numberOfKeywords[POSITIVE_OLD]++;
					}
				}
				for (int j = 0; j < negative.size(); j++) { // 부정적 키워드 갯수가 몇개인가
					if (reviewText.contains(negative.get(j))) {
						numberOfKeywords[NEGATVIE_OLD]++;
					}
				}
			}
		}
		
		//해당되는 키워드 개수가 너무 적으면 부정확하다고 판단해서 패스함
		if ((numberOfKeywords[POSITIVE_NEW] + numberOfKeywords[NEGATIVE_NEW] < KEYWORDS_MIN) || (numberOfKeywords[POSITIVE_OLD] + numberOfKeywords[NEGATVIE_OLD] < KEYWORDS_MIN)) {
			System.out.println("키워드 개수 부족");
			passOrNot = true;
		} else {
			passOrNot = false;
		}		
	}
	
	//////////// 파일에 결과 작성
	private static void writeResult() {
		try {
			bw.write(address + "," + priceIncrease + "%,"); //주소, %로 바꾼 가격상승율(소수점 1자리)
			
			bw.write((float) (Math.round(1000 * (float)numberOfKeywords[POSITIVE_OLD] / (float)(numberOfKeywords[POSITIVE_OLD] + numberOfKeywords[NEGATVIE_OLD])) / 10.0) + "%,"); //옛날 여론, %로 바꾼후 소수점1자리까지 반올림
			bw.write(numberOfKeywords[POSITIVE_OLD] + "," + numberOfKeywords[NEGATVIE_OLD] + ","); //옛날 긍정적 키워드 수, 부정적 키워드 수
			
			bw.write((float) (Math.round(1000 * (float)numberOfKeywords[POSITIVE_NEW] / (float)(numberOfKeywords[POSITIVE_NEW] + numberOfKeywords[NEGATIVE_NEW])) / 10.0) + "%,"); //최근 여론, %로 바꾼후 소수점1자리까지 반올림
			bw.write(numberOfKeywords[POSITIVE_NEW] + "," + numberOfKeywords[NEGATIVE_NEW] + ",");  //최근 긍정적 키워드 수, 부정적 키워드 수
			bw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		recordedAddressCount++;
		System.out.println(" ****** " + recordedAddressCount + " 번째 주소 작성함 ******");
	}
	
	//////////// 파일종료, 종료메시지 출력
	private static void wrapUp() {
		try {
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Finished");	
	}
	
	//////////// 메인
	public static void main(String[] args) {
		try {
			br = new BufferedReader(new FileReader(dataFile)); // 가져올 아파트 목록
			bw = new BufferedWriter(new FileWriter(resultFile)); // 결과를 입력할 파일
			System.setProperty("webdriver.chrome.driver", chromeDriverFile);
			driver = new ChromeDriver(); // 크롬 드라이버

			login(); //로그인
			writeLabels(); // 결과파일의 첫줄 기록
			skip(16); // 원하는 데이터가 17번째 줄부터 시작하므로 문서의 첫 16줄을 스킵함
			
			while ((line = br.readLine()) != null) { //엑셀파일에서 주소뽑아와서 호갱노노에서 데이터추출후 Result파일에 기록, 그걸 반복
				CurrentLineToArray();
				
				checkDuplicate(); // 중복주소인지 검사
				if (passOrNot == true) {
					continue;
				}
				
				openAddress(); // 주소를 검색, 해당 아파트의 페이지 열기
				if (passOrNot == true) {
					continue;
				}
				
				checkReviewNumbers(); // 리뷰갯수를 검사
				if (passOrNot == true) {
					continue;
				}
				
				priceIncrease = getPriceIncrease(); // 연간 가격상승률 조사
				if (passOrNot == true) {
					continue;
				}
				
				countKeywords(); // 키워드 개수 조사
				if (passOrNot == true) {
					continue;
				}
				
				writeResult(); // 결과를 파일에 입력
			}
			
			wrapUp(); // 종료
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}