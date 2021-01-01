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

		c = fgetc(stdin);
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

int string_to_int(char* s){
	if(s == NULL)
		return -1;

	char* ptr;
	int n = strtol(s,&ptr,10);
	if(ptr == s)
		exit(EXIT_FAILURE);
	free(s);
	return n;
}

int string_to_float(char* s){
	if(s == NULL)
		return -1;
	
	char* ptr;
	float f = strtof(s,&ptr);
	if(ptr == s)
		exit(EXIT_FAILURE);
	free(s);
	return f;
}

int string_to_bool(char* s){
	if(s == NULL)
		return -1;
		
	int res = -1;
	if(strcmp(s,"true") == 0)
		res = 1;
	else 
		if (strcmp(s,"false") == 0)
			res = 0;
		else
			exit(EXIT_FAILURE);
	free(s);
	return res;
}


