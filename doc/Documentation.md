---
	title: Toy Compiler
	author:
		- Luigi Crisci
		- Alessio Cuccurullo
	abstract: A compiler for the Toy language in pure Java. It compiles to C as Intermediate code representation, then the *Clang* compiler is used to generate machine code
---
# Toy language Compiler

A compiler for the Toy language, made with Java CUP and JFlex. This implementation results in the creation of an executable file given a compliant Toy file.

## Build

### Requirements

- Java 11
- Maven
- Clang

### Build instructions

To build this project, just type

> mvn package

A jar *with all dependencies* will be created under the `release/` directory.  

> Note that code generation for *Javacup* and *jflex* is disabled by default. If you want to manually generate the sources for Javacup and Jflex, type *"mvn generate-sources -P source-gen"* and the .java classes will be placed under *target* folder.

A compiled version of this compiler can be found under the `release/` directory. The release is composed of:  

- ***Toy2C.jar***: a pre-compiled version of this software
- ***Toy2C***: an executable script that, given a .toy file, compiles it into a .c file and then call `clang` to output a machine-code executable
- ***install_native_libraries***: an executable script that create a hard link under `/usr/local` directory for the `toy_functions.h` library, a native library needed in the compilation phase.  
- ***uninstall_native_libraries***: an executable script that remove the hard link under `/usr/local` created by ***install_native_libraries***.

An example of the *Toy2C* usage:

> ./Toy2C FILENAME 

The output file is placed in the same directory of the input file.


## Assignment overview

This compiler translates a .toy program into a Clang-compliant C program. The generated .c file is then compiled and, after that, it's ready to run. The stages of the execution are the following:

- Lexical analysis
- Syntactic analysis
- (Optional) AST visualization
- Semantic analysis
- Toy2C translation

### Differences with the assignment

This implementation doesn't go that far from the assignment. Although, some variations have been made by the authors:  
- The token MAIN has been introduced;  
- The productions *"Main ::= PROC MAIN ..."* have been added, slightly changing the syntax of the language. These productions, along with the *ProcList*, make every Toy program syntactically compliant when the procedure *Main*:  
	- appears one single time,  
	- is the last one in the file,  
	- returns an INT.    
- The production "CallProcedureStatement" has been duplicated in the production "CallProcedureExpression" because of a parsing problem derived from our class configuration

# Lexical analysis

This step is carried out by a Lexer written in `flex` and compiled with `Jflex` (an open source tool for generating Lexer in Java).

It is composed of a single `Lexer.flex` source file contaning all the logic to generate a Lexer class, which is crucial for the next stage. The Lexer resulting by the compiling does the whole Lexical analysis.

## Lexical specification

This section describes the set of tokens and their corresponding pattern.


```flex
	//Procedures
	PROC "proc"          
	CORP "corp"          
	MAIN "main"          
	
	//Type
	INT "int"             
	FLOAT "float"           
	BOOL "bool"            
	STRING "string"          
	VOID "void"            

	//Statements
	WHILE "while"           
	DO "do"                   
	OD "od"              
	READ "readln"            
	WRITE "write"           
	ASSIGN "assign"                            
	IF "if"                        
	THEN "then"                  
	FI "fi"                        
	ELSE "else"                      
	ELIF "elif"                      

	//Separators
	LPAR "("                                
	RPAR ")"               
	COLON ":"               
	COMMA ","               
	SEMI ";"               


	//Operators
	ASSIGN ":="                                       
	PLUS "+"                            
	MINUS "-"                            
	TIMES "*"                            
	DIV "/"                            
	EQ "="                            
	NE "<>"                            
	LT "<"                            
	LE "<="                            
	GT ">"                            
	GE ">="                            
	AND "&&"              
	OR "||"              
	NOT "!"                            
	NULL "null"                
	TRUE "true"                                       
	FALSE "false"            
	RETURN "->"              


    ALPHA=[A-Za-z]
	DIGIT=[0-9]
	NONZERO_DIGIT=[1-9]
	NEWLINE=\\r|\\n|\\r\\n
	WHITESPACE =  | [ \\t\\f]
	ID = (|_)*
	INT = ((*)|0)
	FLOAT = +)
	STRING_TEXT = [^\\"]*
	COMMENT_TEXT = [\\w\\.\\@]*
```

