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
        if (look != null) // evito di stampare "token = null"
            System.err.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    /**
     * @param t: è il token atteso. Se VERO, legge il prossimo token (se non è EOF).
     */
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    /**
     * Partenza. 
     * 
     */
    public void prog() throws NullPointerException {
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag ==Tag.READ || look.tag ==Tag.CASE || look.tag ==Tag.WHILE || look.tag == '{')
            expr();
        match(Tag.EOF);
    }

    private void statlist(){
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag ==Tag.READ || look.tag ==Tag.CASE || look.tag ==Tag.WHILE || look.tag == '{')

    }
    private void statlistp(){
        if(look.tag == ';')
    }
    private void stat(){
        switch(look.tag) {
            case Tag.ID:
                match(Tag.ID);
                if(look.tag == Tag.ASSIGN) {
                    match(look.tag);
                    expr();
                }
                else error("Erroneous character after ID. Expected := but found: " + look);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                break;
            case Tag.READ:
                match(Tag.READ);
                break;
            case Tag.CASE:
                match(Tag.CASE);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                break;
            case '{':
                match('{');
                break;

        }

    // }
    // private void whenlist(){
    //     if()
    // }
    // private void whenlistp(){
    //     if()
    // }
    // private void whenitem(){
    //     if()
    // }
    // private void bexpr(){
    //     if()
    // }
    // private void expr(){
    //     if()
    // }
    // private void exprp(){
    //     if()
    // }
    // private void term(){
    //     if()
    // }
    // private void termp (){
    //     if()
    // }
    // private void fact(){
    //     if()
    // }


    /**
     * E ::= T E'
     */
    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term();
            exprp();
        } else
            error("Syntax error");
    }

    /**
     * E' ::= + T E' E' | - T E' | ε
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
     * T ::= F T' può iniziare con ( o un numero
     */
    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact();
            termp();
        } else
            error("Syntax error");
    }

    /**
     * T' ::= * F T' | / F T' | ε
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
     * F ::= (E) | NUM
     */
    private void fact() {
        switch (look.tag) {
        case Tag.NUM: // ho letto un NUM
            match(Tag.NUM);
            break;
        case '(': // ho letto una (
            match(look.tag);
            expr();
            if (look.tag == ')') // dopo aver letto E devo trovare una ), altrimenti errore.
                match(')');
            else
                error("Syntax error");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "progetto/parser.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("\nInput OK\n");
            br.close();
        } catch (IOException e) {
            System.out.println("Errore del file. Nessun file con questo nome.");
            // e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Letto simbolo non accettato.\nFine del programma.");
            // e.printStackTrace();
        }
    }
}