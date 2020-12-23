#include <stdlib.h>
#include <stdio.h>
#include "src/main/resources/functions.h"
char *nome = "Michele";
typedef struct function_struct_multAddDiff
{
	int p_0;
	int p_1;
	int p_2;
} function_struct_multAddDiff;
function_struct_multAddDiff multAddDiff()
{
	int primo, secondo, mul, add, diff;
	printf("%s", "Inserire il primo argomento:\n");
	scanf("%d", &primo);
	printf("%s", "Inserire il secondo argomento:\n");
	scanf("%d", &secondo);
	mul = primo * secondo;
	add = primo + secondo;
	diff = primo - secondo;
	function_struct_multAddDiff multAddDiff083347ad9f6b45f69e0cafa05b7f80e5;
	multAddDiff083347ad9f6b45f69e0cafa05b7f80e5.p_0 = mul;
	multAddDiff083347ad9f6b45f69e0cafa05b7f80e5.p_1 = add;
	multAddDiff083347ad9f6b45f69e0cafa05b7f80e5.p_2 = diff;
	return multAddDiff083347ad9f6b45f69e0cafa05b7f80e5;
}
void writeNewLines(int n)
{
	while (n > 0)
	{
		printf("%s", "\n");
		n = n - 1;
	}
}
int main()
{
	char *s;
	int a, b, c;
	scanf("%d%d", &a, &b);
	s = readln();
	scanf("%d", &c);
	function_struct_multAddDiff multAddDiffc48526c5982647a980a16a9eb0e38c67 = multAddDiff();
	a = multAddDiffc48526c5982647a980a16a9eb0e38c67.p_0;
	b = multAddDiffc48526c5982647a980a16a9eb0e38c67.p_1;
	c = multAddDiffc48526c5982647a980a16a9eb0e38c67.p_2;
	printf("%s%s", "Ciao ", s);
	writeNewLines(2);
	printf("%s%d%s%d%s%d%s", "I tuoi valori sono:\n", a, " per la moltiplicazione\n", b, " per la somma, e \n", c, " per la differenza\n");
	return 0;
}