# Syntactic analysis

Just like the lexical analysis, the syntactic analysis is done by a generated Java class. This has been done with `CUP` (`Construction of Useful Parsers`), which implements *LALR(1)* parsing.

## Syntactic specification 

This section describes the whole syntactic specification of the Toy language that was implemented. The grammar as-is doesn't allow *LALR(1)* parsing. In order to generate the parser, the *PEMDAS* (Parenthesis, Exponents, Multiplications/Divisions, Additions/Subtractions) rule has been introduced.

```cup
Program ::= VarDeclList ProcList		 	 
		;

	VarDeclList ::= /* empty */ 					 
		| VarDecl VarDeclList					 
		;

	VarDecl ::= Type IdListInit SEMI			 
		;

	ProcList ::= Main 							 						
		| Proc ProcList						 
		;

	Type ::= INT									 
			| BOOL 								 
			| FLOAT								 
			| STRING								 		
		;

	IdListInit ::= ID 												 
		| IdListInit COMMA ID										 
		| ID ASSIGN Expr											 
		| IdListInit COMMA ID ASSIGN Expr						 
		;

	Proc ::= PROC ID LPAR ParamDeclList RPAR ResultTypeList COLON
			VarDeclList StatList RETURN ReturnExprs CORP SEMI			 
		| PROC ID LPAR RPAR ResultTypeList COLON 									
			VarDeclList StatList RETURN ReturnExprs CORP SEMI			 
		|	PROC ID LPAR ParamDeclList RPAR ResultTypeList COLON 
			VarDeclList RETURN ReturnExprs CORP SEMI						 
		| PROC ID LPAR RPAR ResultTypeList COLON 
			VarDeclList RETURN ReturnExprs CORP SEMI						 
		;    

	Main ::= PROC MAIN LPAR ParamDeclList RPAR INT COLON
			VarDeclList StatList RETURN ReturnExprs CORP SEMI			 
		| PROC MAIN LPAR RPAR INT COLON 									
			VarDeclList StatList RETURN ReturnExprs CORP SEMI			 
		|	PROC MAIN LPAR ParamDeclList RPAR INT COLON 
			VarDeclList RETURN ReturnExprs CORP SEMI
		| PROC MAIN LPAR RPAR INT COLON 
			VarDeclList RETURN ReturnExprs CORP SEMI						 
		;

	ResultTypeList ::= ResultType					 
		| ResultType COMMA ResultTypeList		 
		;

	ResultType ::= Type								 
			| VOID 									 
		;

	ReturnExprs::=  ExprList							  						
		| /* empty */ 									 
		;

	ParamDeclList ::= ParDecl 						 
		| ParamDeclList SEMI ParDecl				 
		;

	ParDecl ::= Type IdList							 
		;

	IdList ::= ID										 
		| IdList COMMA ID							 
		;

	StatList ::= Stat 								 
		| Stat StatList 							 
		;

	Stat ::= IfStat SEMI							 
		| WhileStat SEMI								 
		| ReadlnStat SEMI							 
		| WriteStat SEMI								 
		| AssignStat SEMI							 
		| CallProc SEMI								 
		;

	WhileStat ::= WHILE StatList RETURN Expr DO StatList OD 			 
		| WHILE Expr DO StatList OD										 
		;

	IfStat ::= IF Expr THEN StatList ElifList Else FI				 
		;

	ElifList ::= /* empty */ 								 
		| Elif ElifList							 	   		 
		;

	Elif ::= ELIF Expr THEN StatList					 
		;

	Else ::= /* empty */									  
			| ELSE StatList								 
		;

	ReadlnStat ::= READ LPAR IdList RPAR				 
		;

	WriteStat ::=  WRITE LPAR ExprList RPAR			 
		;

	AssignStat ::= IdList ASSIGN ExprList			 
		;

	CallProc ::= ID LPAR ExprList RPAR   			 
		| ID LPAR RPAR   									 
		;

	ExprList ::= Expr										 
		| Expr COMMA ExprList							 
		;

	Expr ::= NULL											                           					
		| TRUE												                             
		| FALSE												                            
		| INT_CONST										                     
		| FLOAT_CONST										 
		| STRING_CONST									 
		| ID												 	
		| ID LPAR ExprList RPAR   					 
		| ID LPAR RPAR   									 
		| Expr1  PLUS Expr2				
		| Expr1  MINUS Expr2							 				
		| Expr1  TIMES Expr2							 				
		| Expr1  DIV Expr2								 				
		| Expr1  AND Expr2								 				
		| Expr1  OR Expr2								 				
		| Expr1  GT Expr2								 				
		| Expr1  GE Expr2								 				
		| Expr1  LT Expr2								 				
		| Expr1  LE Expr2								 				
		| Expr1  EQ Expr2								 				
		| Expr1  NE Expr2								 				
		| MINUS Expr										 
		| NOT Expr										 			
		;		
```


