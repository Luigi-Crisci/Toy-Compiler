#include<stdlib.h>
#include<stdio.h>
int n=0;
int  factorial(int n){int result=0;
if(n==0){
result=1;
}
else{result=n*factorial(n-1);
}
return result;
}int  main(){int n=0;
printf("%s", "Enter n, or >= 10 to exit: ");
scanf("%d", &n);
while(n<10){
printf("%s%d%s%d%s", "Factorial of ", n, " is ", factorial(n), "\n");
printf("%s", "Enter n, or >= 10 to exit: ");
scanf("%d", &n);
}
return 0;
}