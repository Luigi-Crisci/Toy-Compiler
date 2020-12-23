#include<stdio.h>
#include<stdlib.h>

char* readln(){
	char c;
	char *s = (char*) calloc(256,sizeof(char));
	int i = 0;
	while(1){
		c = fgetc(stdin);
		if(c == '\n')
			break;
		s[i++] = c; 
	}

	return (char*)realloc(s,i * sizeof(char));
}