# The Abstract Syntax Tree

The implemented Grammar has been enhanced with a series of *actions*, one for each production. 

Generally, these actions instantiate a *Node* object related to each *element* appearing in the right-hand side of the production itself. 
There are several different actions in this parser.

For example:

	VarDeclList ::= /* empty */  {: RESULT = new LinkedList<VariableDeclarationNode>(); :}					 
	| VarDecl VarDeclList		{: vl.add(vd); RESULT = vl; :}			 
	;

The *empty* production creates a new list containing the variables declarated, while the second one appends a given variable to the list. In a successful situation, one would expect the process to eventually resolve in the *empty* statement, thus instantiating the list and adding the items found. 

As the *Parser* elaborates a given source file, the corresponding (and unique) *Syntax Tree* is generated recursively. With the completion of the parsing process, a pointer to the root of the *Syntax tree* is returned, which comes in handy for the next step of the compiler.

## Tree visualization

Moreover, this implementation provides with a visualization of the *Syntactic Tree*, constructed in the previous step, via XML. The *Visitor* pattern fits this role perfectly. Once the parser has finished its job, the user can call the *ASTVisitor* on the root of the tree, which will generate a .xml file based on the instance of the *Syntactic tree*. The user can open the generated file using any Web Browser.

# Semantic Analysis

In order to check whether the input program is semantically compliant, a *SemanticVisitor* object is created and invoked on the root of the AST generated by the parser. Given the set of rules, a single visit of such tree is sufficient towards the semantic analysis. 

## Inference table

The following tables describe each and every inference rule required to the type checking. The implementation of these rules can be found in the *TypeCheck* class.



| Binary_op		  | First operand type | Second operand type | Resulting type |
|-----------------|--------------------|---------------------|----------------|
| :=			  | int				   | int				 | int			  |
| :=			  | float			   | float				 | float		  |
| :=			  | string			   | string				 | string		  |
| :=			  | boolean			   | boolean			 | boolean		  |
| * / + -         | int      	       | int         	     | int       	  |
| * / + -         | int          	   | float               | float          |
| * / + -         | float              | int            	 | float          |
| * / + -         | float              | float               | float          |
| <= < == <> > >= | int            	   | int            	 | boolean        |
| <= < == <> > >= | int            	   | float               | boolean        |
| <= < == <> > >= | float              | int        	     | boolean        |
| <= < == <> > >= | float              | float               | boolean        |
| && \|\|         | boolean            | boolean             | boolean        |



| Unary_op		  | Operand type	   | Resulting type |
|-----------------|--------------------|----------------|
| -               | int         	   | int	 		|
| -               | float              | float          |	
| !               | boolean            | boolean        |
## Inference rules

These are the inference rules implemented in the *SemanticVisitor* class.

- Expressions
$$ \frac{(expr:\tau)\in\Gamma}{\Gamma \vdash expr:\tau} $$

- Binary operation
$$ \frac{\Gamma \vdash expr_1 : \tau_1 \;\;\;\Gamma \vdash expr_2 : \tau_2 \;\;\; \Gamma \vdash binary\_op(binOp,\tau_1 , \tau_2)=\tau}{\Gamma \vdash (expr_1\;binOp\;expr_2):\tau} $$

- Unary operation
$$ \frac{\Gamma \vdash expr : \tau_1 \;\;\; \Gamma \vdash unary\_op(unOp,\tau_1)=\tau}{\Gamma \vdash (unOp\;expr):\tau} $$

