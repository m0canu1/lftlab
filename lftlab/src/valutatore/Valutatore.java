/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package valutatore;

import lexer.Lexer;
import lexer.NumberTok;
import lexer.Tag;
import lexer.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    private Valutatore(Lexer l, BufferedReader br) {
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
            System.out.println("token = " + look);
    }

    private void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    /**
     * @param t: è il token atteso. Se VERO, legge il prossimo token (se non è EOF).
     */
    private void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else
            error("syntax error");
    }

    private void start() {
        int expr_val;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            expr_val = expr();
            match(Tag.EOF);
            System.out.println("start: result = " + expr_val);
        } else
            error("Erroneous character in start. Expected ( or NUM, but found" + look);
    }


    private int expr() {
        int term_val, exprp_val = 0;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term_val = term();
            exprp_val = exprp(term_val); //TODO perché term_val come parametro?
        } else
            error("Erroneous character in expr. Expected ( or NUM, but found" + look);
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val = 0;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            case ')':
            case Tag.EOF:
                exprp_val = exprp_i;
                break;
            default:
                error("Erroneous character in exprp. Expected +, -, { o EOF,  but found" + look);
                break;
        }
        return exprp_val;
    }

    private int term() {
        int fact_val, termp_val = 0; //TODO perché termp_val è inizializzato a 0?
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact_val = fact();
            termp_val = termp(fact_val);
        } else
            error("Erroneous character in term. Expected ( or NUM,  but found" + look);
        return termp_val;
    }

    private int termp(int termp_i) {
        int fact_val;
        int termp_val = 0;

        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                termp_val = termp_i;
                break;
            default:
                error("Erroneous character in termp. Expected *, /, +, -, ) or EOF ,  but found" + look);
                break;
        }
        return termp_val;
    }

    private int fact() {
        int fact_val = 0;
        switch (look.tag) {
            case Tag.NUM:
                fact_val = Integer.parseInt(((NumberTok)look).lexeme);
                match(Tag.NUM);
                break;
            case '(':
                match('(');
                fact_val = expr();
                if (look.tag == ')')
                    match(')');
                else
                    error("Erroneous character in fact. Expected ) but found " + look);
                break;
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/alex/Desktop/git/lftlab/lftlab/src/valutatore/valutatore.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
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
