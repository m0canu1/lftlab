/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package parser.uno;

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
        if(look != null) //evito di stampare "token = null"
            System.out.println("token = " + look);
    }

    private void error(String s) {
        throw new Error("near line " + Lexer.line + ": " + s);
    }

    /**
     * @param t: è il token atteso. Se VERO, legge il prossimo token (se non è EOF).
     */
    private void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    /**
     * Partenza.
     * Gli unici caratteri accettati in partenza sono "(" o un Numero
     * S ::= E
     * GUI { (, NUM }
     */
    private void start() throws NullPointerException {
        if (look.tag == '(' || look.tag == Tag.NUM) 
            expr();
        match(Tag.EOF);
    }

    /**
     * E ::= T E'
     * GUI { (, NUM }
     */
    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term();
            exprp();
        } else
            error("Syntax error");
    }

    /**
     * E' ::= + T E' 
     * E'   | - T E'
     *         | ε
     *
     *  GUI { + , - , ) , EOF }
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
            case Tag.EOF:
                break; //epsilon
            default:
                error("Syntax error");
                break;
        }
    }

    /**
     * T ::= F T'
     * può iniziare con ( o un numero
     *
     * GUI { (, NUM }
     */
    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact();
            termp();
        } else
            error("Syntax error");
    }

    /**
     * T' ::= * F T'
     *      | / F T'
     *      | ε
     *
     *  GUI { * , / , + , - , ) , EOF }
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
            case '-':
            case ')':
            case Tag.EOF:
                break;
            default:
                error("Syntax error");
        }
    }

    /**
     * F ::= (E) | NUM
     * GUI { (, NUM }
     */
    private void fact() {
        switch (look.tag) {
            case Tag.NUM: //ho letto un NUM
                match(Tag.NUM);
                break;
            case '(': //ho letto una (
                match(look.tag);
                expr();
                if (look.tag == ')') //dopo aver letto E devo trovare una ), altrimenti errore.
                    match(')');
                else
                    error("Syntax error");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/parser/uno/parser1.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("\nInput OK\n");
            br.close();
        } catch (IOException e) {
            System.out.println("Errore del file. Nessun file con questo nome.");
            // e.printStackTrace();
        }
        catch (NullPointerException e) {
            System.out.println("Letto simbolo non accettato.\nFine del programma.");
            // e.printStackTrace();
        }
    }

}
