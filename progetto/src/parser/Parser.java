package parser;

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
    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /**
     * Legge il prossimo token e lo stampa
     */
    void move() {
        look = lex.lexical_scan(pbr);
        if(look != null) //evito di stampare "token ="
            System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    /**
     * @param t: è il token atteso. Se VERO, legge il prossimo token (se non è EOF).
     */
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    /**
     * Partenza.
     * Gli unici caratteri accettati in partenza sono "(" o un Numero
     */
    public void start() throws NullPointerException {
        if (look.tag == '(' || look.tag == Tag.NUM) 
            expr();
        match(Tag.EOF);
    }

    /**
     * (expr) ::= (term) (exprp)
     */
    private void expr() {
            term();
            exprp();
    }

    /**
     * (expr) ::= + (term) (exprp) 
     * (expr)   | - (term) (exprp)
     *         | ε
     */
    private void exprp() {
        switch (look.tag) {
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
            case ')':
                break;
            case Tag.EOF:
                break;
            default:
                error("Syntax error");
                break;
        }
    }

    /**
     * (term) ::= (fact) (termp)
     * può iniziare con ( o un numero
     */
    private void term() {
        // if (look.tag == '(' || look.tag == Tag.NUM) {
            fact();
            termp();
        // } else error("Syntax error");
    }

    /**
     * () ::= * (fact)(termp)
     *      | / (fact)(termp)
     *      | ε
     */
    private void termp() {
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
                break;
            case '-':
                break;
            case ')':
                break;
            case Tag.EOF:
                break;
            default:
                error("Syntax error");
        }
    }

    /**
     * (fact) ::= ((expr)) | NUM
     */
    private void fact() {
        switch (look.tag) {
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            default:
                match('(');
                expr();
                if (look.tag == ')')
                    match(')');
                else
                    error("Syntax error");
                break;
        }
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "progetto/parser.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
        catch (NullPointerException e) {System.err.println("Simbolo non accettato.");}
    }
}