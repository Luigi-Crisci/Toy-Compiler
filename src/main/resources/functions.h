#include<stdio.h>
#include<stdlib.h>

/**
 * Read a bunch of characters from standard input
 */
char* readln(){
	char c;
	int size = 256;
	char *s = (char*) calloc(size,sizeof(char));
	int i = 0;

	while(1){
		if(i == size - 1){
			size = size + 256;
			s = (char*) realloc(s,size * sizeof(char));
		}

		// c = fgetc(stdin);
		scanf("%c",&c);
		printf("current char: %c\n",c);
		if(c == '\n'){
			if (i > 0){
				break;
			}
			else{
				continue; //This handle trailing newline from scanf
			}
		}
		s[i++] = c; 
	}
	return (char*)realloc(s,i * sizeof(char));
}