#include <stdio.h>

int ChangesToGive(int a) { //int a 는 거스름돈 
	printf("- 거스름돈: %d원 ->  1000원:%d,  ", a, a / 1000);
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

int BillsToGive(int a, int bill_1, int bill_2, int bill_3, int bill_4, int bill_5) { //줄돈, 화페단위 1,2,3,4,5 
	printf("%d:%d,  ", bill_1, a / bill_1);
	a = a % bill_1;
	printf("%d:%d,  ", bill_2,  a / bill_2);
	a = a % bill_2;
	printf("%d:%d,  ", bill_3,  a / bill_3);
	a = a % bill_3;
	printf("%d:%d,  ", bill_4,  a / bill_4);
	a = a % bill_4;
	printf("%d:%d,  \n", bill_5,  a / bill_5);
	return 0; 
}

int main() {
	const float RATE_USD = 1233.1; //달러환율 
	const float RATE_EUR = 1342.51; //유로환율
	const float RATE_YEN = 9.87; //엔화환율
	const float RATE_CNY = 193.5; //위안환율 
	
	int inputWon;
	printf("환전을 원하는 금액을 입력하세요(원화) : ");
	scanf("%d", &inputWon);	
	
	int outputUSD = inputWon / RATE_USD;
	int changeUSD = (int)(inputWon - outputUSD * RATE_USD) / 10 * 10;
	
	int outputEUR = inputWon / RATE_EUR;
	int changeEUR = (int)(inputWon - outputEUR * RATE_EUR) / 10 * 10;
	
	int outputYEN = inputWon / RATE_YEN;
	int changeYEN = (int)(inputWon - outputYEN * RATE_YEN) / 10 * 10;
	
	int outputCNY = inputWon / RATE_CNY;
	int changeCNY = (int)(inputWon - outputCNY * RATE_CNY) / 10 * 10;
	
	printf("\n%d원\n- 받을 달러: %d USD -> ", inputWon, outputUSD);
	BillsToGive(outputUSD, 100, 50, 20, 5, 1);
	ChangesToGive(changeUSD); 
	printf("\n%d원\n- 받을 유로: %d EUR -> ", inputWon, outputEUR);
	BillsToGive(outputEUR, 100, 50, 20, 5, 1);
	ChangesToGive(changeEUR); 
	printf("\n%d원\n- 받을 엔: %d YEN -> ", inputWon, outputYEN);
	BillsToGive(outputYEN, 5000, 1000, 100, 10, 1);
	ChangesToGive(changeYEN); 
	printf("\n%d원\n- 받을 위안: %d CNY -> ", inputWon, outputCNY);
	BillsToGive(outputCNY, 100, 50, 20, 5, 1);
	ChangesToGive(changeCNY); 
	return 0;
}  
