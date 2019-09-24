/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package lexer;

import java.io.BufferedReader;
import java.io.IOException;


public class Lexer {
    public static int line = 1; // linea corrente che viene inizializzata a 1
    private char peek = ' '; // rappresenta il carattere corrente inizializzato a ' '

    private void readch(BufferedReader br) { // metodo che legge il prossimo carattere
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    } //readch

    public Token lexical_scan(BufferedReader br) { // metodo che esegue la traduzione in token del testo in input
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }

        switch (peek) { // gestione della casistica del carattere
            case '!':
                peek = ' ';
                return Token.not;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                readch(br); // leggi prossimo carattere
                if (peek == '/') { // inizia un commento del tipo "//" che finisce con l'accapo
                    readch(br);
                    while (peek != '\n' && peek != -1) readch(br); //TODO manca caso con commento che finisce senza accapo
                        readch(br);
                        return lexical_scan(br);
                    
                } else if (peek == '*') { //inizia un commento del tipo "/*"
                    readch(br);
                    boolean flag = false; //flag che controlla che il commento venga chiuso
                    while (!flag) {
                        if (peek == (char) -1) break; //se raggiunge EOF finisce.
                        readch(br);
                        if (peek == '*') { //continua a leggere finché non trova *
                            readch(br);
                            if (peek == '/') flag = true; //si è chiuso un commento del tipo */
                        }
                    }
                    if (!flag) {//flag = false, quindi non si è chiuso il commento
                        System.err.println("Errore. Commento non chiuso");
                        return new Token(Tag.EOF);
                    } else {
                        readch(br);
                        return lexical_scan(br);
                    }
                } else
                    return Token.div;  //in questo caso non sarà un commento ma il carattere DIV
            case ';':
                peek = ' ';
                return Token.semicolon;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : \""  + peek + "\"");
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character" + " afer | : \""  + peek + "\"");
                    return null;
                }
            case ':':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.assign;
                } else {
                    System.err.println("Erroneous character"
                            + " after : : \""  + peek + "\"");
                    return null;
                }
            case '<':
                readch(br);
                switch(peek){
                    case '=':
                        peek = ' ';
                        return Word.le;
                    case '>':
                        peek = ' ';
                        return Word.ne;
                    default:
                        return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                }
                return Word.gt;
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : \""  + peek + "\"" );
                    return null;
                }
            case '_': //caso dell'ìdentificatore che inizia con "_"
                readch(br);
                if (peek == ' ') { //un identificatore non può contenere solo "_"
                    System.err.println("Erroneous character"
                            + " after _ : \""  + peek + "\"");
                    return null;
                } else {
                    String s = "_";
                    while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                        s = s + peek;
                        readch(br);
                    }
                    return new Word(Tag.ID, s);
                }
            case (char)-1:
                return new Token(Tag.EOF);
            default:  //il caso dell'identificatore che può iniziare con solo con una lettera o con "_"
                if (Character.isLetter(peek)) { // se il carattere è una lettera
                    String s = "";
                    while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                        s = s + peek;
                        readch(br);
                    }
                    if(s.compareTo("print") == 0)
                        return Word.print;
                    else if (s.compareTo("read") == 0 )
                        return Word.read;
                    else if (s.compareTo("while") == 0)
                        return Word.whiletok;
                    else if (s.compareTo("else") == 0)
                        return Word.elsetok;
                    else if (s.compareTo("if") == 0)
                        return Word.iftok;
                    else if (s.compareTo("when") == 0)
                        return Word.when;
                    else if (s.compareTo("case") == 0)
                        return Word.casetok;
                    else if (s.compareTo("do") == 0)
                        return Word.dotok;
                    else if (s.compareTo("then") == 0)
                        return Word.then;
                    else
                        return new Word(Tag.ID, s);

                } else if (Character.isDigit(peek)) { // se il carattere è un numero
                    int n = Character.getNumericValue(peek);
                    int r;
                    readch(br);
                    while (Character.isDigit(peek)) {
                        r = Character.getNumericValue(peek);
                        n = n*10+r;
                        readch(br);
                    }
                    return new NumberTok(Tag.NUM, String.valueOf(n));
                } else {
                    System.err.println("Erroneous character: " + peek);
                    return null;
                }//case default
        }
    } //lexical_scan
}