- Call procedure
  $$ \frac{\Gamma \vdash proc:\tau\,_i^{i\in\,0...n} \rightarrow \tau\,_j^{j\in\,0...m} \;\;\; \Gamma \vdash par_i^{i\in\,0...n}:\tau\,_i}{\Gamma \vdash proc(par_i^{i\in\,0...n}):\tau\,_j^{j\in\,0...m}} $$

- While statement
$$\frac{\Gamma \vdash cnd\_expr : \bm{boolean} \;\;\; \Gamma \vdash while\_stmt\,_i^{i\in\,1...n}\;\;\;\Gamma \vdash   do\_stmt\,_j^{j\in\,0...n}}{\Gamma \vdash \bm{while} \; (while\_stmt\,_i^{i\in\,1...n}) \; \bm{do} \; (do\_stmt\,_j^{j\in\,0...n})\;\bm{od}}$$

- Assign statement
$$\frac{(x\,_i^{i\in\,1...n}:\tau\,_i)\in \Gamma\;\;\;\Gamma\vdash (expr\,_j^{j\in\,1...n}) \rightarrow \tau\,_i^{i\in\,1...n}}{\Gamma \vdash x\,_i^{i\in\,1...n}=expr\,_j^{j\in\,1...n}}$$  

- If statement
$$\frac{\Gamma \vdash cnd\_expr : \bm{boolean} \;\;\; \Gamma \vdash then\_stmt\,_x^{x\in\,0...n}\;\; \Gamma \vdash elif\_stmt\,_y^{y\in\,0...n} \;\; \Gamma \vdash else\_stmt\,_z^{z\in\,0...n}}{\Gamma \vdash \bm{if} \; cnd\_expr \; \bm{then} \; (then\_stmt\,_x^{x\in\,0...n}) \; \bm{elif}\; (elif\_smt\,_y^{y\in\,0...n}) \; \bm{else} \; (else\_stmt\,_z^{z\in\,0...n})\;\bm{fi}}$$

- Write statement  
$$\frac{\Gamma \vdash expr_i \, : \, \tau_i^{i\,\in\,0...n}}{\Gamma \vdash \bm{write}(expr_i^{i\,\in\,0...n})}$$

- Read statement
$$\frac{(x_i \, : \, \tau_i^{i\,\in\,0...n}) \in \Gamma}{\Gamma \vdash \bm{readln}(x_i^{i\,\in\,0...n})}$$

# Translating to the C language

As the previous phases, a visitor has been implemented to translate the AST into a valid C program.  
To do so, a simple library called `toy_functions.h` has been written to handle some functions in a more easy way. It is composed of the following methods:  
- `char* readln()`: read a sequence of character from `stdin` until the `\n` character and return the corresponding string;  
- `int string_to_int(char* s)`: a `strtol()` alias that also deallocates the input string; 
- `int string_to_floar(char* s)`: a `strtof()` alias that also deallocates the input string; 
- `int string_to_bool(char* s)`: convert *"true"* to `1` and *"false"* to `0` and deallocates the input string.

