#include <stdio.h>

const float RATE_USD = 1233.1; //달러환율 
const float RATE_JPY = 9.87; //엔화환율
const float RATE_EUR = 1342.51; //유로환율
const float RATE_CNY = 193.5; //위안환율 
const float RATE_GBP = 1603.15; //파운드환율 
int inputWon, output, change;

// 환전금액(output)과 거스름돈(change)을 계산하는 함수 
int exchange(float a, char *unit) { //a는 환율, unit은 화폐단위 
	printf("\n기준 환율 : %.2f KRW/%s\n\n환전 결과\n", a, unit); //기준환율 출력 
	output = inputWon / a; //환전금액 계산 
	change = (int)(inputWon - output * a) / 10 * 10; //거스름돈 계산 
	return 0;
}

// 거스름돈(change)과 그 지폐갯수 출력 함수 
int ChangesToGive () {
	// 거스름돈 출력 
	printf("거스름돈: %d원 ---> 1000원:%d,  ", change, change / 1000);
	// 지폐갯수 출력 
	change = change % 1000;
	printf("500원:%d,  ", change / 500);
	change = change % 500;
	printf("100원:%d,  ", change / 100);
	change = change % 100;
	printf("50원:%d,  ", change / 50); 
	change = change % 50;
	printf("10원:%d\n", change / 10);
	return 0; 
}

// 환전금액(output)과 지폐갯수 출력 함수
int BillsToGive(char *unit, int bill_1, int bill_2, int bill_3, int bill_4, int bill_5) { // (화폐, 지폐단위 1,2,3,4,5) 
	// 환전금액 출력 (e.g. "달러 : 40달러")
	printf("%s : %d%s ---> ", unit, output, unit);
	// 지폐갯수 출력 
	printf("%d%s:%d,  ", bill_1, unit, output / bill_1);
	output = output % bill_1;
	printf("%d%s:%d,  ", bill_2, unit, output / bill_2);
	output = output % bill_2;
	printf("%d%s:%d,  ", bill_3, unit, output / bill_3);
	output = output % bill_3;
	printf("%d%s:%d,  ", bill_4, unit, output / bill_4);
	output = output % bill_4;
	printf("%d%s:%d\n", bill_5, unit, output / bill_5);
	return 0; 
}



int main() {
	int currency;
	
	printf("환전을 원하는 금액을 입력하세요(원화) : ");
	scanf("%d", &inputWon);	//가져온 원화 입력 
	
	do {
		printf("환전할 외화를 선택하세요 (1:USD, 2:JPY, 3:EUR 4.CNY, 5:GBP) : ");
		scanf("%d", &currency); // 외화 선택 입력 
	} while (currency < 1 || currency > 5); //입력값이 1~5일때 넘어감 

	if (currency == 1) { // 1:USD
		exchange(RATE_USD, "USD");
		BillsToGive("달러", 100, 50, 20, 5, 1);
	} else if (currency == 2) {
		exchange(RATE_JPY, "JPY"); // 2:JPY
		BillsToGive("엔", 5000, 1000, 100, 10, 1);
	} else if (currency == 3) { // 3:EUR
		exchange(RATE_EUR, "EUR");
		BillsToGive("유로", 100, 50, 20, 5, 1);
	} else if (currency == 4) { // 4.CNY
		exchange(RATE_CNY, "CNY");
		BillsToGive("위안", 100, 50, 20, 5, 1);
	} else if (currency == 5) { // 5:GBP
		exchange(RATE_GBP, "GBP");
		BillsToGive("파운드", 50, 20, 10, 5, 1);
	}
	
	ChangesToGive(); 
	return 0;
}  
