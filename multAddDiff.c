#include <stdlib.h>
#include <stdio.h>
#include <string.h>
// #include "src/main/resources/functions.h"
#include <emscripten.h>

EM_JS(char*, readln, (), {
  // return prompt("Insert value");ù

var jsString = prompt("Insert value");
  // 'jsString.length' would return the length of the string as UTF-16
  // units, but Emscripten C strings operate as UTF-8.
  var lengthBytes = lengthBytesUTF8(jsString)+1;
  var stringOnWasmHeap = _malloc(lengthBytes);
  stringToUTF8(jsString, stringOnWasmHeap, lengthBytes);
  return stringOnWasmHeap;

  // return "aaaaa";
});


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
	primo = strtol(readln(), NULL, 10);
	printf("%s", "Inserire il secondo argomento:\n");
	secondo = strtol(readln(), NULL, 10);
	mul = primo * secondo;
	add = primo + secondo;
	diff = primo - secondo;
	function_struct_multAddDiff multAddDiff2c6adc53b52b424e985d7af7bf6964b2;
	multAddDiff2c6adc53b52b424e985d7af7bf6964b2.p_0 = mul;
	multAddDiff2c6adc53b52b424e985d7af7bf6964b2.p_1 = add;
	multAddDiff2c6adc53b52b424e985d7af7bf6964b2.p_2 = diff;
	return multAddDiff2c6adc53b52b424e985d7af7bf6964b2;
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
	int a, b, c;
	a = strtol(readln(), NULL, 10);
	nome = readln();
	b = strtol(readln(), NULL, 10);
	c = strtol(readln(), NULL, 10);
	function_struct_multAddDiff multAddDiff03620fab5b2c41da9e5bc9d83136f92c = multAddDiff();
	a = multAddDiff03620fab5b2c41da9e5bc9d83136f92c.p_0;
	b = multAddDiff03620fab5b2c41da9e5bc9d83136f92c.p_1;
	c = multAddDiff03620fab5b2c41da9e5bc9d83136f92c.p_2;
	printf("%s%s", "Ciao ", nome);
	writeNewLines(2);
	printf("%s%d%s%d%s%d%s", "I tuoi valori sono:\n", a, " per la moltiplicazione\n", b, " per la somma, e \n", c, " per la differenza\n");
	return 0;
}