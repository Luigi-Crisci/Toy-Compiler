#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include"src/main/resources/emsctipten_functions.h"
char* nome="Michele";
typedef struct function_struct_multAddDiff{
int  p_0;
int  p_1;
int  p_2;
}function_struct_multAddDiff;
function_struct_multAddDiff multAddDiff(){int primo, secondo, mul, add, diff;
printf("%s", "Inserire il primo argomento:\n");
primo=strtol(readln(),NULL,10);
printf("%s", "Inserire il secondo argomento:\n");
secondo=strtol(readln(),NULL,10);
mul=primo*secondo;
add=primo+secondo;
diff=primo-secondo;
function_struct_multAddDiff multAddDiff54b3e9a6e9ae4d24aeb1ed86a4330e00;
multAddDiff54b3e9a6e9ae4d24aeb1ed86a4330e00.p_0 = mul;
multAddDiff54b3e9a6e9ae4d24aeb1ed86a4330e00.p_1 = add;
multAddDiff54b3e9a6e9ae4d24aeb1ed86a4330e00.p_2 = diff;
return multAddDiff54b3e9a6e9ae4d24aeb1ed86a4330e00;
}void  writeNewLines(int n){while(n>0){
printf("%s", "\n");
n=n-1;
}
}int  main(){int a, b, c;
a=strtol(readln(),NULL,10);
nome=readln();
b=strtol(readln(),NULL,10);
c=strtol(readln(),NULL,10);
function_struct_multAddDiff multAddDiff9d7f3c53725f4642b1e4e8a83c657348 = multAddDiff();
a=multAddDiff9d7f3c53725f4642b1e4e8a83c657348.p_0;
b=multAddDiff9d7f3c53725f4642b1e4e8a83c657348.p_1;
c=multAddDiff9d7f3c53725f4642b1e4e8a83c657348.p_2;
printf("%s%s", "Ciao ", nome);
writeNewLines(2);
printf("%s%d%s%d%s%d%s", "I tuoi valori sono:\n", a, " per la moltiplicazione\n", b, " per la somma, e \n", c, " per la differenza\n");
return 0;
}