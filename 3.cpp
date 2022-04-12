#include <stdio.h>

const float RATE_USD = 1233.1; //�޷�ȯ�� 
const float RATE_JPY = 9.87; //��ȭȯ��
const float RATE_EUR = 1342.51; //����ȯ��
const float RATE_CNY = 193.5; //����ȯ�� 
const float RATE_GBP = 1603.15; //�Ŀ��ȯ�� 

int inputWon, output, change;
char *unit;
int ChangesToGive (int a) { //int a �� �Ž����� 
	printf("�Ž�����: %d�� ---> 1000��:%d,  ", a, a / 1000);
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

int BillsToGive(int a, char *b, int bill_1, int bill_2, int bill_3, int bill_4, int bill_5) { //�ٵ�, ȭ��, ������� 1,2,3,4,5 
	printf("%s : %d%s ---> ", b, a, b); // "�޷� : 40�޷�" ���� ������ ��� 
	printf("%d%s:%d,  ", bill_1, b, a / bill_1); //���� ������ ���� ��� 
	a = a % bill_1;
	printf("%d%s:%d,  ", bill_2, b, a / bill_2);
	a = a % bill_2;
	printf("%d%s:%d,  ", bill_3, b, a / bill_3);
	a = a % bill_3;
	printf("%d%s:%d,  ", bill_4, b, a / bill_4);
	a = a % bill_4;
	printf("%d%s:%d,  \n", bill_5, b, a / bill_5);
	return 0; 
}

int exchange(float a) {
	printf("���� ȯ�� : %.2f\nȯ�� ���\n", a);
	output = inputWon / a;
	change = (int)(inputWon - output * a) / 10 * 10;
	return 0;
}


int main() {
	int currency;
	printf("ȯ���� ���ϴ� �ݾ��� �Է��ϼ���(��ȭ) : ");
	scanf("%d", &inputWon);	
	
	do {
		printf("ȯ���� ��ȭ�� �����ϼ��� (1:USD, 2:JPY, 3:EUR 4.CNY, 5:GBP) : ");
		scanf("%d", &currency);
	} while (currency < 1 || currency > 5);

	if (currency == 1) {
		exchange(RATE_USD);
		BillsToGive(output, "�޷�", 100, 50, 20, 5, 1);
	} else if (currency == 2) {
		exchange(RATE_JPY);
		BillsToGive(output, "��", 5000, 1000, 100, 10, 1);
	} else if (currency == 3) {
		exchange(RATE_JPY);
		BillsToGive(output, "����", 100, 50, 20, 5, 1);
	} else if (currency == 4) {
		exchange(RATE_JPY);
		BillsToGive(output, "����", 100, 50, 20, 5, 1);
	} else if (currency == 5) {
		exchange(RATE_JPY);
		BillsToGive(output, "�Ŀ��", 50, 20, 10, 5, 1);
	}
	
	ChangesToGive(change); 
	return 0;
}  