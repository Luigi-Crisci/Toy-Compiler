package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import lexer.Lexer;
import nodes.*;
import parser.Parser;
import visitor.ToyVisitor;


public class App {


    public void x(){

    }


    public static void main(String[] args) throws Exception
    {
        if(args.length < 2)
            return;
        
        BufferedReader in = new BufferedReader(new FileReader(args[1]));
        // BufferedReader in = new BufferedReader(new FileReader("D:\\Alessio_Cuccurullo\\gitrepo\\crisci-cuccurullo_es5_scg\\examples\\multAddDiff.toy"));
        ComplexSymbolFactory f = new ComplexSymbolFactory();
        Lexer l = new Lexer(in,f);
        Parser p = new Parser(l,f);

        Symbol s = p.parse();
        ProgramNode programNode = (ProgramNode)s.value;
        
        ToyVisitor visitor = new ToyVisitor("AST");
        programNode.accept(visitor);
        visitor.flush();
    }
}
