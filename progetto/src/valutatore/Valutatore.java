package valutatore;

import java.io.BufferedReader;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;


public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { lex = l; pbr = br; move(); }

void move() { // come in Esercizio 3.1 } 
void error(String s) { // come in Esercizio 3.1 } 
void match(int t) { // come in Esercizio 3.1 } 


}

}