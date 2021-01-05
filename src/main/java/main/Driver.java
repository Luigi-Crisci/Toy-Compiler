package main;

import java.io.BufferedReader;
import java.io.FileReader;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import lexer.Lexer;
import parser.nodes.*;
import parser.Parser;
import visitor.*;


public class Driver {

    public static void main(String[] args) throws Exception
    {
        String filename = "multAddDiff_plus1";
        // BufferedReader in = new BufferedReader(new FileReader(args[1]));
        BufferedReader in = new BufferedReader(new FileReader("/home/luigi/crisci-cuccurullo_es5_scg/examples/" + filename + ".toy"));
        // BufferedReader in = new BufferedReader(new FileReader("/home/luigi/compilatori/crisci-cuccurullo_es5/examples/variablesTests/" + filename + ".toy"));
        
        // BufferedReader in = new BufferedReader(new FileReader("/home/alecuc/crisci-cuccurullo_es5_scg/examples/" + filename + ".toy"));

        
        ComplexSymbolFactory f = new ComplexSymbolFactory();
        Lexer l = new Lexer(in,f);
        Parser p = new Parser(l,f);

        Symbol s = p.parse();
        ProgramNode programNode = (ProgramNode)s.value;
        
        ASTVisitor visitor = new ASTVisitor(filename);
        programNode.accept(visitor);
        visitor.flush();

        SemanticVisitor semanticVisitor = new SemanticVisitor();
        programNode.accept(semanticVisitor);

        ToyToCVisitor toyToCVisitor = new ToyToCVisitor(filename);
        programNode.accept(toyToCVisitor);
        toyToCVisitor.flush();

    }
}
