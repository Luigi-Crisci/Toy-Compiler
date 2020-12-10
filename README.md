# Toy language Parser

A simple Parser for the Toy language, made with Java CUP and JFlex. This implementation results in the creation of the *Abstract Syntax Tree* of the Toy language.

## Build  

### Requirements

- Java 11
- Maven

### IntelliJ Configuratiom

This project is provided with a set of *IntelliJ Configurations:* 

> To run it just import it as a *Maven project* and click *Run*

### Maven Build

Alternatively you can compile manually typing: 

> mvn package

The *jar* file will be placed under the *target/* directory.  

## Syntactic specification 

This section describes the whole syntactic specification of the Toy language that was implemented.

  <details>
	<summary>The grammar</summary>

	Program ::= VarDeclList:vl ProcList:pl	
		

	VarDeclList ::= /* empty */ 				
		| VarDecl:vd VarDeclList:vl				
		

	ProcList ::= Proc 							
		| Proc ProcList
		

	VarDecl ::= Type IdListInit SEMI
		

	Type ::= INT | BOOL | FLOAT | STRING
		

	IdListInit ::= ID 
		| IdListInit COMMA ID
		| ID ASSIGN Expr
		| IdListInit COMMA ID ASSIGN Expr
		

	Proc ::= PROC ID LPAR ParamDeclList RPAR ResultTypeList COLON 
			VarDeclList StatList RETURN ReturnExprs CORP SEMI
		| PROC ID LPAR RPAR ResultTypeList COLON 
			VarDeclList StatList RETURN ReturnExprs CORP SEMI
		|	PROC ID LPAR ParamDeclList RPAR ResultTypeList COLON 
			VarDeclList RETURN ReturnExprs CORP SEMI
		| PROC ID LPAR RPAR ResultTypeList COLON 
			VarDeclList RETURN ReturnExprs CORP SEMI
		

	ResultTypeList ::= ResultType
		| ResultType COMMA ResultTypeList
		

	ReturnExprs::=  ExprList 
		| /* empty */ 
		

	ExprList ::= Expr	
		| Expr COMMA ExprList
		

	ParamDeclList ::= ParDecl | ParamDeclList SEMI ParDecl
		

	ParDecl ::= Type IdList
		

	IdList ::= ID | IdList COMMA ID
		

	ResultType ::= Type | VOID 
		

	StatList ::= Stat | Stat StatList 
		

	Stat ::= IfStat SEMI
		| WhileStat SEMI
		| ReadlnStat SEMI
		| WriteStat SEMI
		| AssignStat SEMI
		| CallProc SEMI
		

	WhileStat ::= WHILE StatList RETURN Expr DO StatList OD
		| WHILE Expr DO StatList OD
		

	IfStat ::= IF Expr THEN StatList ElifList Else FI
		

	ElifList ::= /* empty */ 
		| Elif ElifList		   
		

	Elif ::= ELIF Expr THEN StatList
		

	Else ::= /* empty */ | ELSE StatList
		

	ReadlnStat ::= READ LPAR IdList RPAR
		

	WriteStat ::=  WRITE LPAR ExprList RPAR
		

	AssignStat ::= IdList ASSIGN  ExprList
		

	CallProc ::= ID LPAR ExprList RPAR   
		| ID LPAR RPAR   
		

	Expr ::= NULL                          
		| TRUE                            
		| FALSE                           
		| INT_CONST                    
		| FLOAT_CONST
		| STRING_CONST
		| ID
		| ID:i LPAR ExprList:exprl RPAR 
		| ID:i LPAR RPAR 
		| Expr  PLUS Expr
		| Expr  MINUS Expr
		| Expr  TIMES Expr
		| Expr  DIV Expr
		| Expr  AND Expr
		| Expr  OR Expr
		| Expr  GT Expr
		| Expr  GE Expr
		| Expr  LT Expr
		| Expr  LE Expr
		| Expr  EQ Expr
		| Expr  NE Expr
		| MINUS Expr
		

  </details>


## Lexical specification

This section describes the set of tokens and their corresponding pattern.

<details>
	<summary>The patterns</summary>

	SEMI ';'
	COMMA ','
	INT  'int'
	STRING 'string'
	FLOAT 'float'
	BOOL 'bool'
	PROC 'proc'
	LPAR '('
	RPAR ')'
	COLON ':'
	PROC 'proc'
	CORP 'corp'
	VOID 'void'
	IF 'if'
	THEN 'then'
	ELIF 'elif'
	FI 'fi'
	ELSE 'else'
	WHILE 'while'
	DO 'do'
	OD 'od'
	READ 'readln'
	WRITE 'write'
	ASSIGN ':='
	PLUS '+'
	MINUS '-'
	TIMES '*'
	DIV '/'
	EQ '=' 
	NE '<>' 
	LT '<' 
	LE '<=' 
	GT '>' 
	GE '>='
	AND '&&'
	OR '||'
	NOT '!'
	NULL 'null'                          
    TRUE 'true'                          
    FALSE 'false'
	RETURN '->'

    ALPHA=[A-Za-z]
	DIGIT=[0-9]
	NONZERO_DIGIT=[1-9]
	NEWLINE=\r|\n|\r\n
	WHITESPACE = {NEWLINE} | [ \t\f]
	ID = ({ALPHA}|_)({ALPHA}|{DIGIT}|_)*
	INT = (({NONZERO_DIGIT}+{DIGIT}*)|0)
	FLOAT = {INT}(\.{DIGIT}+)
	STRING_TEXT = [^\"]*
	COMMENT_TEXT = [\w\.\@]*
 </details>

## The Abstract Syntax Tree

The implemented Grammar has been enhanced with a series of *actions*, one for each production. 

Generally, these actions instantiate a *Node* object related to each of the *non-terminals* appearing in the right-hand side of the production itself. 
There are several different actions in this parser.

For example:

	VarDeclList ::= /* empty */ 					{: RESULT = new LinkedList<VariableDeclarationNode>(); :}
	| VarDecl:vd VarDeclList:vl					{: vl.add(vd); RESULT = vl; :}
	;

The *empty* production creates a new list containing the variables declarated, while the second one appends a given variable to the list. In a successful situation, one would expect the process to eventually resolve in the *empty* statement, thus instantiating the list and adding the items found. 

As the *Parser* elaborates a given source file, the corresponding (and unique) *Syntax Tree* is generated recursively. With the completion of the parsing process, a pointer to the root of the *Syntax tree* is returned, which comes in handy for the next step of the compiler.

## Tree visualization

Lastly, this implementation provides with a visualization of the *Syntactic Tree*, constructed in the previous step, via XML. The *Visitor* pattern fits this role perfectly. Once the parser has finished its job, one can call the *ToyVisitor* on the root of the tree, which will generate a .xml file based on the instance of the *Syntactic tree*.