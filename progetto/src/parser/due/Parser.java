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
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag ==Tag.READ || look.tag ==Tag.CASE || look.tag ==Tag.WHILE || look.tag == '{') {
            stat();
            statlistp();
        } else
            error("Error in statlist. Found " + look);
    }
    private void statlistp(){
        switch(look.tag) {
            case ';':
                match(look.tag);
                stat();
                statlistp();
            case Tag.EOF:
                break; //epsilon
            case '}':
                break; //epsilon
            default:
                error("Error in statlistp. Found " + look);
                break;
        }

    }
    private void stat(){
        switch(look.tag) {
            case Tag.ID:
                match(Tag.ID);
                if(look.tag == Tag.ASSIGN) {
                    match(look.tag);
                    expr();
                }
                else 
                    error("Erroneous character after ID. Expected := but found: " + look);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                if(look.tag == '(') {
                    match(look.tag);
                    expr();
                    if (look.tag == ')')
                        match(look.tag); //continuo a leggere
                    else
                        error("Erroneous character. Expected ) but found: " + look);
                } else
                    error("Erroneous character after PRINT. Expected ( but found: " + look);
                break;
            case Tag.READ:
                match(Tag.READ);
                if(look.tag == '(') {
                    match(look.tag);
                    // Tag.ID
                    if(look.tag == ')')
                        match(look.tag);
                    else 
                        error("Erroneous character. Expected ) but found: " + look);
                } else
                    error("Erroneous character after READ. Expected ( but found: " + look);
                break;
            case Tag.CASE:
                match(Tag.CASE);
                whenlist();
                if(look.tag == Tag.ELSE) {
                    match(look.tag);
                    stat();
                } else
                    error("Erroneous character after CASE. Expected ELSE but found: " + look);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                if(look.tag == '(') {
                    match(look.tag);
                    bexpr();
                    if(look.tag == ')'){
                        match(look.tag);
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
                    match(look.tag);
                else 
                    error("Erroneous character. Expected } but found: " + look);
                break;
            }
    }

    /**
     * 
     */
    private void whenlist(){
        if (look.tag) {
            whenitem();
            whenlistp();
        } else
            error("Erroneous character in whenlist. Expected WHEN but found: " + look);
    }

    /**
     * 
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
                error("Erroneous character in WhenListP, expected ) but found: " + look);
                break;
        }
    }


    /**
     * 
     */
    private void whenitem(){
        if(look.tag == Tag.WHEN) {
            match(look.tag);
            if (look.tag == '('){
                match(look.tag);
                
            }
        }
    }

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
            parser.prog();
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