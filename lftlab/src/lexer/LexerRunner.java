/*
 * @author: Alexandru Mocanu
 * Matricola 813322
 */

package lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class LexerRunner {

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/lexer/lexer.txt"; // il percorso del file da leggere
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
