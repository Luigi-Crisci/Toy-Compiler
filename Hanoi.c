#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include"src/main/native/functions.h"
void  printPeg(int peg){if(peg==1){
printf("%s", "left");
}
else if(peg==2){
printf("%s", "center");
}
else{printf("%s", "right");
}
}void  hanoi(int n, int fromPeg, int usingPeg, int toPeg){if(n!=0){
hanoi(n-1, fromPeg, toPeg, usingPeg);
printf("%s", "Move disk from ");
printPeg(fromPeg);
printf("%s", " peg to ");
printPeg(toPeg);
printf("%s", " peg.\n");
hanoi(n-1, usingPeg, fromPeg, toPeg);
}
}int  main(){int n=0;
printf("%s", "How many pegs? ");
n=strtol(readln(),NULL,10);
hanoi(n, 1, 2, 3);
return 0;
}