The reason why these functions are needed is explained [here](#memory-allocation-for-strings).  

An important feature of this visitor is that insert a predefined *prefix* `__t2c__` at every identifier found during the AST traversal: this is needed for 2 reasons:  
- Every generated names during translation does not have the *prefix*, ensuring that no conflicts could emerge with other identifiers;  
- To avoid conflicts with *C names*  
```
	For example, in Toy is possible to declare a function named exit(), even if it is forbidden in C 
```

## Notably differences between Toy and C
The two languages differ in various aspects. Notably:  

- Toy allows the programmer to write a function with multiple return types, while C doesn't;  
- Toy boolean variables are `true` and `false`, while C handles them as `1` and `!1`;  
- Toy doesn't require the programmer to explicitly allocate memory when declaring a new string variable;  
- Toy allows the programmer to open and close a string on two different lines of code.  

For each of them, the solution is explained below.  

### Multiple return types

This is a simple Toy function that returns three integer variables.

```
proc multAddDiff()int, int, int :	
	int primo, secondo, mul, add, diff;

	write("Inserire il primo argomento:\n");
	readln(primo);
	write("Inserire il secondo argomento:\n");
	readln(secondo);
	mul, add, diff := primo*secondo, primo + secondo, primo - secondo;
	-> mul, add, diff
corp;
```

When translating this snippet, this is what gets generated:

```c
typedef struct function_struct_t2cmultAddDiff
{
    int p_0;
    int p_1;
    int p_2;
} function_struct_t2cmultAddDiff;

function_struct_t2cmultAddDiff t2cmultAddDiff()
{
    int t2cprimo, t2csecondo, t2cmul, t2cadd, t2cdiff;
    printf("%s", "Inserire il primo argomento:\n");
    t2cprimo = string_to_int(readln());
    printf("%s", "Inserire il secondo argomento:\n");
    t2csecondo = string_to_int(readln());
    t2cmul = t2cprimo * t2csecondo;
    t2cadd = t2cprimo + t2csecondo;
    t2cdiff = t2cprimo - t2csecondo;
    function_struct_t2cmultAddDiff function_struct_t2cmultAddDiff396874f9a95149b6be1614ee5e99fed5;
    function_struct_t2cmultAddDiff396874f9a95149b6be1614ee5e99fed5.p_0 = t2cmul;
    function_struct_t2cmultAddDiff396874f9a95149b6be1614ee5e99fed5.p_1 = t2cadd;
    function_struct_t2cmultAddDiff396874f9a95149b6be1614ee5e99fed5.p_2 = t2cdiff;
    return function_struct_t2cmultAddDiff396874f9a95149b6be1614ee5e99fed5;
}
```

Firstly, a `struct` with three `int` fields has been defined. It handles the return statement of the Toy function multAddDiff by getting returned by such function. The flow of the translated function is pretty much the same as the original, but the return statement explodes into a series of assignments. 

A mandatory note about the name of the `struct` variable: a uniquely generated string has been appended to the actual function. This verbose act prevents multiple declarations of the same c variable, in case of subsequent calls of the same function in a given scope (recursion!!).

Lastly, the filled `struct` is returned and it's used as follows:

```c
    int __t2c__a, __t2c__b, __t2c__c;
    function_struct___t2c__multAddDiff multAddDiffd3eae84310004a499bdcd38c5f9e2073 = __t2c__multAddDiff();
    __t2c__a = multAddDiffd3eae84310004a499bdcd38c5f9e2073.p_0;
    __t2c__b = multAddDiffd3eae84310004a499bdcd38c5f9e2073.p_1;
    __t2c__c = multAddDiffd3eae84310004a499bdcd38c5f9e2073.p_2;
```


### Memory allocation for strings
Toy doesn't requier the programmer to explicitly indicate the length of a string. Of course this is a huge problem when translating to C. In order to solve this issue, the funcion `readln` has been defined which reads characters from `stdin` and allocate enough memory to store the string read.


**An important flaw:**     We are aware of a huge problem in this implementation: **no string gets deallocated**.  
With a single visit to the AST is impossible to determine for all strings if a certain string can be deallocated or *when* it can be deallocated.   
To solve this issue, a ***Garbage collector*** fits perfectly but its implementation was beyond the scope of this project.

### Strings spanning on multiple lines
Notably, C doesn't allow string delimiters to be on two distinct lines of code.

```c
//This doesn't work
char *str ="C doesn't like 
enjambement";
```

On the other hand, Toy has no strict rule in that regard. For example, the following string is perfectly correct:

```
string fullName := " First name
Last name";
```

The `ToyToCVisitor` translates such strings by simply replacing each `\n` sequence with `\\n`. Its corresponding C string would be:

```c
char *fullName = "First name \n Last name";
```


### Boolean shenanigans

Toy handles boolean variables as "true" and "false", but C does not. In order to actually print these variables, a simple trick has been used. Supposing the compiler runs into a `write` statement that contains a boolean variable, the translation of such block of code would look something like this:

```C
	int __t2c__a_boolean=1;
	printf("%s%s", "This is a boolean: ", __t2c__a_boolean== 1 ? "true" : "false");
```

By using the ternary operator `?` it's easy to return to the original toy-ish boolean values on a single line.