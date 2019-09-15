
/**
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Lexer {
    public static int line = 1; // linea corrente che viene inizializzata a 1
    private char peek = ' '; // rappresenta il carattere corrente inizializzato a ' '
    public static String nmb; // Stringa che contiene il valore numerico per l'analizzatore sintattico

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
                readch(br);
                if (peek == '/') return lexical_scan(br);
                else if (Character.isLetter(peek) || Character.isDigit(peek)) return Token.div;  //in questo caso non sarà un commento ma il carattere DIV
                else if (peek == '*') { //inizia un commento del tipo "/*"
                    readch(br);
                    boolean flag = false; //flag che controlla che il commento si chiuda correttamente
                    while (peek != '\n' || !flag) {
                        if (peek == (char) -1) break; //
                        readch(br);
                        if (peek == '*') {
                            readch(br);
                            if (peek == '/') flag = true; //fine del commento con "*/"
                        }
                    }
                    if (!flag) {//flag = false, quindi non si è chiuso il commento
                        System.err.println("Errore. Commento non chiuso");
                        return new Token(Tag.EOF);
                    } else {
                        readch(br);
                        return lexical_scan(br);
                    }
                }
                else if (peek != '/') { // inizia un commento del tipo "//" che finisce con l'accapo
                    readch(br);
                    while (peek != '\n') readch(br);
                    readch(br);
                    return lexical_scan(br);
                }
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
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character" + " afer | : " + peek);
                    return null;
                }
            case ':':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.assign;
                } else {
                    System.err.println("Erroneous character"
                            + " after : : "  + peek );
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
                            + " after = : "  + peek );
                    return null;
                }
            case (char)-1:
                return new Token(Tag.EOF);

            default:
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

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "lexer.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }

}
