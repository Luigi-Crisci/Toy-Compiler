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
	function_struct_getNumeri getNumeri6a183c0e60724566a41f43ac445987fb;
	getNumeri6a183c0e60724566a41f43ac445987fb.p_0 = primo;
	getNumeri6a183c0e60724566a41f43ac445987fb.p_1 = secondo;
	return getNumeri6a183c0e60724566a41f43ac445987fb;
}
float sommaNumeri()
{
	float primo, secondo;
	function_struct_getNumeri getNumeri4e20e43edca74d5e9cb21d3e212d239e = getNumeri();
	primo = getNumeri4e20e43edca74d5e9cb21d3e212d239e.p_0;
	secondo = getNumeri4e20e43edca74d5e9cb21d3e212d239e.p_1;
	return primo + secondo;
}
float multConSomma()
{
	float primo, secondo, mult = 0.0;
	int i = 0;
	function_struct_getNumeri getNumericca1ded910e84a5d9ccdd8e60b9f6a68 = getNumeri();
	primo = getNumericca1ded910e84a5d9ccdd8e60b9f6a68.p_0;
	secondo = getNumericca1ded910e84a5d9ccdd8e60b9f6a68.p_1;
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
	int secondo, i = 0;
	float primo, res = 1.0;
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