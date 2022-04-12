#include <stdio.h>

const float RATE_USD = 1233.1; //달러환율 
const float RATE_JPY = 9.87; //엔화환율
const float RATE_EUR = 1342.51; //유로환율
const float RATE_CNY = 193.5; //위안환율 
const float RATE_GBP = 1603.15; //파운드환율 
int inputWon, output, change;

//줄 거스름돈과 그 지폐갯수 출력 함수 
int ChangesToGive (int a) { // a는 거스름돈change
	// 줄 거스름돈 출력 
	printf("거스름돈: %d원 ---> 1000원:%d,  ", a, a / 1000);
	//줄 지폐 갯수 출력 
	a = a % 1000;
	printf("500원:%d,  ", a / 500);
	a = a % 500;
	printf("100원:%d,  ", a / 100);
	a = a % 100;
	printf("50원:%d,  ", a / 50); 
	a = a % 50;
	printf("10원:%d,  \n", a / 10);
	return 0; 
}

//환전된 돈과 그 지폐갯수 출력함수
int BillsToGive(int a, char *unit, int bill_1, int bill_2, int bill_3, int bill_4, int bill_5) { //(줄돈output, 화폐, 지폐단위 1,2,3,4,5) 
	// "달러 : 40달러" 같은 식으로 출력 
	printf("%s : %d%s ---> ", unit, a, unit);
	//지폐 동전 갯수 출력 
	printf("%d%s:%d,  ", bill_1, unit, a / bill_1);
	a = a % bill_1;
	printf("%d%s:%d,  ", bill_2, unit, a / bill_2);
	a = a % bill_2;
	printf("%d%s:%d,  ", bill_3, unit, a / bill_3);
	a = a % bill_3;
	printf("%d%s:%d,  ", bill_4, unit, a / bill_4);
	a = a % bill_4;
	printf("%d%s:%d,  \n", bill_5, unit, a / bill_5);
	return 0; 
}

// 줄돈 output과 거스름돈change를 계산하는 함수 
int exchange(float a) { //a는 환율
	printf("기준 환율 : %.2f\n환전 결과\n", a);
	output = inputWon / a; //환전한 돈 계산 
	change = (int)(inputWon - output * a) / 10 * 10; //거스름돈 계산 
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
		exchange(RATE_USD);
		BillsToGive(output, "달러", 100, 50, 20, 5, 1);
	} else if (currency == 2) {
		exchange(RATE_JPY); // 2:JPY
		BillsToGive(output, "엔", 5000, 1000, 100, 10, 1);
	} else if (currency == 3) { // 3:EUR
		exchange(RATE_JPY);
		BillsToGive(output, "유로", 100, 50, 20, 5, 1);
	} else if (currency == 4) { // 4.CNY
		exchange(RATE_JPY);
		BillsToGive(output, "위안", 100, 50, 20, 5, 1);
	} else if (currency == 5) { // 5:GBP
		exchange(RATE_JPY);
		BillsToGive(output, "파운드", 50, 20, 10, 5, 1);
	}
	
	ChangesToGive(change); 
	return 0;
}  
