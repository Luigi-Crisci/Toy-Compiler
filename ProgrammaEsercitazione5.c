#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "src/main/native/functions.h"
typedef struct function_struct_getNumeri
{
	float p_0;
	float p_1;
} function_struct_getNumeri;
function_struct_getNumeri getNumeri()
{
	float primo, secondo;
	printf("%s", "Inserire il primo numero: ");
	primo = strtof(readln(), NULL);
	printf("%s", "Inserire il secondo numero: ");
	secondo = strtof(readln(), NULL);
	function_struct_getNumeri getNumeri4eef8cece9b44f5a89d2a378eb7202f0;
	getNumeri4eef8cece9b44f5a89d2a378eb7202f0.p_0 = primo;
	getNumeri4eef8cece9b44f5a89d2a378eb7202f0.p_1 = secondo;
	return getNumeri4eef8cece9b44f5a89d2a378eb7202f0;
}
float sommaNumeri()
{
	float primo, secondo;
	function_struct_getNumeri getNumeri6afcc33a105e45688535d40f2eefd8fa = getNumeri();
	primo = getNumeri6afcc33a105e45688535d40f2eefd8fa.p_0;
	secondo = getNumeri6afcc33a105e45688535d40f2eefd8fa.p_1;
	return primo + secondo;
}
float multConSomma()
{
	int i = 0;
	float primo, secondo, mult = 0.0;
	function_struct_getNumeri getNumeri33e58f5fe6e34cb7b4dae0b165beb455 = getNumeri();
	primo = getNumeri33e58f5fe6e34cb7b4dae0b165beb455.p_0;
	secondo = getNumeri33e58f5fe6e34cb7b4dae0b165beb455.p_1;
	while (i < secondo)
	{
		mult = mult + primo;
		i = i + 1;
	}
	return mult;
}
int divisioneNumeri()
{
	int primo, secondo;
	printf("%s", "Inserire il primo numero: ");
	primo = strtol(readln(), NULL, 10);
	printf("%s", "Inserire il secondo numero: ");
	secondo = strtol(readln(), NULL, 10);
	return primo / secondo;
}
float potenza()
{
	float primo, res = 1.0;
	int secondo, i = 0;
	printf("%s", "Inserire la base: ");
	primo = strtof(readln(), NULL);
	printf("%s", "Inserire l'esponente: ");
	secondo = strtol(readln(), NULL, 10);
	while (i < secondo)
	{
		res = res * primo;
		i = i + 1;
	}
	return res;
}
int fibonacci(int n)
{
	int res = n;
	if (n != 1 && n != 0)
	{
		res = fibonacci(n - 1) + fibonacci(n - 2);
	}
	return res;
}
int main()
{
	int op = 0, n = 0;
	printf("%s", "Scegli l'operazione da eseguire: \n			   1) Addizione tra due numeri;\n			   2) Moltiplicazione tra due numeri;\n			   3) Divisione intera tra 2 numeri;\n			   4) Elevamento a potenza;\n			   5) Successione di fibonacci\n			   0) Esci.\n			   ");
	op = strtol(readln(), NULL, 10);
	while (op != 0)
	{
		if (op >= 1 || op <= 5)
		{
			if (op == 1)
			{
				printf("%s%f%s", "La somma dei numeri è: ", sommaNumeri(), "\n");
			}
			else if (op == 2)
			{
				printf("%s%f%s", "La moltiplicazione dei numeri è: ", multConSomma(), "\n");
			}
			else if (op == 3)
			{
				printf("%s%d%s", "La divisione dei numeri è: ", divisioneNumeri(), "\n");
			}
			else if (op == 4)
			{
				printf("%s%f%s", "L'elevamento a potenza è: ", potenza(), "\n");
			}
			else if (op == 5)
			{
				printf("%s", "Inserisci il numero : ");
				n = strtol(readln(), NULL, 10);
				if (n >= 0)
				{
					printf("%s%d%s", "La successione di fibonacci è: ", fibonacci(n), "\n");
				}
				else
				{
					printf("%s", "Hai inserito un numero negativo\n");
				}
			}
		}
		printf("%s", "Scegli l'operazione da eseguire: \n			   1) Addizione tra due numeri;\n			   2) Moltiplicazione tra due numeri;\n			   3) Divisione intera tra 2 numeri;\n			   4) Elevamento a potenza;\n			   5) Successione di fibonacci\n			   0) Esci.\n			   ");
		op = strtol(readln(), NULL, 10);
	}
	return 0;
}