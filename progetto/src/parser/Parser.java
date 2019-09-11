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
    void move() { //legge il prossimo token e lo stampa
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    /**
     * verifica di non essere arrivati alla fine del file
     * e che non ci siano errori di tipo sintattico.
     * Ci si aspetta che il token sia esattamente quello atteso
     */
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    /**
     * metodo d'inizio
     */
    public void start() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            expr();
            match(Tag.EOF);
        } else error("Syntax error");
    }
    private void expr() {
        String s = "<expr>";
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term();
            exprp();
        } else error("Syntax error");
    }

    /*
    TODO: verificare correttezza
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
    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact();
            termp();
        } else error("Syntax error");
    }
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
        String path = "parser.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}