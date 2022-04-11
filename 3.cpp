#include <stdio.h>

int ChangesToGive(int a) { //int a �� �Ž����� 
	printf("- �Ž�����: %d�� ->  1000��:%d,  ", a, a / 1000);
	a = a % 1000;
	printf("500��:%d,  ", a / 500);
	a = a % 500;
	printf("100��:%d,  ", a / 100);
	a = a % 100;
	printf("50��:%d,  ", a / 50);
	a = a % 50;
	printf("10��:%d,  \n", a / 10);
	return 0; 
}

int BillsToGive(int a, int bill_1, int bill_2, int bill_3, int bill_4, int bill_5) { //�ٵ�, ȭ����� 1,2,3,4,5 
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
	const float RATE_USD = 1233.1; //�޷�ȯ�� 
	const float RATE_EUR = 1342.51; //����ȯ��
	const float RATE_YEN = 9.87; //��ȭȯ��
	const float RATE_CNY = 193.5; //����ȯ�� 
	
	int inputWon;
	printf("ȯ���� ���ϴ� �ݾ��� �Է��ϼ���(��ȭ) : ");
	scanf("%d", &inputWon);	
	
	int outputUSD = inputWon / RATE_USD;
	int changeUSD = (int)(inputWon - outputUSD * RATE_USD) / 10 * 10;
	
	int outputEUR = inputWon / RATE_EUR;
	int changeEUR = (int)(inputWon - outputEUR * RATE_EUR) / 10 * 10;
	
	int outputYEN = inputWon / RATE_YEN;
	int changeYEN = (int)(inputWon - outputYEN * RATE_YEN) / 10 * 10;
	
	int outputCNY = inputWon / RATE_CNY;
	int changeCNY = (int)(inputWon - outputCNY * RATE_CNY) / 10 * 10;
	
	printf("\n%d��\n- ���� �޷�: %d USD -> ", inputWon, outputUSD);
	BillsToGive(outputUSD, 100, 50, 20, 5, 1);
	ChangesToGive(changeUSD); 
	printf("\n%d��\n- ���� ����: %d EUR -> ", inputWon, outputEUR);
	BillsToGive(outputEUR, 100, 50, 20, 5, 1);
	ChangesToGive(changeEUR); 
	printf("\n%d��\n- ���� ��: %d YEN -> ", inputWon, outputYEN);
	BillsToGive(outputYEN, 5000, 1000, 100, 10, 1);
	ChangesToGive(changeYEN); 
	printf("\n%d��\n- ���� ����: %d CNY -> ", inputWon, outputCNY);
	BillsToGive(outputCNY, 100, 50, 20, 5, 1);
	ChangesToGive(changeCNY); 
	return 0;
}  
