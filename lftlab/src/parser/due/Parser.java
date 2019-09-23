/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package parser.due;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    private Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /**
     * Legge il prossimo token e lo stampa
     */
    private void move() {
        look = lex.lexical_scan(pbr);
        if (look != null) // evito di stampare "token = null"
            System.err.println("token = " + look);
    }

    private void error(String s) {
        throw new Error("near line " + Lexer.line + ": " + s);
    }

    /**
     * @param t: è il token atteso. Se VERO, legge il prossimo token (se non è EOF).
     */
    private void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error.");
    }

    /**
     * Metodo di partenza.
     * GUI { ID , print , read , case , while , { }
     * 
     */
    private void prog() throws NullPointerException {
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag ==Tag.READ || look.tag ==Tag.CASE || look.tag ==Tag.WHILE || look.tag == '{') {
            statlist();
            if(look.tag == Tag.EOF) 
                match(Tag.EOF);
        } else {
            error("Error in prog. Found: " + look);
        }
    }

    /**
     * GUI { ID , print , read , case , while , { }
     */
    private void statlist(){
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.CASE || look.tag == Tag.WHILE || look.tag == '{') {
            stat();
            statlistp();
        } else
            error("Error in statlist. Found " + look);
    }

    /**
     * GUI { ; , EOF , } }
     */
    private void statlistp(){
        switch(look.tag) {
            case ';':
                match(';');
                stat();
                statlistp();
            case Tag.EOF:
            case '}':
                break; //epsilon
            default:
                error("Error in statlistp. Found " + look);
                break;
        }
    }

    /**
     * É il metodo che si occupa di guardare le istruzioni chiave
     * GUI { ID , print , read , case , while , { }
     */
    private void stat(){
        switch(look.tag) {
            case Tag.ID:
                match(Tag.ID);
                if(look.tag == Tag.ASSIGN) {
                    match(Tag.ASSIGN);
                    expr();
                }
                else 
                    error("Erroneous character after ID. Expected := but found: " + look);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                if(look.tag == '(') {
                    match('(');
                    expr();
                    if (look.tag == ')')
                        match(')'); //continuo a leggere
                    else
                        error("Erroneous character. Expected ) but found: " + look);
                } else
                    error("Erroneous character after PRINT. Expected ( but found: " + look);
                break;
            case Tag.READ:
                match(Tag.READ);
                if(look.tag == '(') {
                    match('(');
                    expr();
                    if(look.tag == ')')
                        match(')');
                    else 
                        error("Erroneous character. Expected ) but found: " + look);
                } else
                    error("Erroneous character after READ. Expected ( but found: " + look);
                break;
            case Tag.CASE:
                match(Tag.CASE);
                whenlist();
                if(look.tag == Tag.ELSE) {
                    match(Tag.ELSE);
                    stat();
                } else
                    error("Erroneous character after CASE. Expected ELSE but found: " + look);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                if(look.tag == '(') {
                    match('(');
                    bexpr();
                    if(look.tag == ')'){
                        match(')');
                        stat();
                    } else
                        error("Erroneous character. Expected ) but found: " + look);
                } else 
                    error("Erroneous character after WHILE. Expected ( but found: " + look);
                break;
            case '{':
                match('{');
                statlist();
                if (look.tag == '}') 
                    match('}');
                else 
                    error("Erroneous character. Expected } but found: " + look);
                break;
            }
    }

    /**
     * GUI { when }
     */
    private void whenlist(){
        if (look.tag == Tag.WHEN) {
            whenitem();
            whenlistp();
        } else
            error("Erroneous character in whenlist. Expected WHEN but found: " + look);
    }

    /**
     * GUI { when , else }
     */
    private void whenlistp(){
        switch(look.tag) {
            case Tag.WHEN:
                whenitem();
                whenlistp();
                break;
            case Tag.ELSE:
                break;
            default:
                error("Erroneous character in whenlistp. Expected ) but found: " + look);
                break;
        }
    }

    /**
     * GUI { when }
     */
    private void whenitem(){
        if(look.tag == Tag.WHEN) {
            match(Tag.WHEN);
            if (look.tag == '('){
                match('(');
                bexpr();
                if (look.tag == ')') {
                    match(')');
                    stat();
                } else
                    error("Erroneous character. Expected ) but found: " + look);
            }
        } else
            error("Error in whenitem. Expected WHEN but found: " + look);
    }

    /**
     * GUI { ( , NUM , ID }
     */
     private void bexpr(){
         if(look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
             expr();
             if (look.tag == Tag.RELOP) {
                 match(Tag.RELOP);
                 expr();
             } else
                 error("Erroneous character in bexpr. Invalid boolean expression");
         }
         else
             error("Erroneous character in bexpr. Erroneous starting symbol");
     }

    /**
     * GUI { ( , NUM , ID }
     */
    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            term();
            exprp();
        } else
            error("Erroneous character in expr. Found " + look);
    }

    /**
     * GUI { + , - , ) , ; , RELOP. EOF, when , else , } }
     */
    private void exprp(){
        switch (look.tag)  {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
            case ';':
            case Tag.EOF:
            case ')':
            case Tag.RELOP:
            case Tag.WHEN:
            case Tag.ELSE:
            case '}':
                break;
            default:
                error("Erroneous character in exprp. Found " + look);
                break;
        }
    }

    /**
     * GUI { ( , NUM , ID }
     */
    private void term(){
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            fact();
            termp();
        } else
            error("Erroneous character in term. Found " + look);
    }

    /**
     * GUI { * , / , + , - , ; , EOF , ) , when , else , } }
     */
    private void termp (){
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;
            case '+':
            case '-':
            case ';':
            case Tag.EOF:
            case ')':
            case Tag.RELOP:
            case Tag.WHEN:
            case Tag.ELSE:
            case '}':
                break;
            default:
                error("Erroneous character in termp. Found " + look);
                break;
        }
    }

    /**
     * GUI { ( , NUM , ID }
     */
    private void fact() {
        switch (look.tag) {
            case '(':
                match('(');
                expr();
                if (look.tag == ')')
                    match(')');
                else
                    error("Erroneous character in Fact. Expected ) but missing.");
                break;
            case Tag.NUM:
                match(Tag.NUM);
                if (look.tag == '(')
                    error("Erroneous character in fact. Found: NUM(");
                break;
            case Tag.ID:
                match(Tag.ID);
                if (look.tag == '(')
					error("Erroneous character in fact. Found: ID(");
                break;
            default:
                error("Erroneous character in Fact. Found " + look);
                break;
        }
     }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/alex/Desktop/git/lftlab/lftlab/src/parser/due/parser2.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            parser.due.Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("\nInput OK\n");
            br.close();
        } catch (IOException e) {
            System.out.println("Errore del file. Nessun file con questo nome.");
//            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Letto simbolo non accettato.\nFine del programma.");
            // e.printStackTrace();
        }
    }

}
