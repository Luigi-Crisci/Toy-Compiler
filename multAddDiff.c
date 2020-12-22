#include <stdlib.h>
#include <stdio.h>
char nome[256] = "Michele";
typedef struct function_struct_multAddDiff
{
	int p_0;
	int p_1;
	int p_2;
} function_struct_multAddDiff;
function_struct_multAddDiff multAddDiff()
{
	char s[256];
	int primo, secondo, mul, add, diff;
	scanf("%s", s);
	printf("%s", "Inserire il primo argomento:\n");
	scanf("%d", &primo);
	printf("%s", "Inserire il secondo argomento:\n");
	scanf("%d", &secondo);
	mul = primo * secondo;
	add = primo + secondo;
	diff = primo - secondo;
	function_struct_multAddDiff multAddDifffcc4efc05e4f4c5d907a5d71be9db2ea;
	multAddDifffcc4efc05e4f4c5d907a5d71be9db2ea.p_0 = mul;
	multAddDifffcc4efc05e4f4c5d907a5d71be9db2ea.p_1 = add;
	multAddDifffcc4efc05e4f4c5d907a5d71be9db2ea.p_2 = diff;
	return multAddDifffcc4efc05e4f4c5d907a5d71be9db2ea;
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
	int a, b, c, d, e, f = 0;
	function_struct_multAddDiff multAddDiffac9c8a0d747f49ada609f2b82d0fde36 = multAddDiff();
	function_struct_multAddDiff multAddDiff73e70c1b2f0341099642d246ebe5595a = multAddDiff();
	a = multAddDiffac9c8a0d747f49ada609f2b82d0fde36.p_0;
	b = multAddDiffac9c8a0d747f49ada609f2b82d0fde36.p_1;
	c = multAddDiffac9c8a0d747f49ada609f2b82d0fde36.p_2;
	d = multAddDiff73e70c1b2f0341099642d246ebe5595a.p_0;
	e = multAddDiff73e70c1b2f0341099642d246ebe5595a.p_1;
	f = multAddDiff73e70c1b2f0341099642d246ebe5595a.p_2;
	printf("%s%s", "Ciao ", nome);
	writeNewLines(2);
	printf("%s%d%s%d%s%d%s", "I tuoi valori sono:\n", a, " per la moltiplicazione\n", b, " per la somma, e \n", c, " per la differenza\n");
	return 0;
}